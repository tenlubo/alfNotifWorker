package online.fixu.bsp.alf.alfnotifworker.worker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NotificationService extends IntentService {

    /**
     * Tag.
     */
    private static final String TAG = NotificationService.class.getSimpleName();

    public static final String ACTION_SNOOZE =
            "online.fixu.bsp.alf.alfnotifworker.action.SNOOZE";

    public static final String ACTION_DISMISS =
            "online.fixu.bsp.alf.alfnotifworker.action.DISMISS";

    private static final long SNOOZE_TIME = TimeUnit.SECONDS.toMillis(5);

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent(): " + intent);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DISMISS.equals(action)) {
                cancelNotification();
            } else if (ACTION_SNOOZE.equals(action)) {
                handleActionSnooze();
            }
        }
    }

    /**
     * Handles action Snooze in the provided background thread.
     */
    private void handleActionSnooze() {
        Log.d(TAG, "handleActionSnooze()");
        cancelNotification();
        try {
            Thread.sleep(SNOOZE_TIME);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        Context applicationContext = getApplicationContext();
        NotificationUtils.makeStatusNotification(NotificationConstants.NOTIFICATION_NEW_TASK_MESSAGE,
                applicationContext);
    }

    /**
     * Cancel Notification.
     */
    private void cancelNotification() {
        Log.d(TAG, "cancelNotification()");
        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NotificationConstants.NOTIFICATION_ID);
    }
}
