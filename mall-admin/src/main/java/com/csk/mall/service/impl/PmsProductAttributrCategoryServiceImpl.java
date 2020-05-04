package com.csk.mall.service.impl;

import com.csk.mall.dao.PmsProductAttributeCategoryDao;
import com.csk.mall.dto.PmsProductAttributeCategoryItem;
import com.csk.mall.mapper.PmsProductAttributeCategoryMapper;
import com.csk.mall.model.PmsProductAttributeCategory;
import com.csk.mall.model.PmsProductAttributeCategoryExample;
import com.csk.mall.service.PmsProductAttributeCategoryService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @description: 商品属性分类相关实现类
 * @author: caishengkai
 * @time: 2020/5/1 15:44
 */
@Service
public class PmsProductAttributrCategoryServiceImpl implements PmsProductAttributeCategoryService {

    @Autowired
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Autowired
    private PmsProductAttributeCategoryDao productAttributeCategoryDao;

    @Override
    public List<PmsProductAttributeCategory> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductAttributeCategoryExample example = new PmsProductAttributeCategoryExample();
        if (StringUtils.hasText(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return productAttributeCategoryMapper.selectByExample(example);
    }

    @Override
    public int create(String name) {
        PmsProductAttributeCategory category = new PmsProductAttributeCategory();
        category.setName(name);
        return productAttributeCategoryMapper.insertSelective(category);
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory category = new PmsProductAttributeCategory();
        category.setName(name);
        category.setId(id);
        return productAttributeCategoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public int delete(Long id) {
        return productAttributeCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return productAttributeCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryDao.getListWithAttr();
    }
}
