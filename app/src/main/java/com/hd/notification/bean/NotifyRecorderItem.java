package com.hd.notification.bean;

import java.io.Serializable;


/**
 * @Description: 录音 通知栏 数据
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class NotifyRecorderItem implements Serializable {

    /**
     * 状态
     */
    @EnRecordStatus
    private int status = EnRecordStatus.INIT;

    /**
     * 当前进度
     */
    private long currentMs;

    private final String hashCode = System.currentTimeMillis() + "";

    @EnRecordStatus
    public int getStatus() {
        return status;
    }

    public void setStatus(@EnRecordStatus int status) {
        this.status = status;
    }

    public long getCurrentMs() {
        return currentMs;
    }

    public void setCurrentMs(long currentMs) {
        this.currentMs = currentMs;
    }

    public boolean isRecording() {
        return status == EnRecordStatus.RECORDING;
    }

    /**
     * 思考为什么要重写这两个方法
     * 对比本地歌曲是否相同
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NotifyRecorderItem) {
            NotifyRecorderItem bean = (NotifyRecorderItem) obj;
            return this.hashCode() == bean.hashCode();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return hashCode.hashCode();
    }


}
