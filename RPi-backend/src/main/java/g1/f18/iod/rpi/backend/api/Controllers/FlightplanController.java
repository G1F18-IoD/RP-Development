/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.api.Controllers;

import g1.f18.iod.rpi.backend.MessageManager;
import g1.f18.iod.rpi.backend.datastructure.FlightPlan;
import g1.f18.iod.rpi.backend.datastructure.Json;
import g1.f18.iod.rpi.backend.services.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController. This is the class which handles HTTP Requests regarding Flight Plans.
 * Each public method in this class with a @RequestMapping annotation can be called at the specified @RequestMapping value.
 *
 * @author chris
 */
@RestController
public class FlightplanController {
    
    /**
     * IAuthenticationService. Spring performs Dependency Injection on this field.
     */
    @Autowired
    private IAuthenticationService auth;

    /**
     * HTTP method to get an entire flightplan
     *
     * @param authToken Auth Token found in HTTP request header
     * @param json JSON formatted flightplan including authentication tokens, Drone Commands and other params
     * @return HttpStatus.OK on succes, HttpStatus.UNAUTHORIZED on mismatch auth tokens
     */
    @RequestMapping(value = "/api/flightplan/store", method = RequestMethod.POST)
    public ResponseEntity flightplan(@RequestHeader(value = "AuthToken") String authToken, @RequestBody(required = true) String json) {
        if (!checkAuthToken(authToken)) {
            // Check auth token
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        if (MessageManager.getInstance().handleFlightPlan(Json.decode(json, FlightPlan.class))) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * HTTP method to delete an entire flightplan from the RPi.
     *
     * @param authToken Auth Token found in HTTP request header
     * @param id ID of FlightPlan to remove
     * @return HttpStatus.OK on succes, HttpStatus.UNAUTHORIZED on mismatch auth Tokens
     */
    @RequestMapping(value = "/api/flightplan/del/{id}", method = RequestMethod.POST)
    public ResponseEntity removeFlightPlan(@RequestHeader(value = "AuthToken") String authToken, @PathVariable(value = "id", required = true) int id) {
        if (!checkAuthToken(authToken)) {
            // Check auth token
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        if (MessageManager.getInstance().removeFlightPlan(id)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * HTTP method to get a list of all flightplans currently available on the RPi and stored in the database.
     *
     * @param authToken Auth Token found in HTTP request header
     * @return ResponseEntity object with JSON String of flightplans, along with a HTTP status code. Can either be UNAUTHORIZED if auth tokens are wrong, OK if succesful operations.
     */
    @RequestMapping(value = "/api/flightplan/get/db", method = RequestMethod.GET)
    public ResponseEntity getAllFlightPlans(@RequestHeader(value = "AuthToken") String authToken) {
        if (!checkAuthToken(authToken)) {
            // Check auth token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Json.encode(MessageManager.getInstance().getAllFlightplans()), HttpStatus.OK);
    }

    /**
     * HTTP method to get a list of all flightplans currently available on the RPi.
     *
     * @param authToken Auth Token found in HTTP request header
     * @return ResponseEntity object with JSON String of flightplans, along with a HTTP status code. Can either be UNAUTHORIZED if auth tokens are wrong, OK if succesful operations.
     */
    @RequestMapping(value = "/api/flightplan/get/local", method = RequestMethod.GET)
    public ResponseEntity getFlightPlans(@RequestHeader(value = "AuthToken") String authToken) {
        if (!checkAuthToken(authToken)) {
            // Check auth token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Json.encode(MessageManager.getInstance().getFlightplans()), HttpStatus.OK);
    }
    
    /**
     * HTTP method to execute a flightplan on the RPi. This method will invoke the MessageManager.executeFlightPlan() method. 
     *
     * @param authToken Auth Token found in HTTP request header
     * @return ResponseEntity object with JSON String of flightplans, along with a HTTP status code. Can either be UNAUTHORIZED if auth tokens are wrong, OK if succesful operations.
     */
    @RequestMapping(value = "/api/flightplan/execute", method = RequestMethod.GET)
    public ResponseEntity executeFlightPlan(@RequestHeader(value = "AuthToken") String authToken) {
        if (!checkAuthToken(authToken)) {
            // Check auth token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Json.encode(MessageManager.getInstance().executeFlightPlan()), HttpStatus.OK);
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
