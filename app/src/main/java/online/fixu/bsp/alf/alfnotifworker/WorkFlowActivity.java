package online.fixu.bsp.alf.alfnotifworker;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import online.fixu.bsp.alf.alfnotifworker.worker.WorkflowViewModel;

public class WorkFlowActivity extends AppCompatActivity {

    private static final String TAG = "WorkFlowActivity";

    public static final String ACTION_SNOOZE =
            "online.fixu.bsp.alf.alfnotifworker.action.SNOOZE";

    private WorkflowViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_work_flow);

        // Get the ViewModel
        mViewModel = new ViewModelProvider(this).get(WorkflowViewModel.class);

        Button mGoButton = findViewById(R.id.go_button);
        // Setup blur image file button
        mGoButton.setOnClickListener(view -> mViewModel.startAlfrescoTaskChecker());

        Button mReloadButton = findViewById(R.id.refresh_button);
        // Setup blur image file button
        mReloadButton.setOnClickListener(view -> mViewModel.oneTimeAlfrescoTaskCheck());

    }
}
