/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.enums.MAV_CMD;
import g1.f18.iod.rpi.backend.api.HTTPREQ_CMD;
import g1.f18.iod.rpi.backend.jsonstructure.JsonFlightPlan;
import g1.f18.iod.rpi.backend.jsonstructure.Json;
import g1.f18.iod.rpi.backend.jsonstructure.JsonMAVLinkMessage;
import g1.f18.iod.rpi.backend.persistence.database.DatabaseHandler;
import g1.f18.iod.rpi.backend.persistence.serial.SerialCommHandler;
import g1.f18.iod.rpi.backend.services.IDatabaseService;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * MessageManager has responsibility for creating a flightplan, in MAVLink messages based on the JSON string object coming from API. Also handles the execution of these messages through the
 * SerialCommHandler to the drone.
 *
 * Made Singleton, so we do not have multiple instances of MessageManager including a single point of entry to the drone communications.
 *
 * @author chris
 */
public class MessageManager {

    private static MessageManager instance;

    /**
     * Singleton design pattern
     *
     * @return The one and only instance of this
     */
    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager(new DatabaseHandler());
        }
        return instance;
    }

    /**
     * private constructor, used to initialize database handler
     */
    private MessageManager(DatabaseHandler dbHandler) {
        this.databaseHandler = dbHandler;
    }

    private String authToken = null;
    private final IDatabaseService databaseHandler;

    /**
     * Queue of FlightPlan received through the API. If the length of this queue is 0, no FlightPlan objects has been received yet.
     */
    private LinkedList<FlightPlan> flightPlans = new LinkedList<>();

    /**
     * Thread object currently executing MAVLink commands, if this value is null, no current execution thread exists.
     */
    private Thread currentExecutionThread = null;

    /**
     * Method to test the JSON decoder.
     *
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
     * Method to convert a JSON structured FlightPlan to a normal FlightPlan object, which we can use. This method will convert the overall FlightPlan and each individual MAVLinkMessage.
     *
     * @param jsonFlightPlan JSON FlightPlan structured object to be converted into FlightPlan.class
     * @return A FlightPlan.class structured object based on the jsonFlightPlan.
     */
    public FlightPlan convertFlightPlan(JsonFlightPlan jsonFlightPlan) {
        if (!checkAuthToken(jsonFlightPlan.getAuth_token())) { // Invalid token check
            return null;
        }
        List<JsonMAVLinkMessage> jsonmavmsg = jsonFlightPlan.getMessages();
        Queue<MAVLinkMessage> msgQueue = new LinkedList();

        // HTTP request parameters for the flightplan
        int ownerUserId = jsonFlightPlan.getUser_id();
        int priority = jsonFlightPlan.getPriority();
        String fpAuthToken = jsonFlightPlan.getAuth_token();

        // Go through the list of MAVLinkMessage json wrapper objects and create MAVLink objects based on the cmd_id.
        for (JsonMAVLinkMessage msg : jsonmavmsg) {
            switch (msg.getCmd_id()) {
                case 400:
                    msg_command_long msg400 = new msg_command_long();
                    msg400.msgid = MAV_CMD.MAV_CMD_COMPONENT_ARM_DISARM;
                    msg400.param1 = msg.getParam_1();
                    msgQueue.add(msg400);
                    break;

                default:
                    break;
            }
        }
        return new FlightPlan(msgQueue, priority, fpAuthToken, ownerUserId);
    }

    /**
     * Method to generate a random Auth token for the RPi.
     *
     * @return Randomly generated auth token, this token will be
     */
    public String generateAuthToken() {
        if (this.authToken != null) { // An Auth token has already been generated, we do not wish to overwrite this!!
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
     *
     * @param tokenToCheck Senders auth token to check against the local one
     * @return True on token match, false otherwise.
     */
    private boolean checkAuthToken(String tokenToCheck) {
        return this.authToken.equals(tokenToCheck);
    }

    /**
     * Public method to be used by the CommandController. This methods responsibility is to figure out which command is coming in, and perform the necesarry next method call.
     *
     * @param cmd Incomming request command
     * @param jsonContent Incoming requests JSON String content.
     */
    public void handleRequests(HTTPREQ_CMD cmd, String jsonContent) {
        switch (cmd) {
            // Incoming command is an entire flightplan
            case FLIGHTPLAN:
                FlightPlan fp = this.convertFlightPlan(Json.decode(jsonContent, JsonFlightPlan.class));
                this.flightPlans.addLast(fp);
                this.databaseHandler.storeFlightPlan(fp);
                break;

            // Incoming command is to execute the current flightplan
            case EXECUTE_FLIGHTPLAN:
                if (this.flightPlans.getFirst() != null) {
                    this.currentExecutionThread = new Thread(new MessageExecutor(this.flightPlans.removeFirst(), new SerialCommHandler()));
                }
                break;

            // Incoming command is to delete a specific flightplan from this.flightPlans
            case REMOVE_FLIGHTPLAN:

                break;

            // Incoming command is an ARM command
            case ARM:

                break;

            // Incoming command is a DISARM command
            case DISARM:

                break;

            default:
                break;
        }
    }
}
