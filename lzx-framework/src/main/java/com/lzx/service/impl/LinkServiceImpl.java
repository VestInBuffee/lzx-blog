package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AdminAddLinkDto;
import com.lzx.domain.dto.AdminListLinkDto;
import com.lzx.domain.dto.AdminUpdateLinkDto;
import com.lzx.domain.entity.Link;
import com.lzx.domain.vo.AdminLinkListVo;
import com.lzx.domain.vo.AdminLinkVo;
import com.lzx.domain.vo.LinkVo;
import com.lzx.domain.vo.PageVo;
import com.lzx.mapper.LinkMapper;
import com.lzx.service.LinkService;
import com.lzx.utils.BeanCopyUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-08-10 15:52:02
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //设置wrapper
        LambdaQueryWrapper<Link> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        //从数据库读取link
        List<Link> links = list(lambdaQueryWrapper);
        //封装为指定vo
        List<LinkVo> linkVo = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //返回
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult listLink(Integer pageNum, Integer pageSize, AdminListLinkDto adminListLinkDto) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(adminListLinkDto.getName()),
                Link::getName, adminListLinkDto.getName());
        wrapper.eq(StringUtils.hasText(adminListLinkDto.getStatus()),
                Link::getStatus, adminListLinkDto.getStatus());

        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        List<Link> links = page.getRecords();
        List<AdminLinkListVo> adminLinkListVos = BeanCopyUtils.copyBeanList(links, AdminLinkListVo.class);
        return ResponseResult.okResult(new PageVo(adminLinkListVos, page.getTotal()));
    }

    @Override
    public ResponseResult addLink(AdminAddLinkDto adminAddLinkDto) {
        Link link = BeanCopyUtils.copyBean(adminAddLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = getById(id);
        AdminLinkVo adminLinkVo = BeanCopyUtils.copyBean(link, AdminLinkVo.class);
        return ResponseResult.okResult(adminLinkVo);
    }

    @Override
    public ResponseResult updateLink(AdminUpdateLinkDto adminUpdateLinkDto) {
        Link link = BeanCopyUtils.copyBean(adminUpdateLinkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLinkById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

}

