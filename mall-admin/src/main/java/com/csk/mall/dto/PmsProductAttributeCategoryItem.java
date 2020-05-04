package com.csk.mall.dto;

import com.csk.mall.model.PmsProductAttribute;
import com.csk.mall.model.PmsProductAttributeCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description:
 * @author: caishengkai
 * @time: 2020/5/3 14:17
 */
@Getter
@Setter
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {
    private List<PmsProductAttribute> productAttributeList;
}
