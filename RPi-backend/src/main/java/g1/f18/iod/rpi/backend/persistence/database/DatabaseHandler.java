/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.persistence.database;

import g1.f18.iod.rpi.backend.services.IDatabaseService;
import g1.f18.iod.rpi.backend.FlightPlan;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Database handler, as we all know and love it
 *
 * @author chris
 */
public class DatabaseHandler implements IDatabaseService {

    private final int port = 5432;
    private final String url = "jdbc:postgresql://";
    private final String host = "";
    private final String databaseName = "";
    private final String username = "";
    private final String password = "";
    
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
     * @param flightPlan
     *                  FlightPlan object to store in database
     * @return 
     *                  True on succesful storage in database, false otherwise
     */
    @Override
    public boolean storeFlightPlan(FlightPlan flightPlan) {
        String saveLightQuery = "";
        try (PreparedStatement saveFlightPlan = this.conn.prepareStatement(saveLightQuery)) {
            
            saveFlightPlan.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
}

}
