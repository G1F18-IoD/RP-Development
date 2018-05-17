/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.api.Controllers;

import g1.f18.iod.rpi.backend.MessageManager;
import g1.f18.iod.rpi.backend.datastructure.DroneStatus;
import g1.f18.iod.rpi.backend.datastructure.Json;
import g1.f18.iod.rpi.backend.services.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController. This is the class which handles HTTP Requests regarding drone commands.
 * Each public method in this class with a @RequestMapping annotation can be called at the specified @RequestMapping value.
 *
 * @author chris
 */
@RestController
public class CommandController {
    
    /**
     * IAuthenticationService. Spring performs Dependency Injection on this field.
     */
    @Autowired
    private IAuthenticationService auth;


    /**
     * HTTP method to get the status from the drone. 
     *
     * @param authToken Auth Token found in HTTP request header
     * @return HttpStatus.OK on succes, HttpStatus.BAD_REQUEST on failure
     */
    @RequestMapping(value = "/api/command/get/status", method = RequestMethod.GET)
    public ResponseEntity<String> getDroneStatus(@RequestHeader(value = "AuthToken") String authToken) {
        if (!this.checkAuthToken(authToken)) { // Check auth token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        DroneStatus stats = MessageManager.getInstance().getStatus();
        if(stats == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Json.encode(stats), HttpStatus.OK);
    }
    
    /**
     * HTTP method to get all available drone commands
     * 
     * @param authToken Auth Token found in HTTP request header
     * @return Map of command IDs and their names, HttpStatus.OK on succes. HttpStatus.UNAUTHORIZED on failure to recognize authToken
     */
    @RequestMapping(value = "/api/command/get/commands", method = RequestMethod.GET)
    public ResponseEntity<String> getAvailableDroneCommands(@RequestHeader(value = "AuthToken") String authToken){
        if (!this.checkAuthToken(authToken)) { // Check auth token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>(Json.encode(MessageManager.getInstance().getAvailableCommands()), HttpStatus.OK);
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
