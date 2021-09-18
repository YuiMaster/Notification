package com.hd.notification.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hd.notification.R;
import com.hd.notification.RecorderService;
import com.hd.notification.bean.NotifyRecorderItem;
import com.hd.notification.ui.RecorderAndTranslatingActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.hd.notification.util.MusicPlayAction.TYPE_APP;


public class NotificationUtils {
    private static final String TAG = "NotificationUtils";

    @Nullable
    private RecorderService recorderService;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0x111;

    private Class<?> clazz = RecorderAndTranslatingActivity.class;

    public static NotificationUtils get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private final static NotificationUtils instance = new NotificationUtils();
    }

    private NotificationUtils() {

    }

    /**
     * 1.创建一个NotificationManager的引用
     *
     * @param recorderService PlayService对象
     */
    public void init(@NonNull RecorderService recorderService) {
        LOG.d(TAG, "init");
        this.recorderService = recorderService;
        notificationManager = (NotificationManager) recorderService.getSystemService(NOTIFICATION_SERVICE);
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * 开始播放
     *
     * @param music music
     */
    public void showPlay(NotifyRecorderItem music) {
        LOG.d(TAG, "showPlay");
        if (music == null) {
            return;
        }

        //这个方法是启动Notification到前台
        if (recorderService != null) {
            recorderService.startForeground(NOTIFICATION_ID, buildNotification(recorderService, music, true, clazz));
        }
    }


    /**
     * 暂停
     *
     * @param music music
     */
    public void showPause(NotifyRecorderItem music) {
        LOG.d(TAG, "showPause");
        if (music == null) {
            return;
        }

        //这个方法是停止Notification
        if (recorderService != null) {
            recorderService.stopForeground(false);
            notificationManager.notify(NOTIFICATION_ID, buildNotification(recorderService, music, false, clazz));
        }
    }

    /**
     * 结束所有的
     */
    public void cancelAll() {
        LOG.d(TAG, "cancelAll");
        if (recorderService != null) {
            recorderService.stopForeground(false);
        }
        notificationManager.cancelAll();
    }

    private Notification buildNotification(Context context, NotifyRecorderItem item, boolean isPlaying, Class<?> clazz) {
        LOG.d(TAG, "buildNotification");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mRemoteViews == null) {
            mRemoteViews = getCustomViews(context, item, isPlaying);
        } else {
            updateRemoteView(context, mRemoteViews, item, isPlaying);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
                //设置通知的图标
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(mRemoteViews)
                //设置状态栏的标题
                //.setTicker("有新消息呢")
                //设置标题
                .setContentTitle("这个是标题2")
                //消息内容
                .setContentText("这个是内容2")
                //在右边显示一个数量,等价于setContentInfo函数.如果有设置setContentInfo函数,那么本函数会给覆盖
                //.setNumber(12)
                //是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
                //.setOnlyAlertOnce(true)
                //滚动条,indeterminate true - 不确定的,不会显示进度,false - 根据max和progress情况显示进度条
                //.setProgress (100, 50, true)
                //设置默认的提示音
                //.setDefaults(Notification.DEFAULT_ALL)
                //设置该通知的优先级
                //.setPriority(Notification.PRIORITY_DEFAULT)
                //让通知左右滑的时候不能取消通知
                .setOngoing(true)
                //设置该通知的优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                //.setWhen(System.currentTimeMillis())
                //打开程序后图标消失
                .setAutoCancel(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }

        return builder.build();
    }

    @Nullable
    private RemoteViews mRemoteViews = null;

    private boolean recorded = false;

    /**
     * 设置自定义通知栏布局
     *
     * @param context 上下文
     * @param item
     * @return RemoteViews
     */
    private RemoteViews getCustomViews(Context context, NotifyRecorderItem item, boolean isRecording) {
        LOG.d(TAG, "getCustomViews");
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_player);

        // 录制状态刷新
        updateRemoteView(context, remoteViews, item, isRecording);

        // 录制
        remoteViews.setOnClickPendingIntent(R.id.btn_recorder, getReceiverPendingIntent(context, MusicPlayAction.TYPE_RECORD, 1));
        // 保存
        remoteViews.setOnClickPendingIntent(R.id.btn_save, getReceiverPendingIntent(context, MusicPlayAction.TYPE_SAVE, 1));
        // 打标
        remoteViews.setOnClickPendingIntent(R.id.btn_add_tag, getReceiverPendingIntent(context, MusicPlayAction.TYPE_ADD_TAG, 2));
        // 软件
        remoteViews.setOnClickPendingIntent(R.id.btn_app, getActivityPendingIntent(context, TYPE_APP, 3));
        // 退出
        remoteViews.setOnClickPendingIntent(R.id.btn_exit, getActivityPendingIntent(context, MusicPlayAction.TYPE_EXIT, 4));
        // 点击根布局
        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(context, TYPE_APP, 5));
        return remoteViews;
    }

    private void updateRemoteView(Context context, RemoteViews remoteViews, NotifyRecorderItem music, boolean isRecording) {
        if (isRecording) {
            recorded = true;
            remoteViews.setImageViewResource(R.id.iv_recorder, R.drawable.ic_notify_pause);
            remoteViews.setTextViewText(R.id.tv_recorder, context.getString(R.string.pause));
        } else {
            if (recorded) {
                remoteViews.setImageViewResource(R.id.iv_recorder, R.drawable.ic_notify_resume);
                remoteViews.setTextViewText(R.id.tv_recorder, context.getString(R.string.resume));
            } else {
                remoteViews.setImageViewResource(R.id.iv_recorder, R.drawable.ic_notify_recorder);
                remoteViews.setTextViewText(R.id.tv_recorder, context.getString(R.string.notify_record));
            }
        }
        remoteViews.setTextViewText(R.id.tv_time, FormatUtil.formatMs(music.getCurrentMs()));
    }


    private PendingIntent getActivityPendingIntent(Context context, String type, int code) {
        LOG.d(TAG, "getActivityPendingIntent");
        Intent intent = new Intent(context, clazz);
        intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(NotificationStatusBarReceiver.EXTRA, type);
        return PendingIntent.getActivity(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getReceiverPendingIntent(Context context, String type, int code) {
        LOG.d(TAG, "getReceiverPendingIntent");
        Intent intent = new Intent(NotificationStatusBarReceiver.ACTION_STATUS_BAR);
        intent.putExtra(NotificationStatusBarReceiver.EXTRA, type);
        return PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
