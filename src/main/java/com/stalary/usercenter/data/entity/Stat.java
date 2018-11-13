package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.reflect.TypeToken;
import com.stalary.usercenter.data.dto.Address;
import com.stalary.usercenter.factory.BeansFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @model Stat
 * @description 统计对象
 * @field loginCount 登陆次数
 * @field cityList 登陆城市列表
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "statistics")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class Stat extends BaseEntity {

    /**
     * 关联的用户id
     */
    @JsonIgnore
    private Long userId;

    /**
     * 登陆次数
     */
    private Long loginCount;

    /**
     * 登陆的城市
     */
    @JsonIgnore
    private String city;

    @Transient
    private List<Address> cityList;

    /**
     * 最近一次登陆的时间
     */
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lateLoginTime;

    public List<Address> getCityList() {
        if (StringUtils.isBlank(this.city)) {
            return new ArrayList<>();
        }
        this.cityList = BeansFactory.getGson().fromJson(city, new TypeToken<List<Address>>(){}.getType());
        return cityList;
    }

    public void setCityList(List<Address> addressList) {
        addressList.sort(Comparator.comparing(Address::getCount).reversed());
        this.city = BeansFactory.getGson().toJson(addressList);
    }

}
