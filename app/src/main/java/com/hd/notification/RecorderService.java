package com.hd.notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hd.notification.bean.NotifyRecorderItem;
import com.hd.notification.broadcast.NotificationUtils;


/**
 * @Description:录音服务
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class RecorderService extends Service {

    @NonNull
    private NotifyRecorderItem mNotifyRecorderItem = new NotifyRecorderItem();

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationUtils.get().init(this);
    }

    /**
     * 比如，广播，耳机声控，通知栏广播，来电或者拔下耳机广播开启服务
     *
     * @param context 上下文
     * @param type    类型
     */
    public static void startCommand(Context context, String type) {
        Intent intent = new Intent(context, RecorderService.class);
        intent.setAction(type);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public void setItem(@NonNull NotifyRecorderItem item) {
        this.mNotifyRecorderItem = item;
    }

    public boolean isRecording() {
        return mNotifyRecorderItem.isRecording();
    }


    public class PlayBinder extends Binder {
        public RecorderService getService() {
            return RecorderService.this;
        }
    }

}
