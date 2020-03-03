package online.fixu.bsp.alf.alfnotifworker.ui.login;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

import online.fixu.bsp.alf.alfnotifworker.R;
import online.fixu.bsp.alf.alfnotifworker.worker.NotificationConstants;

public class LoginViewModel extends AndroidViewModel {

    private final static String TAG_LOGIN_WORKER = "TAG_LOGIN_WORKER";


    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final WorkManager workManager;
    private LiveData<List<WorkInfo>> savedAlfrescoLoginInfo;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        workManager = WorkManager.getInstance(application);
        savedAlfrescoLoginInfo = workManager.getWorkInfosByTagLiveData(TAG_LOGIN_WORKER);
    }

    public void setAlfrescoLoginOwner(LifecycleOwner owner) {
        savedAlfrescoLoginInfo.observe(owner, listOfWorkInfo -> {

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
                //             showWorkInProgress();
            } else {
                //             showWorkFinished();

                Data outputData = workInfo.getOutputData();
                if (outputData.getString(AlfrescoLoginWorker.LOGIN_SUCCESS) != null) {
                    loginResult.setValue(new LoginResult(new LoggedInUserView("Ahojky Lego")));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        });

    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

        OneTimeWorkRequest.Builder alfBuilder =
                new OneTimeWorkRequest.Builder(AlfrescoLoginWorker.class).setInputData(
                        new Data.Builder()
                                .putString(AlfrescoLoginWorker.USERNAME_KEY, username)
                                .putString(AlfrescoLoginWorker.PASSWORD_KEY, password)
                                .build());
        OneTimeWorkRequest alfWork = alfBuilder
                .addTag(TAG_LOGIN_WORKER)
                .build();

        WorkContinuation continuation = workManager
                .beginUniqueWork(NotificationConstants.TAG_ALFRESCO_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        alfWork);

        continuation.enqueue();
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 4;
    }
}
