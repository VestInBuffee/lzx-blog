package com.lzx.service;

import com.lzx.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    public boolean hasPermission(String perm){
        if(SecurityUtils.isAdmin()){
            return true;
        }
        List<String> perms = SecurityUtils.getLoginUser().getPerms();
        return perms.contains(perm);
    }
}
