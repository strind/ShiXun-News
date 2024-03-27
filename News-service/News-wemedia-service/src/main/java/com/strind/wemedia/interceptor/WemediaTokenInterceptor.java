package com.strind.wemedia.interceptor;

import com.strind.model.wemedia.pojos.WmUser;
import com.strind.thread.WmmediaThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author strind
 * @version 1.0
 * @description 拦截器，为threadlocal赋值
 * @date 2024/3/23 11:04
 */
@Slf4j
public class WemediaTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        String userId = request.getHeader("id");
        if (StringUtils.isNotBlank(userId)){
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmmediaThreadLocalUtil.setUser(wmUser);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        WmmediaThreadLocalUtil.clear();
    }
}
