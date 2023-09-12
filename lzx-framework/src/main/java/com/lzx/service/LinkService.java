package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AdminAddLinkDto;
import com.lzx.domain.dto.AdminListLinkDto;
import com.lzx.domain.dto.AdminUpdateLinkDto;
import com.lzx.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-08-10 15:52:01
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLink(Integer pageNum, Integer pageSize, AdminListLinkDto adminListLinkDto);

    ResponseResult addLink(AdminAddLinkDto adminAddLinkDto);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(AdminUpdateLinkDto adminUpdateLinkDto);

    ResponseResult deleteLinkById(Long id);
}

