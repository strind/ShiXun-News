package com.strind.thread;

import com.strind.model.user.pojos.AppUser;

/**
 * @author strind
 * @version 1.0
 * @description app threadlocal
 * @date 2024/3/21 20:45
 */
public class AppThreadLocalUtil {

    private static final ThreadLocal<AppUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    // 存入线程中
    public static void setUser(AppUser appUser){
        WM_USER_THREAD_LOCAL.set(appUser);
    }
    // 从线程中取出
    public static AppUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    // 清理
    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }

}
