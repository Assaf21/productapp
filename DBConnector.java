/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productsinformationsystem;

/**
 *
 * @author assaf
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    // The URL of the database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/productsdb_al-kaabi_al-qurashi";

    // The username for accessing the database
    private static final String DB_USERNAME = "Assaf";

    // The password for accessing the database
    private static final String DB_PASSWORD = "1441";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
