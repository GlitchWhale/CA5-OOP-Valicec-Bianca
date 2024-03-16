package org.example;

import org.example.App;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testGetAllPets() {
        // Establish a connection to the database
        String url = "jdbc:mysql://localhost/";
        String dbName = "petstore";
        String userName = "root";   // default
        String password = "";
        try (Connection conn = DriverManager.getConnection(url + dbName, userName, password)) {
            System.out.println("Connected to the database");

            // Create an instance of org.example.App
            App app = new App();

            // Call getAllPets method
            app.getAllPets(conn);
        } catch (SQLException e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }
}