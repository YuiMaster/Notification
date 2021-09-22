package com.hd.notification.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.hd.notification.BaseAppHelper;
import com.hd.notification.util.LOG;


/**
 * @Description:
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class NotificationStatusBarReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationStatusBarReceiver";

    public static final String ACTION_STATUS_BAR = "com.hd.notification.status_bar";
    public static final String EXTRA = "extra";

    @NonNull
    public MutableLiveData<String> liveExtra = new MutableLiveData<>();

    /**
     * 避免重复注册
     */
    private boolean isRegister = false;

    /**
     * 注册
     *
     * @param activity 活动
     */
    public void register(Activity activity) {
        if (isRegister) {
            return;
        }
        isRegister = true;
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATUS_BAR);
        activity.registerReceiver(this, intentFilter);
    }

    /**
     * 取消
     *
     * @param activity 活动
     */
    public void unregister(Activity activity) {
        if (isRegister) {
            isRegister = false;
            activity.unregisterReceiver(this);
        }
    }

    /**
     * notification有部分 Activity接收
     *
     * @param intent
     */
    public void onNewIntent(Intent intent) {
        if (intent != null && intent.getAction().equals(ACTION_STATUS_BAR)) {
            String action = intent.getStringExtra(NotificationStatusBarReceiver.EXTRA);
            LOG.d(TAG, "onNewIntent extra: " + action);
            liveExtra.postValue(action);
        }
    }


    /**
     * 接收
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }

        final String extra = intent.getStringExtra(EXTRA);
        liveExtra.postValue(extra);
        if (TextUtils.equals(extra, NotifyAction.TYPE_APP)) {
            LOG.d(TAG, "onReceive  TYPE_APP");
        } else if (TextUtils.equals(extra, NotifyAction.TYPE_RECORD)) {
            LOG.d(TAG, "onReceive  TYPE_RECORD");
            if (BaseAppHelper.get().getPlayService() != null) {
                boolean playing = BaseAppHelper.get().getPlayService().isRecording();
                if (playing) {
                    LOG.d(TAG, "onReceive  暂停");
                } else {
                    LOG.d(TAG, "onReceive  播放");
                }
            }
        } else if (TextUtils.equals(extra, NotifyAction.TYPE_ADD_TAG)) {
            LOG.d(TAG, "onReceive  TYPE_ADD_TAG");
        } else if (TextUtils.equals(extra, NotifyAction.TYPE_EXIT)) {
            LOG.d(TAG, "onReceive  TYPE_EXIT");
        } else if (TextUtils.equals(extra, NotifyAction.TYPE_SAVE)) {
            LOG.d(TAG, "onReceive  TYPE_SAVE");
        }
    }
}
