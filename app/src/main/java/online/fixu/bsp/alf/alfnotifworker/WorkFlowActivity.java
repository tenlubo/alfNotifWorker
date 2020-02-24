package online.fixu.bsp.alf.alfnotifworker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import online.fixu.bsp.alf.alfnotifworker.worker.WorkflowViewModel;

public class WorkFlowActivity extends AppCompatActivity {

    private WorkflowViewModel mViewModel;

    public static final String TAG = "WorkFlowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_flow);

        // Get the ViewModel
        mViewModel = new ViewModelProvider(this).get(WorkflowViewModel.class);
    }
}
