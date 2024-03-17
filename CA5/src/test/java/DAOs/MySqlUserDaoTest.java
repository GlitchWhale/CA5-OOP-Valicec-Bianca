package DAOs;
import DTOs.User;
import Exceptions.DaoException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDaoTest {
    @Test
    public void testFindAllUsers() throws DaoException {
        // Arrange: Prepare test data and dependencies
        UserDaoInterface userDao = new MySqlUserDao(); // Instantiate your UserDao implementation

        // Act: Call the method to be tested
        List<User> users = userDao.findAllUsers();

        // Assert: Verify the results
        assertNotNull("Returned list of users should not be null", users); // Ensure that the list of users is not null
        assertFalse("Returned list of users should not be empty", users.isEmpty()); // Ensure that the list of users is not empty
        // Add more assertions as needed to verify the correctness of the returned data
    }
}