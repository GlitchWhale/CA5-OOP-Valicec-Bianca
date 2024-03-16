import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.run();
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

    public void run() {
        String url = "jdbc:mysql://localhost/";
        String dbName = "petstore";
        String userName = "root";   // default
        String password = "";

        try (Connection conn =
                     DriverManager.getConnection(url + dbName, userName, password)) {
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
