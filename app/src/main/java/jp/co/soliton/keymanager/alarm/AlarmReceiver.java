package jp.co.soliton.keymanager.alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import jp.co.soliton.keymanager.LogCtrl;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.StringList;
import jp.co.soliton.keymanager.activity.AlarmReapplyActivity;
import jp.co.soliton.keymanager.dbalias.ElementApply;
import jp.co.soliton.keymanager.dbalias.ElementApplyManager;

public class AlarmReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here update the widget/remote views.
        Bundle extras = intent.getExtras();

        String id = extras.getString(StringList.ELEMENT_APPLY_ID, "");

        ElementApplyManager mgr = new ElementApplyManager(context);
        ElementApply element = mgr.getElementApply(id);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_notification)
                        .setContentTitle(context.getString(R.string.notif_title))
                        .setContentText(element.getUserId());
        Intent resultIntent = new Intent(context, AlarmReapplyActivity.class);
        resultIntent.putExtra(StringList.ELEMENT_APPLY_ID, id);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AlarmReapplyActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final int _id = (int) System.currentTimeMillis();
        mNotificationManager.notify(_id, mBuilder.build());

        //Release the lock
        wl.release();

    }

    public void setOnetimeTimer(Context context, String elementId){

        try {
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(StringList.ELEMENT_APPLY_ID, elementId);
            final int _id = (int) System.currentTimeMillis();
            PendingIntent pi = PendingIntent.getBroadcast(context, _id, intent, PendingIntent.FLAG_ONE_SHOT);
            ElementApplyManager mgr = new ElementApplyManager(context);
            ElementApply element = mgr.getElementApply(elementId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date expirationDate = formatter.parse(element.getExpirationDate());

            am.set(AlarmManager.RTC_WAKEUP, expirationDate.getTime(), pi);
//            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public void setupNotification(Context context) {
        try {
            cancelNotification(context);
            ElementApplyManager elementMgr = new ElementApplyManager(context);
            List<ElementApply> lsElement = elementMgr.getAllCertificate();
            AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(context, AlarmReceiver.class);
            int index = 0;
            int startIndex = 1000;
            for (ElementApply el : lsElement) {
                if (!el.isNotiEnableFlag() && !el.isNotiEnableBeforeFlag()) {
                    continue;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date expirationDate = formatter.parse(el.getExpirationDate());
                cal.setTime(expirationDate);
                if (el.isNotiEnableFlag()) {
                    intent.removeExtra(StringList.ELEMENT_APPLY_ID);
                    intent.putExtra(StringList.ELEMENT_APPLY_ID, el.getId());

                    PendingIntent pi = PendingIntent.getBroadcast(context, startIndex + index, intent, PendingIntent.FLAG_ONE_SHOT);
                    am.set(AlarmManager.RTC_WAKEUP, expirationDate.getTime(), pi);
                    index++;
                }
                if (el.isNotiEnableBeforeFlag()) {
                    intent.removeExtra(StringList.ELEMENT_APPLY_ID);
                    intent.putExtra(StringList.ELEMENT_APPLY_ID, el.getId());

                    int before = el.getNotiEnableBefore();
                    cal.add(Calendar.DAY_OF_MONTH, before * -1);

                    PendingIntent pi = PendingIntent.getBroadcast(context, startIndex + index, intent, PendingIntent.FLAG_ONE_SHOT);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTime().getTime(), pi);
                    index++;
                }
            }
        } catch (Exception ex) {
            LogCtrl.Logger(LogCtrl.m_strError, ex.toString(), context);
        }
    }
}