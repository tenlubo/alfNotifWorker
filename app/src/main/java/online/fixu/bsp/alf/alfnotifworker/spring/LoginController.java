package online.fixu.bsp.alf.alfnotifworker.spring;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import online.fixu.bsp.alf.alfnotifworker.spring.json.LoginCredentials;
import online.fixu.bsp.alf.alfnotifworker.spring.json.LoginTicket;

public class LoginController {


    /**
     * Tag.
     */
    private static final String TAG = LoginController.class.getSimpleName();

    public static LoginTicket alfrescoLogin() {
        final String url = "http://bike.fixu.online:8080/alfresco/api/-default-/public/authentication/versions/1/tickets";

        // Populate the HTTP Basic Authentitcation header with the username and password
//        HttpAuthentication authHeader = new HttpBasicAuthentication("admin", "admin");
        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        LoginCredentials loginCredenc = new LoginCredentials();

        HttpEntity<LoginCredentials> requestEnt = new HttpEntity<>(loginCredenc, requestHeaders);
        requestEnt.getBody().setUserId("admin");
        requestEnt.getBody().setPassword("admin");

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

        try {
            // Make the network request
            Log.d(TAG, url);
            ResponseEntity<LoginTicket> response = restTemplate.exchange(url, HttpMethod.POST, requestEnt, LoginTicket.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return new LoginTicket(e.getStatusText(), e.getLocalizedMessage());
        } catch (ResourceAccessException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return new LoginTicket(e.getClass().getSimpleName(), e.getLocalizedMessage());
        }
    }
}
