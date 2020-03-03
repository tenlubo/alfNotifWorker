package online.fixu.bsp.alf.alfnotifworker.spring;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.support.Base64;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import online.fixu.bsp.alf.alfnotifworker.spring.json.authentication.AuthenticationProps;
import online.fixu.bsp.alf.alfnotifworker.spring.json.authentication.LoginCredentials;
import online.fixu.bsp.alf.alfnotifworker.spring.json.authentication.LoginTicket;
import online.fixu.bsp.alf.alfnotifworker.spring.json.workflow.tasks.TaskProps;

public class LoginController {

    /**
     * Tag.
     */
    private static final String TAG = LoginController.class.getSimpleName();

    public static String alfrescoLogin() {
        return  alfrescoLogin("admin", "admin");
    }

    public static String alfrescoLogin(String userName, String password) {
        Log.d(TAG, "alfrescoLogin()");

        final String url = "http://bike.fixu.online:8080/" + AuthenticationProps.AUTH_REST_PATH;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        LoginCredentials loginCredentials = new LoginCredentials();
        HttpEntity<LoginCredentials> requestEnt = new HttpEntity<>(loginCredentials, requestHeaders);
        requestEnt.getBody().setUserId(userName);
        requestEnt.getBody().setPassword(password);

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        // Make the network request
        Log.d(TAG, url);
        ResponseEntity<LoginTicket> response = restTemplate.exchange(url, HttpMethod.POST, requestEnt, LoginTicket.class);

        return "Basic " + Base64.encodeBytes(response.getBody().getEntry().getId().getBytes());

    }

    public static String alfrescoLoginWithTask(String userName, String password) {
        Log.d(TAG, "alfrescoLogin()");

        final String url = "http://bike.fixu.online:8080/" + AuthenticationProps.AUTH_REST_PATH;

        final String taskUrl = "http://bike.fixu.online:8080/" + TaskProps.TASK_REST_PATH;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        LoginCredentials loginCredentials = new LoginCredentials();
        HttpEntity<LoginCredentials> requestEnt = new HttpEntity<>(loginCredentials, requestHeaders);
        requestEnt.getBody().setUserId(userName);
        requestEnt.getBody().setPassword(password);

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        // Make the network request
        Log.d(TAG, url);
        ResponseEntity<LoginTicket> response = restTemplate.exchange(url, HttpMethod.POST, requestEnt, LoginTicket.class);
        HttpHeaders requestHeadersTask = new HttpHeaders();
        requestHeadersTask.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeadersTask.add("Authorization", "Basic " + Base64.encodeBytes(response.getBody().getEntry().getId().getBytes()));

        ResponseEntity<Object> responseTask = restTemplate.exchange(taskUrl, HttpMethod.GET,
                new HttpEntity<>(requestHeadersTask), Object.class);

        return responseTask.getStatusCode().toString();

    }
}
