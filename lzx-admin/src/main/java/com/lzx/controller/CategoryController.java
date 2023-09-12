package com.lzx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ListCategoryDto;
import com.lzx.domain.dto.UpdateCategoryDto;
import com.lzx.domain.entity.Category;
import com.lzx.domain.vo.ExcelCategoryVo;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.service.CategoryService;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ServletContext servletContext;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response)  {
        try {
            //设置头
            WebUtils.setDownLoadHeader("分类.xlsx", servletContext, response);
            //获取需要导出的数据
            List<Category> categoryList = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.
                    copyBeanList(categoryList, ExcelCategoryVo.class);
            //把数据写入到excel
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).
                    autoCloseStream(Boolean.FALSE).sheet("1").
                    doWrite(excelCategoryVos);
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("list")
    public ResponseResult listCategory(Integer pageNum, Integer pageSize,
                                       ListCategoryDto listCategoryDto){
        return categoryService.listCategory(pageNum, pageSize, listCategoryDto);
    }
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Long id){
        return categoryService.getCategoryById(id);
    }
    @PutMapping
    public ResponseResult updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto){
        return categoryService.updateCategory(updateCategoryDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable("id") Long id){
        return categoryService.deleteCategoryById(id);
    }
}
