package com.lzx.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCategoryVo {
    @ExcelProperty("id")
    private Long id;
    @ExcelProperty("名字")
    private String name;
    //状态0:正常,1禁用
    @ExcelProperty("状态")
    private String status;
}
