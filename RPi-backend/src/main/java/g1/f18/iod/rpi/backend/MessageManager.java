/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import g1.f18.iod.rpi.backend.api.HTTPREQ_CMD;
import g1.f18.iod.rpi.backend.datastructure.DroneCommand;
import g1.f18.iod.rpi.backend.datastructure.FlightPlan;
import g1.f18.iod.rpi.backend.datastructure.Json;
import g1.f18.iod.rpi.backend.persistence.database.DatabaseHandler;
import g1.f18.iod.rpi.backend.persistence.dronecomm.DroneCommHandler;
import g1.f18.iod.rpi.backend.services.IDatabaseService;
import java.util.LinkedList;
import java.util.Random;

/**
 * MessageManager has responsibility for creating a flightplan, in MAVLink messages based on the JSON string object coming from API. Also handles the execution of these messages through the
 * DroneCommHandler to the drone.
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

    /**
     * Auth token.
     */
    private String authToken = null;

    /**
     * Database service
     */
    private final IDatabaseService databaseHandler;

    /**
     * Queue of FlightPlan received through the API. If the length of this queue is 0, no FlightPlan objects has been received yet.
     */
    private LinkedList<FlightPlan> flightPlans = new LinkedList<>();

    /**
     * Thread object currently executing Drone commands, if this value is null, no current execution thread exists.
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
                + "    \"author_id\": 1,\n"
                + "    \"created_at\" : 0,\n"
                + "    \"priority\": 0,\n"
                + "    \"commands\": [\n"
                + "        {\n"
                + "            \"cmd_id\": 0,\n"
                + "            \"params\": [0,0,0,0,0,0,0]\n"
                + "        },\n"
                + "		{\n"
                + "            \"cmd_id\": 1,\n"
                + "            \"params\": [1,2,0,0,1,0,2]\n"
                + "        }\n"
                + "    ]\n"
                + "}";

        FlightPlan fp = Json.decode(json, FlightPlan.class);
        System.out.println(fp.getAuthToken());
        // Get command ID
        System.out.println(fp.getCommands().get(0).getCmdId());
        // Get PARAMS list for each command
        for (DroneCommand cmd : fp.getCommands()) {
            System.out.println(cmd.getParams());
        }
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
     * Method to check if senders auth token equals to the one locally stored here.
     *
     * @param tokenToCheck Senders auth token to check against the local one
     * @return True on token match, false otherwise.
     */
    public boolean checkAuthToken(String tokenToCheck) {
        return this.authToken.equals(tokenToCheck);
    }

    /**
     * Public method to be used by the CommandController. This methods responsibility is to figure out which command is coming in, and perform the necesarry next method call.
     *
     * @param cmd Incomming request command
     * @param jsonContent Incoming requests JSON String content.
     * @return True on succesful handling, false otherwise
     */
    public boolean handleRequests(HTTPREQ_CMD cmd, String jsonContent) {
        // Switch on available commands
        switch (cmd) {
            // Incoming command is an entire flightplan
            case FLIGHTPLAN:
                FlightPlan fp = Json.decode(jsonContent, FlightPlan.class);
                this.flightPlans.addLast(fp);
                if (this.databaseHandler.storeFlightPlan(fp)) {
                    return true;
                }
                return false;

            // Incoming command is to execute the current flightplan
            case EXECUTE_FLIGHTPLAN:
                if (this.flightPlans.getFirst() != null) { // Check if we even have a flightplan object to execute
                    this.currentExecutionThread = new Thread(new MessageExecutor(this.flightPlans.removeFirst(), new DroneCommHandler(), 2500));
                }
                if (this.currentExecutionThread != null) {
                    return true;
                }
                return false;

            // Incoming command is to delete a specific flightplan from this.flightPlans
            case REMOVE_FLIGHTPLAN:

                break;

            // Incoming command is a GET_STATUS command
            case GET_STATUS:

                break;

            default:
                break;
        }
        return false;
    }
}
