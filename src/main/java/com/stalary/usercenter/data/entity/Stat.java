package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
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

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.*;

/**
 * UserStat
 *
 * @author lirongqian
 * @since 2018/03/25
 */
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
