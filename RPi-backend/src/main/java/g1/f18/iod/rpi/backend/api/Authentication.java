/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.api;

import g1.f18.iod.rpi.backend.services.IAuthenticationService;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling HTTP Request authentication. 
 * @author chris
 */
@Service
public class Authentication implements IAuthenticationService {

    /**
     * Auth token.
     */
    private final String authToken;
    
    public Authentication(){
        this.authToken = this.generateAuthToken();
    }

    /**
     * Method to check if senders auth token equals to the one locally stored here.
     *
     * @param authToken Senders auth token to check against the local one
     * @return True on token match, false otherwise.
     */
    @Override
    public boolean checkAuthToken(String authToken) {
        return this.authToken.equals(authToken);
    }
    
    /**
     * Public method to get AuthToken
     * @return AuthToken
     */
    @Override
    public String getAuthToken(){
        return this.authToken;
    }

    /**
     * Method to generate a random Auth token for the RPi.
     *
     * @return Randomly generated auth token, this token will be
     */
    private String generateAuthToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder localAuthToken = new StringBuilder();
        Random rnd = new Random();
        while (localAuthToken.length() < 18) {
            // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            localAuthToken.append(chars.charAt(index));
        }
        System.out.println("AUTHENTICATIONCLASS: " + localAuthToken.toString());
        return localAuthToken.toString();
    }
    
}
