/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.common.msg_command_long;
import g1.f18.iod.rpi.backend.jsonstructure.JsonFlightPlan;
import g1.f18.iod.rpi.backend.jsonstructure.Json;
import g1.f18.iod.rpi.backend.jsonstructure.JsonMAVLinkMessage;
import g1.f18.iod.rpi.backend.persistence.database.DatabaseHandler;
import g1.f18.iod.rpi.backend.services.IDatabaseService;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * MessageExecutor has responsibility for creating a flightplan, in MAVLink messages based on the JSON string object coming from API. 
 * Also handles the execution of these messages through the SerialCommHandler to the drone.
 * 
 * Made Singleton, so we do not have multiple instances of MessageExecutor including a single point of entry to the drone communications.
 * @author chris
 */
public class MessageExecutor {
    private static MessageExecutor instance;
    
    public static MessageExecutor getInstance(){
        if(instance == null){
            instance = new MessageExecutor();
        }
        return instance;
    }
    
    /**
     * private constructor, used to initialize database handler
     */
    private MessageExecutor(){
        this.dbhandler = new DatabaseHandler();
    }
    
    private String authToken = null;
    private IDatabaseService dbhandler;

    /**
     * Method to test the JSON decoder. 
     * @param args 
     */
    public static void main(String[] args) {
        String json = "{\n"
                + "    \"auth_token\": \"Randomly generated token, created by RPi initially and used by BE to handshake requests sent to RPi.\",\n"
                + "    \"user_id\": 1,\n"
                + "    \"priority\": 0,\n"
                + "    \"messages\": [\n"
                + "        {\n"
                + "            \"cmd_id\": 400,\n"
                + "            \"param_1\": 0,\n"
                + "            \"param_2\": 0,\n"
                + "            \"param_3\": 0,\n"
                + "            \"param_4\": 0,\n"
                + "            \"param_5\": 0,\n"
                + "            \"param_6\": 0,\n"
                + "            \"param_7\": 0\n"
                + "        }\n"
                + "    ]\n"
                + "}";

        JsonFlightPlan fp = Json.decode(json, JsonFlightPlan.class);
        System.out.println(fp.getAuth_token());
        System.out.println(fp.getMessages().get(0).getCmd_id());
    }
    
    /**
     * Method to convert a JSON structured FlightPlan to a normal FlightPlan object, which we can use.
     * This method will convert the overall FlightPlan and each individual MAVLinkMessage.
     * @param jsonFlightPlan
     *                  JSON FlightPlan structured object to be converted into FlightPlan.class
     * @return 
     *                  A FlightPlan.class structured object based on the jsonFlightPlan.
     */
    public FlightPlan convertFlightPlan(JsonFlightPlan jsonFlightPlan){
        if(!checkAuthToken(jsonFlightPlan.getAuth_token())){ // Invalid token check
            return null;
        }
        List<JsonMAVLinkMessage> jsonmavmsg = jsonFlightPlan.getMessages();
        Queue<MAVLinkMessage> msgQueue = new LinkedList();
        for(JsonMAVLinkMessage msg : jsonmavmsg){
            switch(msg.getCmd_id()){
                case 400 :
                    msg_command_long msg400 = new msg_command_long();
                    msg400.msgid = 400;
                    msg400.param1 = msg.getParam_1();
                    msgQueue.add(msg400);
                    break;
                    
                default :
                    break;
            }
        }
        return new FlightPlan(msgQueue);
    }
    
    /**
     * Method to generate a random Auth token for the RPi.
     * @return 
     *              Randomly generated auth token, this token will be 
     */
    public String generateAuthToken(){
        if(this.authToken != null){ // An Auth token has already been generated, we do not wish to overwrite this!!
            return null;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder localAuthToken = new StringBuilder();
        Random rnd = new Random();
        while (localAuthToken.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            localAuthToken.append(chars.charAt(index));
        }
        this.authToken = localAuthToken.toString();
        return this.authToken;
    }
    
    /**
     * Internal method to check if senders auth token equals to the one locally stored here.
     * @param tokenToCheck
     *                  Senders auth token to check against the local one
     * @return 
     *                  True on token match, false otherwise.
     */
    private boolean checkAuthToken(String tokenToCheck){
        return this.authToken.equals(tokenToCheck);
    }
    
    /**
     * Public method to be used by the CommandController.
     * This methods responsibility is to figure out which command is coming in, and perform the necesarry next method call.
     * @param cmd
     *                  Incomming request command
     * @param jsonContent 
     *                  Incoming requests JSON String content.
     */
    public void handleRequests(String cmd, String jsonContent){
        switch(cmd){
            // Incoming command is an entire flightplan
            case "flightplan" :
                FlightPlan fp = this.convertFlightPlan(Json.decode(jsonContent, JsonFlightPlan.class));
                this.dbhandler.storeFlightPlan(fp);
                // Now to handle the execution of the flightplan. 
        }
    }
}
