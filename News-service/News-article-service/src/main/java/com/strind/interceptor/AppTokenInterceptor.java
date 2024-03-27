package com.strind.interceptor;

import com.strind.model.user.pojos.AppUser;
import com.strind.model.wemedia.pojos.WmUser;
import com.strind.thread.AppThreadLocalUtil;
import com.strind.thread.WmmediaThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author strind
 * @version 1.0
 * @description app 端用户拦截器
 * @date 2024/3/21 20:43
 */
public class AppTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        String userId = request.getHeader("userId");
        if (null != userId){
            // 存入当前线程中
            AppUser appUser = new AppUser();
            appUser.setId(Integer.valueOf(userId));
            AppThreadLocalUtil.setUser(appUser);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        AppThreadLocalUtil.clear();
    }
}
