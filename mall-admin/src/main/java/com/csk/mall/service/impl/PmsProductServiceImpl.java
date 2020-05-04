package com.csk.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.csk.mall.dao.PmsProductAttributeValueDao;
import com.csk.mall.dao.PmsProductDao;
import com.csk.mall.dao.PmsSkuStockDao;
import com.csk.mall.dto.PmsProductParam;
import com.csk.mall.dto.PmsProductQueryParam;
import com.csk.mall.dto.PmsProductResult;
import com.csk.mall.mapper.PmsProductAttributeValueMapper;
import com.csk.mall.mapper.PmsProductMapper;
import com.csk.mall.mapper.PmsSkuStockMapper;
import com.csk.mall.model.*;
import com.csk.mall.service.PmsProductService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 商品相关业务实现类
 * @author: caishengkai
 * @time: 2020/5/2 15:05
 */
@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsProductDao productDao;
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private PmsSkuStockDao skuStockDao;
    @Autowired
    private PmsProductAttributeValueMapper productAttributeValueMapper;
    @Autowired
    private PmsProductAttributeValueDao productAttributeValueDao;

    @Override
    public int create(PmsProductParam productParam) {
        PmsProduct product = productParam;
        int count = productMapper.insertSelective(product);
        Long productId = product.getId();

        //处理sku的编码
        handleSkuStockCode(productParam.getSkuStockList(),productId);
        //添加sku库存信息
        skuStockDao.insertList(productParam.getSkuStockList(), productId);
        //添加商品参数,添加自定义商品规格
        productAttributeValueDao.insertList(productParam.getProductAttributeValueList(), productId);

        return count;
    }

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        return productMapper.selectByExample(productExample);
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productDao.getUpdateInfo(id);
    }

    @Override
    public int update(Long productId, PmsProductParam productParam) {
        PmsProduct product = productParam;
        product.setId(productId);
        int count = productMapper.updateByPrimaryKeySelective(product);

        //更新sku库存信息
        handleUpdateSkuStockList(productParam.getSkuStockList(), productId);
        //更新自定义商品规格信息
        //先删除原来的
        PmsProductAttributeValueExample productAttributeValueExample = new PmsProductAttributeValueExample();
        productAttributeValueExample.createCriteria().andProductIdEqualTo(productId);
        productAttributeValueMapper.deleteByExample(productAttributeValueExample);
        //再新增
        if (CollectionUtil.isNotEmpty(productParam.getProductAttributeValueList())) {
            productAttributeValueDao.insertList(productParam.getProductAttributeValueList(), productId);
        }
        return count;
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(record, example);
    }

    /**
     * 更新sku库存信息，已存在的做更新操作，不改变sku_code
     * TODO 目前删除是直接真删除，应该要做成下架
     * @param skuStockList
     * @param productId
     */
    private void handleUpdateSkuStockList(List<PmsSkuStock> skuStockList, Long productId) {
        //如果更新的sku库存为空，则直接删除原来的数据
        if (CollectionUtil.isEmpty(skuStockList)) {
            PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
            skuStockExample.createCriteria().andProductIdEqualTo(productId);
            skuStockMapper.deleteByExample(skuStockExample);
            return;
        }

        //获取初始sku信息
        PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
        skuStockExample.createCriteria().andProductIdEqualTo(productId);
        List<PmsSkuStock> oriStuList = skuStockMapper.selectByExample(skuStockExample);
        //获取新增的sku信息
        List<PmsSkuStock> insertSkuList = skuStockList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        //获取需要更新的sku信息
        List<PmsSkuStock> updateSkuList = skuStockList.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        //获取需要更新的sku信息ID
        List<Long> updateSkuIdList = updateSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
        //获取需要删除的sku信息
        List<PmsSkuStock> deleteSkuList = oriStuList.stream().filter(item -> !updateSkuIdList.contains(item.getId())).collect(Collectors.toList());
        //给新增的sku生成sku_code
        handleSkuStockCode(insertSkuList, productId);
        //新增sku
        skuStockDao.insertList(insertSkuList, productId);
        //删除sku
        if (CollectionUtil.isNotEmpty(deleteSkuList)) {
            PmsSkuStockExample remvodeSkuStockExample = new PmsSkuStockExample();
            skuStockExample.createCriteria().andProductIdEqualTo(productId);
            skuStockMapper.deleteByExample(remvodeSkuStockExample);
        }
        //修改sku
        if(CollUtil.isNotEmpty(updateSkuList)){
            for (PmsSkuStock pmsSkuStock : updateSkuList) {
                skuStockMapper.updateByPrimaryKeySelective(pmsSkuStock);
            }
        }
    }

    /**
     * 根据规则生成sku_code
     * 规则：日期+四位商品id+三位索引id
     * @param skuStockList
     * @param productId
     */
    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if (CollectionUtil.isNotEmpty(skuStockList)) {
            for(int i=0; i<skuStockList.size(); i++){
                PmsSkuStock skuStock = skuStockList.get(i);
                if(StringUtils.isEmpty(skuStock.getSkuCode())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    StringBuilder sb = new StringBuilder();
                    //日期
                    sb.append(sdf.format(new Date()));
                    //四位商品id
                    sb.append(String.format("%04d", productId));
                    //三位索引id
                    sb.append(String.format("%03d", i+1));
                    skuStock.setSkuCode(sb.toString());
                }
            }
        }
    }

}
