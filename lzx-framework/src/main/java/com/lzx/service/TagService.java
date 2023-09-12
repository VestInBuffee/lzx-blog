package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.Tag;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-08-21 16:28:41
 */
public interface TagService extends IService<Tag> {

//    ResponseResult tagList();

    ResponseResult tagList(Integer pageNum, Integer pageSize, String name, String remark);

    ResponseResult addTag(Tag addTag);

    ResponseResult deleteTag(Long id);

    ResponseResult getTagById(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult listAllTag();
}

