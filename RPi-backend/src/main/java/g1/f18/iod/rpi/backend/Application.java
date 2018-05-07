/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import g1.f18.iod.rpi.backend.api.PostService;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author chris
 */
@SpringBootApplication
public class Application {

    /**
     * Main method, being called by RPi upon system start up. This method performs a blocking post request to the server backend.
     * It will repeat this post request until a response has been received. 
     * @param args 
     */
    public static void main(String[] args) {
        // Run Spring Application
        SpringApplication.run(Application.class, args);
        
        // Generate auth token for this RPi
        String authToken = MessageExecutor.getInstance().generateAuthToken();
        try {
            // Get local IP address
            String ip = Inet4Address.getLocalHost().getHostAddress();

            // Generate JSON String
            String json = generateJson(authToken, ip);

            while (true) {
                try {
                    // Perform post request to server backend.
                    PostService.doPostRequest(json);
                    break;
                } catch (IOException ex) {
                    System.out.println("Post request failed. Trying again in 2,5 sec");
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException ex1) {
                    }
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Error occured when fetching local IP address. " + ex.getMessage());
        }
    }

    private static String generateJson(String authToken, String ip) {
        return new JSONObject()
                .put("auth_token", authToken)
                .put("ip_address", ip)
                .toString();
    }
}
