
package com.stalary.usercenter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Address
 *
 * @author lirongqian
 * @since 2018/03/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String address;

    private Integer count;
}