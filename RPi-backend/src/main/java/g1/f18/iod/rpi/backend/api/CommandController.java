/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.api;

import g1.f18.iod.rpi.backend.MessageManager;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.MAVLINK javadoc https://download.roboticsapi.org/2.0/javadoc/overview-summary.html
 * http://api.ning.com/files/i*tFWQTF2R*7Mmw7hksAU-u9IABKNDO9apguOiSOCfvi2znk1tXhur0Bt00jTOldFvob-Sczg3*lDcgChG26QaHZpzEcISM5/MAVLINK_FOR_DUMMIESPart1_v.1.1.pdf
 *
 * @author chris
 */
@RestController
public class CommandController {

    @RequestMapping("/command/arm")
    public ResponseEntity arm(@RequestParam(value = "json", defaultValue = "") String json) {
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/command/disarm")
    public ResponseEntity disarm(@RequestParam(value = "json", defaultValue = "") String json) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method to receive an entire flightplan formatted in json
     *
     * @param json JSON formatted flightplan including authentication tokens, MAVLink messages etc.
     * @return Status code
     */
    @RequestMapping(value = "/command/flightplan", method = RequestMethod.POST)
    public ResponseEntity flightplan(@RequestBody String json) {
        JSONObject jsonObj = new JSONObject(json);
        if (this.checkAuthToken(jsonObj)) { // check json values/contents
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // perform next method call.
        MessageManager.getInstance().handleRequests(HTTPREQ_CMD.FLIGHTPLAN, json);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/command/flightplan/del", method = RequestMethod.POST)
    public ResponseEntity removeFlightPlan(@RequestBody String json) {
        JSONObject jsonObj = new JSONObject(json);
        //if (this.checkAuthToken(jsonObj)) {
        //    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        //}

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/command/getstatus", method = RequestMethod.GET)
    public ResponseEntity getDroneStatus(@RequestBody String json){
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    private boolean checkAuthToken(JSONObject jsonObj) {
        if (jsonObj.has("auth_token")) {
            if (MessageManager.getInstance().checkAuthToken(jsonObj.getString("auth_token"))) {
                return true;
            }
        }
        return false;
    }
}
