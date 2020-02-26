package online.fixu.bsp.alf.alfnotifworker;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.WorkInfo;

import online.fixu.bsp.alf.alfnotifworker.worker.NotificationConstants;
import online.fixu.bsp.alf.alfnotifworker.worker.WorkflowViewModel;

public class WorkFlowActivity extends AppCompatActivity {

    private static final String TAG = WorkFlowActivity.class.getSimpleName();

    private ProgressBar mProgressBar;

    private TextView taskView;

    private Button mGoButton, mReloadButton, mCancelButton;

    private WorkflowViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_work_flow);

        // Cancel Notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationConstants.NOTIFICATION_ID);

        // Get the ViewModel
        mViewModel = new ViewModelProvider(this).get(WorkflowViewModel.class);

        mProgressBar = findViewById(R.id.progress_bar);
        mCancelButton = findViewById(R.id.cancel_button);

        mGoButton = findViewById(R.id.go_button);
        taskView =  findViewById(R.id.bike_fixu_text_view);

        // Setup blur image file button
        mGoButton.setOnClickListener(view -> mViewModel.startAlfrescoTaskChecker());

        mReloadButton = findViewById(R.id.refresh_button);
        // Setup blur image file button
        mReloadButton.setOnClickListener(view -> mViewModel.oneTimeAlfrescoTaskCheck());

        showWorkerData();
    }

    private void showWorkerData() {

        // Show work status
        mViewModel.getSavedAlfrescoInfo().observe(this, listOfWorkInfo -> {

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            WorkInfo workInfo = listOfWorkInfo.get(0);

            boolean finished = workInfo.getState().isFinished();
            if (!finished) {
                showWorkInProgress();
            } else {
                showWorkFinished();

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.
                Data outputData = workInfo.getOutputData();

                String alfrescoTaskName =
                        outputData.getString(NotificationConstants.BIKE_FIXU_TASK_NAME_KEY);

                // If there is an output file show "See File" button
                if (!TextUtils.isEmpty(alfrescoTaskName)) {
                    mViewModel.setAlfrescoTaskName(alfrescoTaskName);
                    taskView.append(alfrescoTaskName);
                }
            }
        });
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private void showWorkInProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mCancelButton.setVisibility(View.VISIBLE);
        mGoButton.setVisibility(View.GONE);
        mReloadButton.setVisibility(View.GONE);
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private void showWorkFinished() {
        mProgressBar.setVisibility(View.GONE);
        mCancelButton.setVisibility(View.GONE);
        mGoButton.setVisibility(View.VISIBLE);
        mReloadButton.setVisibility(View.VISIBLE);
    }
}
