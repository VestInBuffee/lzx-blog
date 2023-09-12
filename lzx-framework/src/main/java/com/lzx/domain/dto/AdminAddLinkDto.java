package com.lzx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddLinkDto {
    private String description;
    //网站地址
    private String address;
    private String name;

    private String logo;
    //审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    private String status;
}
