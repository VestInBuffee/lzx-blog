package com.lzx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.lzx.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuTreeVo {
    //菜单ID
    private Long id;
    //父菜单ID
    private Long parentId;
    private List<MenuTreeVo> children;
    @JSONField(name = "label")
    private String menuName;
}
