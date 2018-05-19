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
import java.util.LinkedList;
import java.util.List;
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
    private final String username = "postgres";
    private final String password = "agger";

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
    }

    /**
     * Missing implementation
     *
     * @param flightPlan FlightPlan object to store in database
     * @return True on succesful storage in database, false otherwise
     */
    @Override
    public boolean storeFlightPlan(FlightPlan flightPlan) {
        String saveFlightQuery = "INSERT INTO flightplan (priority,cmd_delay,createdat, executedat) VALUES (?,?,?,?) RETURNING id;";
        try (PreparedStatement saveFlightPlan = this.conn.prepareStatement(saveFlightQuery)) {
            saveFlightPlan.setInt(1, flightPlan.getPriority());
            saveFlightPlan.setInt(2, flightPlan.getCmdDelay());
            saveFlightPlan.setInt(3, flightPlan.getCreatedAt());
            saveFlightPlan.setInt(4, flightPlan.getExecutedAt());
            ResultSet rs = saveFlightPlan.executeQuery();
            while (rs.next()) {
                int flightPlanId = rs.getInt("id");
                int order = 1;
                for (DroneCommand cmd : flightPlan.getCommands()) {
                    this.storeFlightplanCommands(flightPlanId, order, cmd);
                    order++;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
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
        String saveCmdQuery = "INSERT INTO flightplan_commands (flightplan_id,cmd,payload,order) VALUES (?,'?','?',?);";
        try (PreparedStatement saveCmd = this.conn.prepareStatement(saveCmdQuery)) {
            saveCmd.setInt(1, flightplanId);
            saveCmd.setObject(2, DRONE_CMD.getAvailableCommands().get(cmd.getCmdId()));
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
        String getFlightplansQuery = "SELECT flightplan_id, c.id as command_id, cmd, payload, \"order\", priority, cmd_delay, createdat, executedat FROM flightplan_commands c JOIN flightplan f ON f.id = c.flightplan_id ORDER BY flightplan_id ASC;";
        try (PreparedStatement getFlightplans = this.conn.prepareStatement(getFlightplansQuery)) {
            ResultSet rs = getFlightplans.executeQuery();
            int fp_id = 0, fp_priority, fp_cmdDelay, fp_createdAt, fp_executedAt;
            // Continue from here... Missing implementation to fetch data from db
            while (rs.next()) {
                if(fp_id != rs.getInt("flightplan_id")){
                    
                }
                fp_id = rs.getInt("flightplan_id");
                fp_priority = rs.getInt("priority");
                fp_cmdDelay = rs.getInt("cmd_delay");
                fp_createdAt = rs.getInt("createdat");
                fp_executedAt = rs.getInt("executedat");

            }
            return new LinkedList<>();
        } catch (SQLException ex) {
            System.out.println("Error fetching list of flightplan objects.");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get a list of all flightlogs for this RPi
     *
     * @return List of String objects, which are the flight logs.
     */
    @Override
    public List<String> getFlightLogs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This is being invoked upon application startup to populate the MessageManager with all non executed FlightPlan objects
     *
     * @return List of FlightPlan objects which has their fields: executedAt equal to NULL or 0 (zero)
     */
    @Override
    public List<FlightPlan> getNonexecutedFlightPlans() {
        return new LinkedList<>();
    }

}
