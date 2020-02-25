/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package online.fixu.bsp.alf.alfnotifworker.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import online.fixu.bsp.alf.alfnotifworker.R;
import online.fixu.bsp.alf.alfnotifworker.WorkFlowActivity;


final class NotificationUtils {

    private static final String TAG = NotificationUtils.class.getSimpleName();

    /**
     * Create a Notification that is shown as a heads-up notification if possible.
     *
     * For this codelab, this is used to show a notification so that you know when different steps
     * of the background work chain are starting
     *
     * @param message Message shown on the notification
     * @param context Context needed to create Toast
     */
    static void makeStatusNotification(String message, Context context) {

        Log.d(TAG,"makeStatusNotification()");

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = NotificationConstants.VERBOSE_NOTIFICATION_CHANNEL_NAME;
            String description = NotificationConstants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            boolean areNotificationsEnabled = notificationManager.areNotificationsEnabled();
            if (!areNotificationsEnabled) {
                // Because the user took an action to create a notification, we create a prompt to let
                // the user re-enable notifications for this application again.
                openNotificationSettingsForApp(context);
                return;
            }

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                NotificationConstants.CHANNEL_ID)
                .setStyle(getBigTextStyle(message))
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.ic_alarm_white_48dp))
                .setContentTitle(NotificationConstants.NOTIFICATION_TITLE)
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getNotificationPendingIntent(context))
                .addAction(getSnoozeAction(context))
                .addAction(getDismissAction(context));

        // Show the notification
        NotificationManagerCompat.from(context).notify(NotificationConstants.NOTIFICATION_ID, builder.build());
    }


    private static NotificationCompat.BigTextStyle getBigTextStyle(String message) {
        // Create the notification
         return new NotificationCompat.BigTextStyle()
                // Overrides ContentText in the big form of the template.
                .bigText(message)
                // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle(NotificationConstants.NOTIFICATION_TITLE)
                // Summary line after the detail section in the big form of the template.
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText("Asi vela bike fixu");
    }

    private static PendingIntent getNotificationPendingIntent(Context context) {

        Intent notifyIntent = new Intent(context, WorkFlowActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static NotificationCompat.Action getDismissAction(Context context){
        // Dismiss Action.
        Intent dismissIntent = new Intent(context, WorkFlowActivity.class);
        dismissIntent.setAction(WorkFlowActivity.ACTION_DISMISS);

        PendingIntent dismissPendingIntent = PendingIntent.getService(context, 0, dismissIntent, 0);
        return new NotificationCompat.Action.Builder(
                        R.drawable.ic_cancel_white_48dp,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();
    }

    private static NotificationCompat.Action getSnoozeAction(Context context){

        Intent snoozeIntent = new Intent(context, WorkFlowActivity.class);
        snoozeIntent.setAction(WorkFlowActivity.ACTION_SNOOZE);

        PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, 0);
        return new NotificationCompat.Action.Builder(
                        R.drawable.ic_alarm_white_48dp,
                        "Snooze",
                        snoozePendingIntent)
                        .build();
    }

    /**
     * Method for sleeping for a fixed about of time to emulate slower work
     */
//    static void sleep() {
//        try {
//            Thread.sleep(Constants.DELAY_TIME_MILLIS, 0);
//        } catch (InterruptedException e) {
//            Log.d(TAG, e.getMessage());
//        }
//    }



    private NotificationUtils() {
    }

    /**
     * Helper method for the SnackBar action, i.e., if the user has this application's notifications
     * disabled, this opens up the dialog to turn them back on after the user requests a
     * Notification launch.
     *
     * IMPORTANT NOTE: You should not do this action unless the user takes an action to see your
     * Notifications like this sample demonstrates. Spamming users to re-enable your notifications
     * is a bad idea.
     */
    private static void openNotificationSettingsForApp(Context context) {
        // Links to this app's notification settings.
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", context.getPackageName());
        intent.putExtra("app_uid", context.getApplicationInfo().uid);
        context.startActivity(intent);
    }
}
