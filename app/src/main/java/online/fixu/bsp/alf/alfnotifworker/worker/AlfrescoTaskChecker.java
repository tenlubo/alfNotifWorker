package online.fixu.bsp.alf.alfnotifworker.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.alfresco.mobile.android.api.constants.WorkflowModel;
import org.alfresco.mobile.android.api.exceptions.AlfrescoSessionException;
import org.alfresco.mobile.android.api.model.Person;
import org.alfresco.mobile.android.api.model.Process;
import org.alfresco.mobile.android.api.model.ProcessDefinition;
import org.alfresco.mobile.android.api.model.Task;
import org.alfresco.mobile.android.api.services.WorkflowService;
import org.alfresco.mobile.android.api.session.RepositorySession;
import org.alfresco.mobile.android.api.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        try {
            // connect to on-premise repo
            RepositorySession session = RepositorySession.connect(url,
                    username, password);

            if (session != null) {

                // Get WorkflowService
                workflowService = session.getServiceRegistry().getWorkflowService();

                // start an adhoc workflow

                Map<String, Serializable> variables = new HashMap<>();

                // Process Definition
                String processDefinitionIdentifier = "activitiAdhoc:1:4";
                ProcessDefinition adhoc = workflowService
                        .getProcessDefinition(processDefinitionIdentifier);

                // Assignee
                Person user = session.getServiceRegistry()
                        .getPersonService()
                        .getPerson(session.getPersonIdentifier());
                List<Person> users = new ArrayList<Person>();
                users.add(user);

                // Due date
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.set(Calendar.YEAR, 2013);
                variables.put(WorkflowModel.PROP_WORKFLOW_DUE_DATE,
                        DateUtils.format(calendar));

                // Priority
                variables.put(WorkflowModel.PROP_WORKFLOW_PRIORITY,
                        WorkflowModel.PRIORITY_HIGH);

                // Description
                variables.put(WorkflowModel.PROP_WORKFLOW_DESCRIPTION,
                        DESCRIPTION);

                // Notification
                variables.put(WorkflowModel.PROP_SEND_EMAIL_NOTIFICATIONS,
                        "true");

                variables.put(WorkflowModel.PROP_REVIEW_OUTCOME, WorkflowModel.TRANSITION_REJECT);

                // START THE PROCESS
                Process adhocProcess = workflowService.startProcess(adhoc,
                        users, variables, new ArrayList<>());

                Log.d(TAG, "process identifier for newly started process: " + adhocProcess.getIdentifier());

                // show a list of all processes
                List<Process> processes = workflowService.getProcesses();

                for (Process process : processes) {
                    Log.d(TAG, "process identifier: " + process.getIdentifier());
                }

                // set up closing comment
                String comment = "Completing task ";
                Map<String, Serializable> vars = new HashMap<>();

                // find task(s) to complete
                List<Task> tasks = workflowService.getTasks(adhocProcess);

                // for each task
                for (Task task : tasks) {

                    // set up comment and/or other variables as required
                    comment = comment + task.getIdentifier();
                    vars.put(WorkflowModel.PROP_COMMENT, comment);

                    Log.d(TAG, comment);
                    // Close Active Task
                    workflowService.completeTask(task, vars);

                }

            } else {

                Log.d(TAG, "No Session available!");

            }

        } catch (AlfrescoSessionException e) {
            Log.e(TAG, "Failed to connect: " + e.toString());
            return "Failed to connect: " + e.getMessage();
        }

        Log.d(TAG, "doInBackground Complete");
        return "doInBackground Complete";
    }

}

