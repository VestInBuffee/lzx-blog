package com.lzx.domain.vo;

import com.lzx.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutersVo {
    List<Menu> menus;
}
