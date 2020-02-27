package online.fixu.bsp.alf.alfnotifworker.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import online.fixu.bsp.alf.alfnotifworker.spring.LoginController;

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

        Context applicationContext = getApplicationContext();
        NotificationUtils.makeStatusNotification(NotificationConstants.NOTIFICATION_NEW_TASK_MESSAGE,
                applicationContext);

        Data outputData = new Data.Builder()
                .putString(NotificationConstants.BIKE_FIXU_TASK_NAME_KEY,
                        LoginController.alfrescoLogin().getEntry().getId())
                .build();
        return Result.success(outputData);
    }
}
