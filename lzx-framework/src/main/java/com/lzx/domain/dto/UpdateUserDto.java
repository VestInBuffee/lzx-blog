package com.lzx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    //昵称
    private String nickName;
    //邮箱
    private String email;
    //用户性别（0男，1女，2未知）
    private String sex;
    //主键
    private Long id;
    //用户名
    private String userName;
    //账号状态（0正常 1停用）
    private String status;
    private List<Long> roleIds;
}
