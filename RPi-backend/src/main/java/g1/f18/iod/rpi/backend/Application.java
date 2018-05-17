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
     * Main method, being called by RPi upon system start up.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Run Spring Application
        SpringApplication.run(Application.class, args);
    }
}
