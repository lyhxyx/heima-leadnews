package com.heima.user.filter;

import com.heima.model.user.pojos.ApUser;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(filterName = "appTokenFilter",urlPatterns = "/*")
@Log4j2
//@Component  // 扫描包
public class AppTokenFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            //得到header中的信息
            String userId = request.getHeader("userId");
            if(userId != null){
                ApUser apUser = new ApUser();
                apUser.setId(Integer.valueOf(userId));
                AppThreadLocalUtils.setUser(apUser);
            }
      
        // 如果没有则直接放行
        filterChain.doFilter(request,response);
    }
}