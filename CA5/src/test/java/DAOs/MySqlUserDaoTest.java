package DAOs;
import DTOs.User;
import Exceptions.DaoException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

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

    @Test
    public void findUserByStudentIdTrue() throws DaoException {
        // Arrange: Prepare test data and dependencies
        UserDaoInterface userDao = new MySqlUserDao(); // Instantiate your UserDao implementation
        int studentId = 104; // Provide a test first name

        // Act: Call the method to be tested
        User user = userDao.findUserByStudentId(studentId);

        // Assert: Verify the results
        assertNotNull("Returned user should not be null", user); // Ensure that the user is not null
        assertEquals("Unexpected first name", studentId, user.getStudentId()); // Ensure that the returned user has the expected studentId
    }

    @Test
    public void findUserByStudentIdFalse() throws DaoException {
        // Arrange: Prepare test data and dependencies
        UserDaoInterface userDao = new MySqlUserDao(); // Instantiate your UserDao implementation
        int studentId = 2568; // Provide a test first name

        // Act: Call the method to be tested
        User user = userDao.findUserByStudentId(studentId);

        // Assert: Verify the results
        assertNull("Returned user should be null", user); // Ensure that the user is null indicating no user was found with the specified studentId
    }
}