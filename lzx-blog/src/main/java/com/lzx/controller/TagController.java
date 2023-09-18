package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/tagList")
    public ResponseResult tagList(){
        return tagService.listAllTag();
    }
}
