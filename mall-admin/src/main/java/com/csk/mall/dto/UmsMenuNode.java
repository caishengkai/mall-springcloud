package com.csk.mall.dto;

import com.csk.mall.model.UmsMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 后台菜单节点封装
 * @author: caishengkai
 * @time: 2020/4/29 9:56
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    private List<UmsMenuNode> children;
}
