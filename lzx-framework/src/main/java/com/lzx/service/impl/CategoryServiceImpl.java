package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ListCategoryDto;
import com.lzx.domain.dto.UpdateCategoryDto;
import com.lzx.domain.entity.Article;
import com.lzx.domain.entity.Category;
import com.lzx.domain.vo.*;
import com.lzx.mapper.CategoryMapper;
import com.lzx.service.ArticleService;
import com.lzx.service.CategoryService;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-08-08 17:07:57
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //1.查找到article表中已发布的文章,获取到类别id，并去重
        //1.1查找到article表中已发布的文章
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(lambdaQueryWrapper);
        //1.2获取到类别id，并去重
        Set<Long> categoryIdSet = articleList.stream()
                //1.2.2获取到类别id
                .map(article -> article.getCategoryId())
                //1.2.2去重
                .collect(Collectors.toSet());


        //2.用已发布的文章类别id，去查询category表，查询到的category的status必须为启用
        //2.1用已发布的文章类别id，去查询category表
        List<Category> categoryList = listByIds(categoryIdSet);
        //2.2查询到的category的status必须为启用
        categoryList.stream()
                .filter(category ->
                        SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //3.
        List<CategoryVo> categoryVos =
                BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categoryList = list(wrapper);
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(categoryList,
                CategoryListVo.class);
        return ResponseResult.okResult(categoryListVos);
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(listCategoryDto.getName()),
                Category::getName, listCategoryDto.getName());
        wrapper.eq(StringUtils.hasText(listCategoryDto.getStatus()),
                Category::getStatus, listCategoryDto.getStatus());

        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        List<Category> categoryList = page.getRecords();
        List<AdminCategoryListVo> adminCategoryListVos =
                BeanCopyUtils.copyBeanList(categoryList, AdminCategoryListVo.class);
        return ResponseResult.okResult(new PageVo(adminCategoryListVos, page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        AdminCategoryVo adminCategoryVo = BeanCopyUtils.copyBean(category, AdminCategoryVo.class);
        return ResponseResult.okResult(adminCategoryVo);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category = BeanCopyUtils.copyBean(updateCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategoryById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}

