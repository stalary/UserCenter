package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stalary.usercenter.factory.BeansFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class Stat extends BaseEntity {

    /**
     * 关联的用户id
     */
    private Long userId;

    /**
     * 登陆次数
     */
    private Long loginCount;

    /**
     * 登陆的城市
     */
    private String city;

    @Transient
    @JsonIgnore
    private Map<String, Integer> cityMap;

    /**
     * 最近一次登陆的时间
     */
    @UpdateTimestamp
    private Date lateLoginTime;

    @Transient
    @JsonIgnore
    public Map<String, Integer> sort(Map<String, Integer> cityMap) {
        ValueComparator valueComparator = new ValueComparator(cityMap);
        Map<String, Integer> sortMap = new TreeMap<>(valueComparator);
        sortMap.putAll(cityMap);
        return sortMap;
    }

    public void serializeFields() {
        this.city = BeansFactory.getGson().toJson(cityMap);
    }

    public void deserializeFields() {
        this.cityMap = BeansFactory.getGson().fromJson(city, new TypeToken<Map<String, Integer>>(){}.getType());
    }


}

class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }


    @Override
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}

