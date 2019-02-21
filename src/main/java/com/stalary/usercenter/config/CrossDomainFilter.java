package com.stalary.usercenter.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CrossDomainFilter
 *
 * @author lirongqian
 * @since 2018/11/12
 */
@Slf4j
@WebFilter(filterName = "crossDomainFilter", urlPatterns = "/*")
public class CrossDomainFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestOrigin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", requestOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String contentTypes = request.getHeader("Access-Control-Request-Headers");
        if (StringUtils.isNotEmpty(contentTypes)) {
            response.setHeader("Access-Control-Allow-Headers", contentTypes);
        }
        filterChain.doFilter(request, response);
    }

}