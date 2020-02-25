package online.fixu.bsp.alf.alfnotifworker.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AlfrescoTaskChecker extends Worker {

    private static final String TAG = "AlfrescoTaskChecker";

    public AlfrescoTaskChecker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork()");
        Context applicationContext = getApplicationContext();
        WorkerUtils.makeStatusNotification("Blurring image", applicationContext);

        return Result.success();
    }
}
