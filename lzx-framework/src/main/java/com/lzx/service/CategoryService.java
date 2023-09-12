package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ListCategoryDto;
import com.lzx.domain.dto.UpdateCategoryDto;
import com.lzx.domain.entity.Category;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-08-08 17:07:57
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listCategory(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto);

    ResponseResult addCategory(Category category);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto);

    ResponseResult deleteCategoryById(Long id);
}

