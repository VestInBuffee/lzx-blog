package com.lzx.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限表(Menu)表实体类
 *
 * @author makejava
 * @since 2023-08-22 16:27:36
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_menu")//需要补充tablename和tableId
public class Menu {
    @TableId
    //菜单ID
    private Long id;
    //菜单名称
    private String menuName;
    //父菜单ID
    private Long parentId;
    //显示顺序
    private Integer orderNum;
    //路由地址
    private String path;
    //组件路径
    private String component;
    //是否为外链（0是 1否）
    private Integer isFrame;
    //菜单类型（M目录 C菜单 F按钮）
    private String menuType;
    //菜单状态（0显示 1隐藏）
    private String visible;
    //菜单状态（0正常 1停用）
    private String status;
    //权限标识
    private String perms;
    //菜单图标
    private String icon;
    @TableField(fill = FieldFill.INSERT)
    //创建者
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    //创建时间
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    //更新者
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    //更新时间
    private Date updateTime;
    //备注
    private String remark;
    
    private String delFlag;

    @TableField(exist = false)
    private List<Menu> children;
}

