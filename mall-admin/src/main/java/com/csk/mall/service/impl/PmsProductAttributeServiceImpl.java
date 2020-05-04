package com.csk.mall.service.impl;

import com.csk.mall.dao.PmsProductCategoryAttributeRelationDao;
import com.csk.mall.dto.PmsProductAttributeParam;
import com.csk.mall.dto.ProductAttrInfo;
import com.csk.mall.mapper.PmsProductAttributeCategoryMapper;
import com.csk.mall.mapper.PmsProductAttributeMapper;
import com.csk.mall.model.PmsProductAttribute;
import com.csk.mall.model.PmsProductAttributeCategory;
import com.csk.mall.model.PmsProductAttributeExample;
import com.csk.mall.service.PmsProductAttributeService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @description: 商品属性相关实现类
 * @author: caishengkai
 * @time: 2020/5/1 15:32
 */
@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    @Autowired
    private PmsProductAttributeMapper productAttributeMapper;
    @Autowired
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Autowired
    private PmsProductCategoryAttributeRelationDao productCategoryAttributeRelationDao;

    @Override
    public List<PmsProductAttribute> list(Long cid, Integer type, String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andProductAttributeCategoryIdEqualTo(cid).andTypeEqualTo(type);
        if (StringUtils.hasText(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return productAttributeMapper.selectByExample(example);
    }

    @Override
    public int create(PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam, productAttribute);
        int count = productAttributeMapper.insertSelective(productAttribute);
        //新增商品属性后需要更新属性分类的数量
        PmsProductAttributeCategory category = productAttributeCategoryMapper.selectByPrimaryKey(productAttribute.getProductAttributeCategoryId());
        if (productAttribute.getType() == 0) {
            category.setAttributeCount(category.getAttributeCount() + 1);
        } else if (productAttribute.getType() == 1) {
            category.setParamCount(category.getParamCount() + 1);
        }
        productAttributeCategoryMapper.updateByPrimaryKeySelective(category);
        return count;
    }

    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        productAttribute.setId(id);
        BeanUtils.copyProperties(productAttributeParam, productAttribute);
        return productAttributeMapper.updateByPrimaryKeySelective(productAttribute);
    }

    @Override
    public PmsProductAttribute getItem(Long id) {
        return productAttributeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        //获取分类
        PmsProductAttribute attribute = productAttributeMapper.selectByPrimaryKey(ids.get(0));
        PmsProductAttributeCategory category = productAttributeCategoryMapper.selectByPrimaryKey(attribute.getProductAttributeCategoryId());
        //删除属性
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andIdIn(ids);
        int count = productAttributeMapper.deleteByExample(example);
        //更新分类属性数量
        if (attribute.getType() == 0) {
            if (category.getAttributeCount() > count) {
                category.setAttributeCount(category.getAttributeCount() - count);
            } else {
                category.setAttributeCount(0);
            }
        } else if (attribute.getType() == 1) {
            if (category.getParamCount() > count) {
                category.setParamCount(category.getParamCount() - count);
            } else {
                category.setParamCount(0);
            }
        }
        productAttributeCategoryMapper.updateByPrimaryKeySelective(category);
        return count;
    }

    @Override
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return productCategoryAttributeRelationDao.getProductAttrInfo(productCategoryId);
    }
}
