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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * MessageExecutor has responsibility for creating a flightplan, in MAVLink messages based on the JSON string object coming from API. 
 * Also handles the execution of these messages through the SerialCommHandler to the drone.
 * 
 * Made Singleton, so we do not have multiple instances of MessageExecutor.
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
     * test
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

        JsonFlightPlan fp = Json.decode(json);
        System.out.println(fp.getAuth_token());
        System.out.println(fp.getMessages().get(0).getCmd_id());
    }
    
    public FlightPlan convertFlightPlan(JsonFlightPlan jsonFlightPlan){
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
}
