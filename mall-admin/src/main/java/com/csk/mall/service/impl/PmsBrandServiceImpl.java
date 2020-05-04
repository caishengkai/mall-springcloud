package com.csk.mall.service.impl;

import com.csk.mall.dto.PmsBrandParam;
import com.csk.mall.mapper.PmsBrandMapper;
import com.csk.mall.model.PmsBrand;
import com.csk.mall.model.PmsBrandExample;
import com.csk.mall.service.PmsBrandService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @description: 商品品牌相关实现类
 * @author: caishengkai
 * @time: 2020/5/2 15:36
 */
@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Autowired
    private PmsBrandMapper brandMapper;

    @Override
    public List<PmsBrand> listAll() {
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public int create(PmsBrandParam param) {
        PmsBrand brand = new PmsBrand();
        BeanUtils.copyProperties(param, brand);
        return brandMapper.insertSelective(brand);
    }

    @Override
    public int update(Long id, PmsBrandParam param) {
        PmsBrand brand = new PmsBrand();
        brand.setId(id);
        BeanUtils.copyProperties(param, brand);

        //TODO 更新品牌时要更新商品中的品牌名称

        return brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public int delete(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PmsBrand> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsBrandExample example = new PmsBrandExample();
        if (StringUtils.hasText(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return brandMapper.selectByExample(example);
    }

    @Override
    public PmsBrand getItem(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        return brandMapper.deleteByExample(example);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setShowStatus(showStatus);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.createCriteria().andIdIn(ids);
        return brandMapper.updateByExampleSelective(pmsBrand, pmsBrandExample);
    }
}
