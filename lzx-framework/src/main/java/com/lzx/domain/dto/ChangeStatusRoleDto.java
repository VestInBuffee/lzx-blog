package com.lzx.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRoleDto {
    //角色ID
    @JSONField(name = "roleId")
    private Long id;
    //角色状态（0正常 1停用）
    private String status;
}
