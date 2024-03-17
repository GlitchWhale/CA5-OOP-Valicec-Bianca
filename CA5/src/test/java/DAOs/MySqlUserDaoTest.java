package DAOs;
import DTOs.User;
import Exceptions.DaoException;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;

public class MySqlUserDaoTest {
    private UserDaoInterface userDao;
    private User userToDelete; // Store the user to delete for the test

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

    @Before
    public void setUp() throws DaoException {
        // Instantiate your UserDao implementation and insert a user into the database
        userDao = new MySqlUserDao();
        userToDelete = new User(12345, "Jane", "Doe", 1, "Math", 80, "Spring 2021");
        // Insert the user into the database
        userDao.insertUser(userToDelete);
    }

    @After
    public void tearDown() throws DaoException {
        // Clean up the database by deleting the user inserted during setup
        if (userToDelete != null) {
            userDao.deleteUserByStudentId(userToDelete.getStudentId());
        }
    }

    @Test
    public void testDeleteUserByStudentId_ExistingUser() throws DaoException {
        // Arrange: Prepare test data and dependencies
        int existingStudentId = userToDelete.getStudentId(); // Use the student ID of the user inserted during setup

        // Act: Call the method to be tested
        int initialUserCount = userDao.findAllUsers().size(); // Get the initial number of users
        userDao.deleteUserByStudentId(existingStudentId);

        // Assert: Verify the effects of deletion
        int finalUserCount = userDao.findAllUsers().size(); // Get the final number of users after deletion

        // If the user existed and was deleted, the final user count should be less than the initial user count
        assertTrue("User should be deleted if it exists", finalUserCount < initialUserCount);
    }

    @Test
    public void testDeleteUserByStudentId_NonExistingUser() throws DaoException {
        // Arrange: Prepare test data and dependencies
        int nonExistingStudentId = -1; // Provide a non-existing student ID

        // Act: Call the method to be tested
        int initialUserCount = userDao.findAllUsers().size(); // Get the initial number of users
        userDao.deleteUserByStudentId(nonExistingStudentId);

        // Assert: Verify the effects of deletion
        int finalUserCount = userDao.findAllUsers().size(); // Get the final number of users after deletion

        // If the user didn't exist, the user count should remain unchanged
        assertEquals("User count should remain unchanged if user does not exist", initialUserCount, finalUserCount);
    }

    @Test
    public void testInsertUser_Success() throws DaoException {
        // Arrange: Prepare test data and dependencies
        UserDaoInterface userDao = new MySqlUserDao(); // Instantiate your UserDao implementation
        User userToInsert = new User(111, "Amy", "O'Neill", 304, "Math", 95.5f, "Semester 1"); // Create a user to insert into the database
        int initialUserCount = userDao.findAllUsers().size(); // Get the initial number of users

        // Act: Call the method to be tested
        userDao.insertUser(userToInsert);

        // Assert: Verify the effects of insertion
        int finalUserCount = userDao.findAllUsers().size(); // Get the final number of users after insertion

        // If insertion is successful, the final user count should be greater than the initial user count
        assertTrue("User should be inserted successfully", finalUserCount > initialUserCount);

        // Clean up: Remove the inserted user from the database
        userDao.deleteUserByStudentId(userToInsert.getStudentId());
    }
}
