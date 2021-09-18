package com.hd.notification.util;


/**
 * @Description:
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class MusicPlayAction {
    /**
     * 录制，暂停，继续
     */
    public static final String TYPE_RECORD = "TYPE_RECORD";
    /**
     * 保存
     */
    public static final String TYPE_SAVE = "TYPE_SAVE";
    /**
     * 打标
     */
    public static final String TYPE_ADD_TAG = "TYPE_ADD_TAG";
    /**
     * 软件
     */
    public static final String TYPE_APP = "TYPE_APP";
    /**
     * 退出
     */
    public static final String TYPE_EXIT = "TYPE_EXIT";

    /**
     * 默认状态
     */
    public static final int STATE_IDLE = 100;
    /**
     * 正在准备中
     */
    public static final int STATE_PREPARING = 101;
    /**
     * 正在播放中
     */
    public static final int STATE_PLAYING = 102;
    /**
     * 暂停状态
     */
    public static final int STATE_PAUSE = 103;

}
