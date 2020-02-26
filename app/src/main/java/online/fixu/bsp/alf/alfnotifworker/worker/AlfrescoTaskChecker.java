package online.fixu.bsp.alf.alfnotifworker.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AlfrescoTaskChecker extends Worker {

    /**
     * Tag.
     */
    private static final String TAG = AlfrescoTaskChecker.class.getSimpleName();

    public AlfrescoTaskChecker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork()");

        try {
            Thread.sleep(5000, 0);
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, "Work response");

        Context applicationContext = getApplicationContext();
        NotificationUtils.makeStatusNotification(NotificationConstants.NOTIFICATION_NEW_TASK_MESSAGE,
                applicationContext);

        Data outputData = new Data.Builder()
                .putString(NotificationConstants.BIKE_FIXU_TASK_NAME_KEY, "Bike Fixu Task Name tralalalal")
                .build();
        return Result.success(outputData);
    }
}
