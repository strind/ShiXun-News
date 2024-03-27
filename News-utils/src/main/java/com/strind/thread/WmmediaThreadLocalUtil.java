package com.strind.thread;

import com.strind.model.wemedia.pojos.WmUser;

/**
 * @author strind
 * @version 1.0
 * @description wemedia threadlocal
 * @date 2024/3/21 20:45
 */
public class WmmediaThreadLocalUtil {

    private static final ThreadLocal<WmUser> WM_MEDIA_USER_THREAD_LOCAL = new ThreadLocal<>();

    // 存入线程中
    public static void setUser(WmUser appUser){
        WM_MEDIA_USER_THREAD_LOCAL.set(appUser);
    }
    // 从线程中取出
    public static WmUser getUser(){
        return WM_MEDIA_USER_THREAD_LOCAL.get();
    }

    // 清理
    public static void clear(){
        WM_MEDIA_USER_THREAD_LOCAL.remove();
    }

}
