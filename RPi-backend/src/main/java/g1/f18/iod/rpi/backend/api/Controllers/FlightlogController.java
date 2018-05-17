/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.api.Controllers;

import g1.f18.iod.rpi.backend.MessageManager;
import g1.f18.iod.rpi.backend.datastructure.Json;
import g1.f18.iod.rpi.backend.services.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController. This is the class which handles HTTP Requests regarding Flight Logs.
 * Each public method in this class with a @RequestMapping annotation can be called through webservices at the specified @RequestMapping URL.
 *
 * @author chris
 */
@RestController
public class FlightlogController {
    
    /**
     * IAuthenticationService. Spring performs Dependency Injection on this field.
     */
    @Autowired
    private IAuthenticationService auth;
    
    /**
     * HTTP method to get a flightlog based on @param id. 
     * @param authToken Auth Token found in HTTP request header
     * @param id ID of the flightplan to get the flightlogs for.
     * @return List of flightlogs, HttpStatus.OK on succes. HttpStatus.UNAUTHORIZED on failure to recognize authToken
     */
    @RequestMapping(value = "/api/flightlog/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getFlightLog(@RequestHeader(value = "AuthToken") String authToken, @PathVariable(value = "id", required = true) int id){
        if (!this.checkAuthToken(authToken)) { // Check auth token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>(Json.encode(MessageManager.getInstance().getFlightLogs(id)), HttpStatus.OK);
    }
    
    /**
     * HTTP method to get all flightlogs stored in database
     * @param authToken Auth Token found in HTTP request header
     * @return List of flightlogs, HttpStatus.OK on succes. HttpStatus.UNAUTHORIZED on failure to recognize authToken
     */
    @RequestMapping(value = "/api/flightlog/get/all", method = RequestMethod.GET)
    public ResponseEntity<String> getFlightLog(@RequestHeader(value = "AuthToken") String authToken){
        if (!this.checkAuthToken(authToken)) { // Check auth token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>(Json.encode(MessageManager.getInstance().getFlightLogs()), HttpStatus.OK);
    }
    
    /**
     * Internal method to check the AuthToken from Authentication class.
     * @param authToken The authToken coming from HTTP Request.
     * @return True on succesful match between authToken and MessageManager.authToken
     */
    private boolean checkAuthToken(String authToken) {
        return this.auth.checkAuthToken(authToken);
    }
}
