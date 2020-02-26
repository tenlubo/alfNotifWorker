package online.fixu.bsp.alf.alfnotifworker.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.alfresco.mobile.android.api.exceptions.AlfrescoSessionException;
import org.alfresco.mobile.android.api.model.Process;
import org.alfresco.mobile.android.api.model.Task;
import org.alfresco.mobile.android.api.services.WorkflowService;
import org.alfresco.mobile.android.api.session.RepositorySession;

import java.util.List;

public class AlfrescoTaskChecker extends Worker {

    /**
     * Tag.
     */
    private static final String TAG = AlfrescoTaskChecker.class.getSimpleName();

    protected static final String DESCRIPTION = "Tutorial Adhoc Process";
    private WorkflowService workflowService;


    public AlfrescoTaskChecker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork()");


        // Modify to suit your Alfresco server installation
        String url = "http://bike.fixu.online:8080/alfresco";
        String username = "admin";
        String password = "alfavil";

        String processMessage = doInBackground(url, username, password);
        Log.d(TAG, "Work response");

        Context applicationContext = getApplicationContext();
        NotificationUtils.makeStatusNotification(NotificationConstants.NOTIFICATION_NEW_TASK_MESSAGE,
                applicationContext);

        Data outputData = new Data.Builder()
                .putString(NotificationConstants.BIKE_FIXU_TASK_NAME_KEY, processMessage)
                .build();
        return Result.success(outputData);
    }

    private String doInBackground(String... params) {

        Log.d(TAG, "doInBackground");
        Log.d(TAG, params[0] + ":" + params[1] + ":" + params[2]);

        String url = params[0];
        String username = params[1];
        String password = params[2];
        String bikeFixuTaskList = "Bike Fixu Tasks:  ";

        try {
            // connect to on-premise repo
            RepositorySession session = RepositorySession.connect(url,
                    username, password);

            if (session != null) {

                // Get WorkflowService
                workflowService = session.getServiceRegistry().getWorkflowService();

                // show a list of all processes
                List<Process> processes = workflowService.getProcesses();

                for (Process process : processes) {
                    Log.d(TAG, "process identifier: " + process.getIdentifier());
                    bikeFixuTaskList += "\n\n:proc-id:" + process.getIdentifier();
                    bikeFixuTaskList += "\n\n:proc-desc:" + process.getDescription();
                }

                // find task(s) to complete
                List<Task> tasks = workflowService.getTasks();

                // for each task
                for (Task task : tasks) {
                    // set up comment and/or other variables as required
                    bikeFixuTaskList += "\n\n:task-id:" + task.getIdentifier();
                    bikeFixuTaskList += "\n\n:task-desc:" + task.getDescription();
                    bikeFixuTaskList += "\n\n:task-assign:" + task.getAssigneeIdentifier();
                    bikeFixuTaskList += "\n\n:task-key:" + task.getKey();
                    bikeFixuTaskList += "\n\n:task-name:" + task.getName();
                    bikeFixuTaskList += "\n\n:task-proc-id:" + task.getProcessIdentifier();
                    bikeFixuTaskList += "\n\n:task-proc-def-id:" + task.getProcessDefinitionIdentifier();
                    Log.d(TAG, bikeFixuTaskList);
                }

            } else {
                Log.d(TAG, "No Session available!");
                return "Failed to connect: No Session available!";
            }

        } catch (AlfrescoSessionException e) {
            Log.e(TAG, "Failed to connect: " + e.toString());
            return "Failed to connect: " + e.getMessage();
        }

        Log.d(TAG, "doInBackground Complete");
        return bikeFixuTaskList+"\n\n";
    }

}

