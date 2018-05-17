/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import g1.f18.iod.rpi.backend.api.PostAnnounce;
import g1.f18.iod.rpi.backend.services.IAuthenticationService;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class intended to announce the presence of this RPi drone. 
 * This class will be constructed and init() called after Spring is done configuring it's variables and environment
 * @author chris
 */
@Component
public class Announcer {
    @Autowired
    private IAuthenticationService auth;
    
    /**
     * Method will be called AFTER Spring has finished configuring it's beans etc.
     */
    @PostConstruct
    public void init(){
        PostAnnounce post = new PostAnnounce(this.auth);

        while (true) {
            try {
                // Perform post request to server backend.
                post.announcePresence();
                break;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Post request failed. Trying again in 2,5 sec");
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex1) {
                }
            }
        }
    }
}
