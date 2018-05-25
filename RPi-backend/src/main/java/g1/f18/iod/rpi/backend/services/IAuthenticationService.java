/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.services;

/**
 * Authentication service interface
 * @author chris
 */
public interface IAuthenticationService {
    
    /**
     * Method to check if senders auth token equals to the one locally stored here.
     *
     * @param authToken Senders auth token to check against the local one
     * @return True on token match, false otherwise.
     */
    public abstract boolean checkAuthToken(String authToken);
    
    /**
     * Method to get Auth Token from Authentication service
     * @return Auth token
     */
    public abstract String getAuthToken();
}
