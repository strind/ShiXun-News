package com.strind.wemedia.task;

import com.strind.common.SensitiveWordUtil;
import org.apache.commons.collections.list.LazyList;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author strind
 * @version 1.0
 * @description 执行审核的线程池
 * @date 2024/3/24 18:36
 */
public class ExecuteTaskPool {

    private static Executor executor = null;

    static {
        executor = new ThreadPoolExecutor(
            8,
            16,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(30)
        );
    }

    public void initSensitiveUtil(List<String> list){
        synchronized (ExecuteTaskPool.class){
            if (list != null && !list.isEmpty()){
                SensitiveWordUtil.initMap(list);
            }
        }
    }


    public static void addTask(Runnable task){
        executor.execute(task);
    }

}
