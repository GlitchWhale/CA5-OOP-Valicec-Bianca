package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost/";
        String dbName = "petstore";
        String userName = "root";   // default
        String password = "";

        try (Connection conn =
                     DriverManager.getConnection(url + dbName, userName, password)) {
            System.out.println("Connected to the database");

            App app = new App();

            app.getAllPets(conn);

            int petId = 3;
            Pet foundPet = app.getPetById(conn, petId);
            if (foundPet != null) {
                System.out.println("Found pet: " + foundPet);
            } else {
                System.out.println("Pet with ID " + petId + " not found");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }

/*CREATE TABLE pets (
            id SERIAL PRIMARY KEY,
            pet_id INTEGER,
            price FLOAT,
            name VARCHAR(100),
    pet_type VARCHAR(50)
);

INSERT INTO pets (pet_id, price, name, pet_type) VALUES
(1, 50.99, 'Max', 'Dog'),
(2, 35.50, 'Whiskers', 'Cat'),
(3, 15.75, 'Tweety', 'Bird'),
(4, 10.25, 'Goldie', 'Fish'),
(5, 100.00, 'Rocky', 'Turtle'),
(6, 45.00, 'Buddy', 'Dog'),
(7, 20.50, 'Fluffy', 'Rabbit'),
(8, 75.25, 'Nemo', 'Fish'),
(9, 60.99, 'Mittens', 'Cat'),
(10, 80.00, 'Charlie', 'Parrot');
*/
    public void menu(){

    }

    public void getAllPets(Connection connection){
        List<Pet> pets = new ArrayList<>();
        String query = "SELECT * FROM pets";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int petId = resultSet.getInt("pet_id");
                double price = resultSet.getDouble("price");
                String name = resultSet.getString("name");
                String petType = resultSet.getString("pet_type");

                // Create a new org.example.Pet object and add it to the list
                Pet pet = new Pet(petId, price, name, petType);
                pet.setId(id);
                pets.add(pet);
            }

            // Process the retrieved pets
            for (Pet pet : pets) {
                System.out.println(pet);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching pets: " + e.getMessage());
        }
    }

    public Pet  getPetById(Connection connection, int id) {
        Pet pet = null;
        String query = "SELECT * FROM pets WHERE pet_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int petId = resultSet.getInt("pet_id");
                    double price = resultSet.getDouble("price");
                    String name = resultSet.getString("name");
                    String petType = resultSet.getString("pet_type");

                    // Create the Pet object
                    pet = new Pet(petId, price, name, petType);
                    pet.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching pet: " + e.getMessage());
        }

        return pet;
    }

}
