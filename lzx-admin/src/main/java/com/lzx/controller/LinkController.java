package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AdminAddLinkDto;
import com.lzx.domain.dto.AdminListLinkDto;
import com.lzx.domain.dto.AdminUpdateLinkDto;
import com.lzx.domain.entity.Link;
import com.lzx.domain.vo.AdminLinkVo;
import com.lzx.service.LinkService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult listLink(Integer pageNum, Integer pageSize,
                                   AdminListLinkDto adminListLinkDto){
        return linkService.listLink(pageNum, pageSize, adminListLinkDto);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody AdminAddLinkDto adminAddLinkDto){
        return linkService.addLink(adminAddLinkDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id") Long id){
        return linkService.getLinkById(id);
    }
    @PutMapping
    public ResponseResult updateLink(@RequestBody AdminUpdateLinkDto adminUpdateLinkDto){
        return linkService.updateLink(adminUpdateLinkDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkById(@PathVariable("id") Long id){
        return linkService.deleteLinkById(id);
    }
}
