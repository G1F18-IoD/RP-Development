/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author chris
 */
@SpringBootApplication
public class Application {

    /**
     * Main method, being called by RPi upon system start up. This method performs a blocking post request to the server backend. It will repeat this post request until a response has been received.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Run Spring Application
        SpringApplication.run(Application.class, args);
    }
}
