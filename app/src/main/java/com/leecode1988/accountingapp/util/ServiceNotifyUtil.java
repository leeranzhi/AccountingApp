package com.leecode1988.accountingapp.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.leecode1988.accountingapp.R;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * author:LeeCode
 * create:2020/2/19 0:59
 */
public class ServiceNotifyUtil {
    public static Notification getServiceNotify(Service service, Class tartgetClass, String channerl_id, String channerl_name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(service, channerl_id, channerl_name);
        }
        Intent intent = new Intent(service, tartgetClass);
        PendingIntent pi = PendingIntent.getActivity(service, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(service, channerl_id)
            .setContentTitle("启动页更新服务")
            .setContentText("热评正在更新...")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.mipmap.ic_launcher))
            .setContentIntent(pi)
            .setNumber(1)
            .build();
        service.startForeground(1, notification);
        // notifySystem(notification, 2);
        return notification;
    }


    /**
     * 通知系统
     *
     * @param notification
     * @param count
     */
    private static void notifySystem(Notification notification, int count) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Notification getNotification(Service service, Class targetClass, String channerl_id, String channerl_name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(service, channerl_id, channerl_name);
        }

        Intent intent = new Intent(service, targetClass);
        PendingIntent pi = PendingIntent.getActivity(service, 1, intent, 0);

        Notification notification = new NotificationCompat.Builder(service, channerl_id)
            .setContentTitle("启动页更新服务")
            .setContentText("热评更新失败")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.mipmap.ic_launcher))
            .setContentIntent(pi)
            .setNumber(1)
            .build();
        return notification;
    }


    /**
     * 设置并开启本应用独有的通知渠道
     *
     * @param service
     * @param channerl_id
     * @param channerl_name
     */
    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Service service, String channerl_id, String channerl_name) {
        NotificationChannel notificationChannel = new NotificationChannel(
            channerl_id, channerl_name, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLightColor(Color.WHITE);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
        NotificationChannel channel = manager.getNotificationChannel(channerl_id);
        if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, service.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            service.startActivity(intent);
            Toast.makeText(service.getApplication(), "请手动将通知打开", Toast.LENGTH_SHORT).show();
        }
    }
}
