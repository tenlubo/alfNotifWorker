package online.fixu.bsp.alf.alfnotifworker.worker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkflowViewModel extends AndroidViewModel {

    private final WorkManager mWorkManager;

    private LiveData<List<WorkInfo>> mSavedAlfrescoInfo;

    private String alfrescoTaskName;

    public WorkflowViewModel(@NonNull Application application) {
        super(application);
        mWorkManager = WorkManager.getInstance(application);

        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        mSavedAlfrescoInfo = mWorkManager.getWorkInfosByTagLiveData(
                NotificationConstants.TAG_ALFRESCO_OUTPUT);
    }

    public void startAlfrescoTaskChecker() {

        PeriodicWorkRequest.Builder alfWorkBuilder = new PeriodicWorkRequest.Builder(
                AlfrescoTaskChecker.class, 900, TimeUnit.SECONDS);

        PeriodicWorkRequest alfWork = alfWorkBuilder
                .addTag(NotificationConstants.TAG_ALFRESCO_OUTPUT)
                .build();
        mWorkManager.enqueueUniquePeriodicWork("alfTaskTag",
                ExistingPeriodicWorkPolicy.KEEP, alfWork);
    }

    public void oneTimeAlfrescoTaskCheck() {
        OneTimeWorkRequest.Builder alfBuilder =
                new OneTimeWorkRequest.Builder(AlfrescoTaskChecker.class);
        OneTimeWorkRequest alfWork = alfBuilder
                .addTag(NotificationConstants.TAG_ALFRESCO_OUTPUT)
                .build();
        mWorkManager.enqueue(alfWork);
    }

    public LiveData<List<WorkInfo>> getSavedAlfrescoInfo() {
        return mSavedAlfrescoInfo;
    }

    public void setAlfrescoTaskName(final String alfrescoTaskName) {
        this.alfrescoTaskName = alfrescoTaskName;
    }

    public String getAlfrescoTaskName() {
        return alfrescoTaskName;
    }
}
