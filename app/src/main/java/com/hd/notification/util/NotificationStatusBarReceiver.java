package com.hd.notification.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.hd.notification.BaseAppHelper;


/**
 * @Description:
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class NotificationStatusBarReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationStatusBarReceiver";

    public static final String ACTION_STATUS_BAR = "com.hd.notification.status_bar";
    public static final String EXTRA = "extra";

    private boolean isRegister = false;

    public void register(Activity activity) {
        if (isRegister) {
            return;
        }
        isRegister = true;
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATUS_BAR);
        activity.registerReceiver(this, intentFilter);
    }

    public void unregister(Activity activity) {
        if (isRegister) {
            isRegister = false;
            activity.unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }

        final String extra = intent.getStringExtra(EXTRA);
        if (TextUtils.equals(extra, MusicPlayAction.TYPE_APP)) {
            LOG.d(TAG, "onReceive  TYPE_APP");
        } else if (TextUtils.equals(extra, MusicPlayAction.TYPE_RECORD)) {
            LOG.d(TAG, "onReceive  TYPE_RECORD");
            if (BaseAppHelper.get().getPlayService() != null) {
                boolean playing = BaseAppHelper.get().getPlayService().isPlaying();
                if (playing) {
                    LOG.d(TAG, "onReceive  暂停");
                } else {
                    LOG.d(TAG, "onReceive  播放");
                }
            }
        } else if (TextUtils.equals(extra, MusicPlayAction.TYPE_ADD_TAG)) {
            LOG.d(TAG, "onReceive  TYPE_ADD_TAG");
        } else if (TextUtils.equals(extra, MusicPlayAction.TYPE_EXIT)) {
            LOG.d(TAG, "onReceive  TYPE_EXIT");
        } else if (TextUtils.equals(extra, MusicPlayAction.TYPE_SAVE)) {
            LOG.d(TAG, "onReceive  TYPE_SAVE");
        }
    }
}
