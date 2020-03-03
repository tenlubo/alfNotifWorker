package online.fixu.bsp.alf.alfnotifworker.ui.login;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import online.fixu.bsp.alf.alfnotifworker.spring.LoginController;

public class AlfrescoLoginWorker extends Worker {

    private static final String TAG = AlfrescoLoginWorker.class.getSimpleName();

    final static String USERNAME_KEY = "USERNAME_KEY";

    final static String PASSWORD_KEY = "PASSWORD_KEY";

    final static String LOGIN_SUCCESS = "LOGIN_SUCCESS";

    final static String LOGIN_ERROR = "LOGIN_ERROR";


    public AlfrescoLoginWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String userName = getInputData().getString(USERNAME_KEY);
        String userPassword = getInputData().getString(PASSWORD_KEY);
        try {
            Data outputData = new Data.Builder()
                    .putString(LOGIN_SUCCESS, LoginController.alfrescoLogin(userName, userPassword))
                    .build();
            return Result.success(outputData);
        } catch (HttpClientErrorException | ResourceAccessException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return Result.failure(new Data.Builder()
                    .putString(LOGIN_ERROR, e.getMessage())
                    .build());
        }
    }
}
