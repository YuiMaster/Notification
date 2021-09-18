package com.hd.notification.bean;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description: 录音状态
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
@IntDef(value = {EnRecordStatus.INIT, EnRecordStatus.RECORDING, EnRecordStatus.PAUSE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnRecordStatus {
    int INIT = 0;
    int RECORDING = 1;
    int PAUSE = 2;
}
