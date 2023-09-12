package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddUserDto;
import com.lzx.domain.dto.ListUserDto;
import com.lzx.domain.dto.UpdateUserDto;
import com.lzx.domain.entity.LoginUser;
import com.lzx.domain.entity.Role;
import com.lzx.domain.entity.User;
import com.lzx.domain.vo.PageVo;
import com.lzx.domain.vo.UserListVo;
import com.lzx.domain.vo.UserUpdateVo;
import com.lzx.domain.vo.UserVo;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.handler.exception.SystemException;
import com.lzx.mapper.UserMapper;
import com.lzx.service.RoleService;
import com.lzx.service.UserService;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-08-13 17:12:25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //从context获取user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) (authentication.getPrincipal());
        User user = loginUser.getUser();

        //将user序列化为userVo
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);

        //返回
        return ResponseResult.okResult(userVo);
    }

    @Override
    public ResponseResult putUserInfo(User user) {
        //1.只更新3列
        //1.1获取更新列的值
        String avatar = user.getAvatar();
        String nickName = user.getNickName();
        String sex = user.getSex();
        Long id = user.getId();
        //1.2设置wrapper
        LambdaUpdateWrapper<User> userLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        userLambdaQueryWrapper.set(User::getAvatar, avatar);
        userLambdaQueryWrapper.set(User::getNickName, nickName);
        userLambdaQueryWrapper.set(User::getSex, sex);
        userLambdaQueryWrapper.eq(User::getId, id);

        //更新数据库
        update(null, userLambdaQueryWrapper);

        //3.更新redis
        //3.1获取user
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + id);
        User userUpdate = loginUser.getUser();
        //3.2设置更新后user的值
        userUpdate.setAvatar(avatar);
        userUpdate.setNickName(nickName);
        userUpdate.setSex(sex);
        //3.3存储到redis中
        redisCache.setCacheObject("bloglogin:" + id, loginUser);

        //返回
        return ResponseResult.okResult("更新成功");
    }

    @Override
    public ResponseResult register(User user) {
        //数据是否为空
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        //数据是否存在
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, ListUserDto listUserDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(listUserDto.getUserName()),
                User::getUserName, listUserDto.getUserName());
        wrapper.like(StringUtils.hasText(listUserDto.getPhonenumber()),
                User::getPhonenumber, listUserDto.getPhonenumber());
        wrapper.eq(StringUtils.hasText(listUserDto.getStatus()),
                User::getStatus, listUserDto.getStatus());

        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        List<User> userList = page.getRecords();
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(userList, UserListVo.class);
        return ResponseResult.okResult(new PageVo(userListVos, page.getTotal()));
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        //判断userName为空
        if(!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //判断userName存在
        if(userNameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //判断phonenum
        if(phoneNumExist(addUserDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //判断email
        if(emailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //加密
        String encodePassword = passwordEncoder.encode(addUserDto.getPassword());
        addUserDto.setPassword(encodePassword);
        //存入user表
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);

        //存入user_role表
        getBaseMapper().saveUserRole(user.getId(), addUserDto.getRoleIds());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(Long id) {
        User user = getById(id);
        List<Role> roles = roleService.getAllRole();
        List<Long> userRole = getBaseMapper().getUserRole(id);
        return ResponseResult.okResult(new UserUpdateVo(userRole, roles, user));
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        updateById(user);

        getBaseMapper().deleteUserRole(user.getId());
        getBaseMapper().saveUserRole(user.getId(), updateUserDto.getRoleIds());
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper)>0;
    }
    private boolean phoneNumExist(String phoneNum){
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phoneNum);
        return count(queryWrapper)>0;
    }
}

