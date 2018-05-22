/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.persistence.database;

import g1.f18.iod.rpi.backend.datastructure.DRONE_CMD;
import g1.f18.iod.rpi.backend.datastructure.DroneCommand;
import g1.f18.iod.rpi.backend.services.IDatabaseService;
import g1.f18.iod.rpi.backend.datastructure.FlightPlan;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 * Database handler, as we all know and love it
 *
 * @author chris
 */
@Service
public class DatabaseHandler implements IDatabaseService {

    private final int port = 5432;
    private final String url = "jdbc:postgresql://";
    private final String host = "localhost";
    private final String databaseName = "rpi_db";
    private final String username = "pi";
    private final String password = "raspberry";

    private Connection conn = null;

    /**
     * Public constructor to initialize the database connection.
     */
    public DatabaseHandler() {
        try {
            this.conn = DriverManager.getConnection(this.url + this.host + ":" + this.port + "/" + this.databaseName, this.username, this.password);
            this.conn.setAutoCommit(true);
        } catch (SQLException ex) {
            System.out.println("Error connecting to database, please check credentials listed in DatabaseHandler.java !");
            System.out.println("Error Message: " + ex);
            System.exit(1);
        }
        System.out.println(this.conn);
    }

    /**
     * Missing implementation
     *
     * @param flightPlan FlightPlan object to store in database
     * @return True on succesful storage in database, false otherwise
     */
    @Override
    public int storeFlightPlan(FlightPlan flightPlan) {
        int flightPlanId = 0;
        String saveFlightQuery = "INSERT INTO flightplan (priority,cmd_delay,createdat, executedat) VALUES (?,?,?,?) RETURNING id;";
        try (PreparedStatement saveFlightPlan = this.conn.prepareStatement(saveFlightQuery)) {
            saveFlightPlan.setInt(1, flightPlan.getPriority());
            saveFlightPlan.setInt(2, flightPlan.getCmdDelay());
            saveFlightPlan.setLong(3, flightPlan.getCreatedAt());
            saveFlightPlan.setLong(4, flightPlan.getExecutedAt());
            ResultSet rs = saveFlightPlan.executeQuery();
            while (rs.next()) {
                flightPlanId = rs.getInt("id");
                int order = 1;
                for (DroneCommand cmd : flightPlan.getCommands()) {
                    this.storeFlightplanCommands(flightPlanId, order, cmd);
                    order++;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return -1;
        }
        return flightPlanId;
    }
    
    

    /**
     * Internal method to store flightplan DroneCommand objects.
     *
     * @param flightplanId Flightplan ID to store the DroneCommand object with
     * @param order Order of this DroneCommand
     * @param cmd DroneCommand object to store
     * @return True on succesful store in database, false otherwise
     */
    private boolean storeFlightplanCommands(int flightplanId, int order, DroneCommand cmd) {
        String saveCmdQuery = "INSERT INTO flightplan_commands (flightplan_id,cmd,payload,\"order\") VALUES (?,?,?,?);";
        try (PreparedStatement saveCmd = this.conn.prepareStatement(saveCmdQuery)) {
            saveCmd.setInt(1, flightplanId);
            saveCmd.setObject(2, cmd.getCmdId());
            saveCmd.setArray(3, this.conn.createArrayOf("integer", cmd.getParams().toArray()));
            saveCmd.setInt(4, order);
            return saveCmd.execute();
        } catch (SQLException ex) {
            System.out.println("Error saving DroneCommand " + ex);
            return false;
        }
    }

    /**
     * Note: Get these with priority DESC
     *
     * @return
     */
    @Override
    public List<FlightPlan> getFlightPlans() {
        List<FlightPlan> toReturn = new LinkedList<>();
        String getFlightplansQuery = "SELECT * FROM flightplan ORDER BY priority DESC;";
        try (PreparedStatement getFlightplans = this.conn.prepareStatement(getFlightplansQuery)) {
            ResultSet rs = getFlightplans.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int priority = rs.getInt("priority");
                int cmd_delay = rs.getInt("cmd_delay");
                long createdAt = rs.getLong("createdat");
                long executedAt = rs.getLong("executedat");
                List cmds = this.getDroneCommands(id);
                toReturn.add(new FlightPlan(cmds, id, priority, createdAt, executedAt, cmd_delay));
            }
            return toReturn;
        } catch (SQLException ex) {
            System.out.println("Error fetching list of flightplan objects.");
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    private List<DroneCommand> getDroneCommands(int flightplanid){
        List<DroneCommand> toReturn = new ArrayList<>();
        String getDroneCmdQuery = "SELECT * FROM flightplan_commands WHERE flightplan_id = ? ORDER BY \"order\";";
        try(PreparedStatement getDroneCmd = this.conn.prepareStatement(getDroneCmdQuery)){
            getDroneCmd.setInt(1, flightplanid);
            ResultSet rs = getDroneCmd.executeQuery();
            while(rs.next()){
                int id, cmdId = 0;
                id = rs.getInt("id");
                cmdId = rs.getInt("cmd");
                int[] payloadArr = ((int[]) rs.getArray("payload").getArray());
                List payload = new ArrayList<>(Arrays.asList(payloadArr));
                toReturn.add(new DroneCommand(id, cmdId, payload));
            }
            return toReturn;
        } catch (SQLException ex) {
            System.out.println("Error fetching list of DroneCommands");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Get all flight logs for a specific Flightplan with ID: id
     *
     * @param id ID of the flightplan which logs to fetch
     * @return List of String objects, which are the flight logs.
     */
    @Override
    public List<String> getFlightLogs(int id) {
        List<String> toReturn = new ArrayList<>();
        String getFlightLogsQuery = "SELECT * FROM flightplan_logs WHERE flightplan_id = ?;";
        try (PreparedStatement getFlightLogs = this.conn.prepareStatement(getFlightLogsQuery)) {
            getFlightLogs.setInt(1, id);
            ResultSet rs = getFlightLogs.executeQuery();
            while (rs.next()) {
                toReturn.add(rs.getString("logfile"));
            }
            return toReturn;
        } catch (SQLException ex) {
            System.out.println("Error fetching list of flightplan logs.");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Get a list of all flightlogs for this RPi
     *
     * @return List of String objects, which are the flight logs.
     */
    @Override
    public List<String> getFlightLogs() {
        List<String> toReturn = new ArrayList<>();
        String getFlightLogsQuery = "SELECT * FROM flightplan_logs;";
        try (PreparedStatement getFlightLogs = this.conn.prepareStatement(getFlightLogsQuery)) {
            ResultSet rs = getFlightLogs.executeQuery();
            while (rs.next()) {
                toReturn.add(rs.getString("logfile"));
            }
            return toReturn;
        } catch (SQLException ex) {
            System.out.println("Error fetching list of flightplan logs.");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * This is being invoked upon application startup to populate the MessageManager with all non executed FlightPlan objects
     *
     * @return List of FlightPlan objects which has their fields: executedAt equal to NULL or 0 (zero).
     * This can return an empty list if there are no flightplans with executedat = 0
     */
    @Override
    public List<FlightPlan> getNonexecutedFlightPlans() {
        List<FlightPlan> toReturn = new LinkedList<>();
        String getFlightplansQuery = "SELECT * FROM flightplan WHERE executedat = 0 ORDER BY priority DESC;";
        try (PreparedStatement getFlightplans = this.conn.prepareStatement(getFlightplansQuery)) {
            ResultSet rs = getFlightplans.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int priority = rs.getInt("priority");
                int cmd_delay = rs.getInt("cmd_delay");
                long createdAt = rs.getLong("createdat");
                long executedAt = rs.getLong("executedat");
                List cmds = this.getDroneCommands(id);
                toReturn.add(new FlightPlan(cmds, id, priority, createdAt, executedAt, cmd_delay));
            }
            return toReturn;
        } catch (SQLException ex) {
            System.out.println("Error fetching list of flightplan objects.");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean beginExecution(int flightplanId) {
        String beginExecutionQuery = "UPDATE flightplan SET executedat = ? WHERE id = ?;";
        try(PreparedStatement beginExecution = this.conn.prepareStatement(beginExecutionQuery)){
            beginExecution.setLong(1, System.currentTimeMillis());
            beginExecution.setInt(2, flightplanId);
            return beginExecution.execute();
        } catch (SQLException ex) {
            System.out.println("Error updating execution time.");
            System.out.println(ex.getMessage());
            return false;
        }
    }

}
