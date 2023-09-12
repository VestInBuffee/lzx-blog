package com.lzx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.lzx.domain.entity.Role;
import com.lzx.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateVo {
    @JSONField(name = "roleIds")
    private List<Long> userRole;
    private List<Role> roles;
    private User user;
}
