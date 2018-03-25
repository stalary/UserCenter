package com.stalary.usercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
/**
 * @author Stalary
 * @description
 * @date 2018/3/25
 */
@NoRepositoryBean
public interface BaseRepo<T, Long extends Serializable> extends JpaRepository<T, Long> {

}
