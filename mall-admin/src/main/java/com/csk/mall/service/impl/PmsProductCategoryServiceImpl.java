package com.csk.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.csk.mall.dao.PmsProductCategoryAttributeRelationDao;
import com.csk.mall.dao.PmsProductCategoryDao;
import com.csk.mall.dto.PmsProductCategoryParam;
import com.csk.mall.dto.PmsProductCategoryWithChildrenItem;
import com.csk.mall.mapper.PmsProductCategoryAttributeRelationMapper;
import com.csk.mall.mapper.PmsProductCategoryMapper;
import com.csk.mall.model.PmsProductCategory;
import com.csk.mall.model.PmsProductCategoryAttributeRelation;
import com.csk.mall.model.PmsProductCategoryAttributeRelationExample;
import com.csk.mall.model.PmsProductCategoryExample;
import com.csk.mall.service.PmsProductCategoryService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 商品分类相关实现类
 * @author: caishengkai
 * @time: 2020/5/1 15:17
 */
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private PmsProductCategoryDao productCategoryDao;
    @Autowired
    private PmsProductCategoryAttributeRelationDao productCategoryAttributeRelationDao;
    @Autowired
    private PmsProductCategoryAttributeRelationMapper productCategoryAttributeRelationMapper;

    @Override
    public List<PmsProductCategory> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        return productCategoryMapper.selectByExample(example);
    }

    @Override
    public int create(PmsProductCategoryParam productCategoryParam) {
        PmsProductCategory category = new PmsProductCategory();
        category.setProductCount(0);
        BeanUtils.copyProperties(productCategoryParam, category);
        //根据parentId设置商品分类级别
        setCategoryLevel(category);
        int count = productCategoryMapper.insertSelective(category);
        //生成商品分类和属性的关系表数据
        List<Long> attributeIds = productCategoryParam.getProductAttributeIdList();
        if (CollectionUtil.isNotEmpty(attributeIds)) {
            insertRelationList(category.getId(), attributeIds);
        }
        return count;
    }


    @Override
    public int update(Long id, PmsProductCategoryParam productCategoryParam) {
        PmsProductCategory category = new PmsProductCategory();
        category.setId(id);
        BeanUtils.copyProperties(productCategoryParam, category);
        setCategoryLevel(category);
        int count = productCategoryMapper.updateByPrimaryKeySelective(category);

        //TODO 更新商品信息中商品分类的名字

        //更新商品分类和商品属性关联表信息
        //先删除原来的关系
        PmsProductCategoryAttributeRelationExample example = new PmsProductCategoryAttributeRelationExample();
        example.createCriteria().andProductCategoryIdEqualTo(id);
        productCategoryAttributeRelationMapper.deleteByExample(example);
        //重新添加关系
        if (CollectionUtil.isNotEmpty(productCategoryParam.getProductAttributeIdList())) {
            insertRelationList(id, productCategoryParam.getProductAttributeIdList());
        }
        return count;
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return productCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        //TODO 关联关系未做处理
        return productCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return productCategoryDao.listWithChildren();
    }

    /**
     *  生成商品分类和属性的关系表数据
     * @param categoryId 商品分类id
     * @param attributeIds 商品属性ids
     */
    private void insertRelationList(Long categoryId, List<Long> attributeIds) {
        List<PmsProductCategoryAttributeRelation> list = new ArrayList<>();
        for (Long attributeId : attributeIds) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(attributeId);
            relation.setProductCategoryId(categoryId);
            list.add(relation);
        }
        productCategoryAttributeRelationDao.insertList(list);
    }

    /**
     * 根据parentId设置商品分类级别
     * @param category 商品分类
     */
    private void setCategoryLevel(PmsProductCategory category) {
        if (category.getParentId() == 0) {
            category.setLevel(0);
        } else {
            PmsProductCategory parentCategory = productCategoryMapper.selectByPrimaryKey(category.getParentId());
            if (parentCategory != null) {
                category.setLevel(parentCategory.getLevel() + 1);
            } else {
                category.setLevel(0);
            }
        }
    }
}
