package com.lzx.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.TagArticleDto;
import com.lzx.domain.entity.Tag;

import com.lzx.domain.vo.PageVo;
import com.lzx.domain.vo.TagListVo;
import com.lzx.domain.vo.TagVo;
import com.lzx.mapper.TagMapper;
import com.lzx.service.TagService;

import com.lzx.utils.BeanCopyUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-08-21 16:28:41
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    /**
     *
     * @param pageNum
     * @param pageSize
     * @param name 标签名
     * @param remark 备注
     * @return
     */
    @Override
    public ResponseResult tagList(Integer pageNum, Integer pageSize,
                                  String name, String remark) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), Tag::getName, name);
        wrapper.like(StringUtils.hasText(remark), Tag::getRemark, remark);

        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        List<Tag> tagList = page.getRecords();

        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return ResponseResult.okResult(new PageVo(tagVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addTag(Tag addTag) {
        save(addTag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tagList = list();
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(tagList, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }

//    @Override
//    public List<Long> getTagIdByArticleId(Long id) {
//        List<Long> tagIds =
//                getBaseMapper().getTagIdByArticleId(id);
//        return tagIds;
//    }

//    @Override
//    public List<TagArticleDto> getTagListByArticleId(Long id) {
//        List<Long> tagIds = getTagIdByArticleId(id);
//        List<TagArticleDto> tagArticleDtos = null;
//        if(0 != tagIds.size()){
//            List<Tag> tagList = listByIds(tagIds);
//            tagArticleDtos = BeanCopyUtils.copyBeanList(tagList,
//                    TagArticleDto.class);
//        }
//        return tagArticleDtos;
//    }
}

