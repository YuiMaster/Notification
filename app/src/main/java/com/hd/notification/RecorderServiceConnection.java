package com.hd.notification;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hd.notification.util.LOG;


/**
 * @Description:
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/17
 */
public class RecorderServiceConnection implements ServiceConnection {
    private static final String TAG = "PlayServiceConnection";

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LOG.d(TAG, "onServiceConnected ");
        final RecorderService recorderService = ((RecorderService.PlayBinder) service).getService();
        BaseAppHelper.get().setPlayService(recorderService);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        LOG.d(TAG, "onServiceDisconnected ");
        BaseAppHelper.get().setPlayService(null);
    }
}
