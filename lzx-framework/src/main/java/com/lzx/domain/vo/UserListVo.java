package com.lzx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListVo {
    //主键
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //手机号
    private String phonenumber;
    //账号状态（0正常 1停用）
    private String status;
    //创建时间
    private Date createTime;
}
