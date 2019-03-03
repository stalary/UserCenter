
package com.stalary.usercenter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @model Address
 * @description 城市信息
 * @field address 城市名称
 * @field count 登陆次数
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String address;

    private Long count;
}