package online.fixu.bsp.alf.alfnotifworker.data;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;

import online.fixu.bsp.alf.alfnotifworker.data.model.LoggedInUser;
import online.fixu.bsp.alf.alfnotifworker.spring.LoginController;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            LoginController.alfrescoLogin(username,password);
            LoggedInUser alfUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.Success<>(alfUser);
        } catch (HttpClientErrorException | ResourceAccessException e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
