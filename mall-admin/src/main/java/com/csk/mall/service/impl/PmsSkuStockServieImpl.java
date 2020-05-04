package com.csk.mall.service.impl;

import com.csk.mall.mapper.PmsSkuStockMapper;
import com.csk.mall.model.PmsSkuStock;
import com.csk.mall.model.PmsSkuStockExample;
import com.csk.mall.service.PmsSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @description: 商品sku库存相关实现类
 * @author: caishengkai
 * @time: 2020/5/4 14:48
 */
@Service
public class PmsSkuStockServieImpl implements PmsSkuStockService {

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria().andProductIdEqualTo(pid);
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andSkuCodeLike("%" + keyword + "%");
        }
        return skuStockMapper.selectByExample(example);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return 0;
    }
}
