package com.hd.notification.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hd.notification.BaseAppHelper;
import com.hd.notification.R;
import com.hd.notification.RecorderService;
import com.hd.notification.RecorderServiceConnection;
import com.hd.notification.bean.EnRecordStatus;
import com.hd.notification.bean.NotifyRecorderItem;
import com.hd.notification.broadcast.NotificationStatusBarReceiver;
import com.hd.notification.broadcast.NotificationUtils;
import com.hd.notification.util.LOG;


/**
 * @Description:录音实时转写
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/17
 */
public class RecorderAndTranslatingActivity extends FragmentActivity {
    private static final String TAG = "RecorderAndTranslatingActivity";
    @Nullable
    private RecorderServiceConnection mRecorderServiceConnection = null;
    private final NotificationStatusBarReceiver notificationStatusBarReceiver = new NotificationStatusBarReceiver();

    private volatile int curTime = 0;
    private final byte[] lock = new byte[0];

    @NonNull
    private final NotifyRecorderItem mNotifyRecorderItem = new NotifyRecorderItem();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private TextView tvSetPlayStatus;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        notificationStatusBarReceiver.onNewIntent(intent);
        LOG.d(TAG, "onNewIntent extra: " + intent.getStringExtra(NotificationStatusBarReceiver.EXTRA));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.d(TAG, "onCreate extra: " + getIntent().getStringExtra(NotificationStatusBarReceiver.EXTRA));
        setContentView(R.layout.activity_recorder_and_translating);

        startService();
        findViewById(R.id.bind_service).setOnClickListener(v -> {
            notificationStatusBarReceiver.register(this);
            bindService();
            NotificationUtils.get().setClazz(RecorderAndTranslatingActivity.class);
        });

        tvSetPlayStatus = findViewById(R.id.setPlayStatus);
        tvSetPlayStatus.setOnClickListener(v -> {
            RecorderService recorderService = BaseAppHelper.get().getPlayService();
            if (recorderService != null) {
                if (recorderService.isRecording()) {
                    mNotifyRecorderItem.setStatus(EnRecordStatus.PAUSE);
                } else {
                    mNotifyRecorderItem.setStatus(EnRecordStatus.RECORDING);
                }
                recorderService.setItem(mNotifyRecorderItem);
                if (mNotifyRecorderItem.isRecording()) {
                    NotificationUtils.get().showStart(mNotifyRecorderItem);
                    tvSetPlayStatus.setText("录制中");
                } else {
                    NotificationUtils.get().showPause(mNotifyRecorderItem);
                    tvSetPlayStatus.setText("暂停");
                }
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            notificationStatusBarReceiver.unregister(this);
            NotificationUtils.get().cancelAll();
        });
        final Button button = findViewById(R.id.btn_add);
        button.setOnClickListener(v -> {
            button.setText((Integer.parseInt(button.getText().toString()) + 1) + "");
        });
        LOG.d(TAG, "android.os.Process.myPid(): " + android.os.Process.myPid());

        final Button btnShow = findViewById(R.id.btn_show);
        btnShow.setOnClickListener(v -> {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(500);
                        synchronized (lock) {
                            curTime++;
                            mNotifyRecorderItem.setCurrentMs(curTime * 1000);
                            mHandler.post(() -> tvSetPlayStatus.callOnClick());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });

        notificationStatusBarReceiver.liveExtra.observe(this, extra -> {
            LOG.d(TAG, "liveExtra: " + extra);
            if (NotifyAction.TYPE_RECORD.equals(extra)) {
                tvSetPlayStatus.callOnClick();
            } else if (NotifyAction.TYPE_SAVE.equals(extra)) {
            } else if (NotifyAction.TYPE_ADD_TAG.equals(extra)) {
            } else if (NotifyAction.TYPE_APP.equals(extra)) {
            } else if (NotifyAction.TYPE_EXIT.equals(extra)) {
            }
        });
    }


    /**
     * 开启服务
     */
    private void startService() {
        final Intent intent = new Intent(this, RecorderService.class);
        startService(intent);
    }


    /**
     * 绑定服务
     */
    private void bindService() {
        final Intent intent = new Intent();
        intent.setClass(this, RecorderService.class);
        mRecorderServiceConnection = new RecorderServiceConnection();
        bindService(intent, mRecorderServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 服务解绑
     */
    private void unbindService() {
        if (mRecorderServiceConnection != null) {
            unbindService(mRecorderServiceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }

}
