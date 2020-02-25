package online.fixu.bsp.alf.alfnotifworker.worker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WorkflowViewModel extends AndroidViewModel {

    private final WorkManager mWorkManager;

    public WorkflowViewModel(@NonNull Application application) {
        super(application);
        mWorkManager = WorkManager.getInstance(application);

    }

    public void startAlfrescoTaskChecker() {

        PeriodicWorkRequest.Builder myWorkBuilder =
                new PeriodicWorkRequest.Builder(AlfrescoTaskChecker.class, 900, TimeUnit.SECONDS);

        PeriodicWorkRequest myWork = myWorkBuilder.build();
        mWorkManager.enqueueUniquePeriodicWork("alfTaskTag",
                ExistingPeriodicWorkPolicy.REPLACE, myWork);
    }
}
