package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-10 16:47:33
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    void saveUserRole(@Param("id")Long id, @Param("roleIds")List<Long> roleIds);

    List<Long> getUserRole(Long id);

    void deleteUserRole(Long id);
}

