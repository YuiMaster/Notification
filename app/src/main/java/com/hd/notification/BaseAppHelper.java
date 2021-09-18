package com.hd.notification;


import androidx.annotation.Nullable;

/**
 * @Description: app辅助类
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class BaseAppHelper {
    @Nullable
    private RecorderService mRecorderService = null;

    private BaseAppHelper() {
    }

    private static class SingletonHolder {
        private final static BaseAppHelper INSTANCE = new BaseAppHelper();
    }

    public static BaseAppHelper get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取PlayService对象
     *
     * @return 返回PlayService对象
     */
    @Nullable
    public RecorderService getPlayService() {
        return mRecorderService;
    }

    /**
     * 设置PlayService服务
     */
    public void setPlayService(@Nullable RecorderService service) {
        mRecorderService = service;
    }
}
