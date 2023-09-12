package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddTagDto;
import com.lzx.domain.entity.Tag;
import com.lzx.service.TagService;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult tagList(Integer pageNum, Integer pageSize,
                                  String name, String remark){
        return tagService.tagList(pageNum, pageSize, name, remark);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        Tag addTag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.addTag(addTag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }

}
