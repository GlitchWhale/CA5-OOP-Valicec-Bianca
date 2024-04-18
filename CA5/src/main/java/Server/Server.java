package Server;

import Server.Comparators.UserGradeComparator;
import Server.DAOs.MySqlUserDao;
import Server.DAOs.UserDaoInterface;
import Server.DTOs.User;
import Server.Exceptions.DaoException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Main Author: Bianca Valicec
 **/

public class Server {

    final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started");
            int clientNumber = 0;

            while (true) {
                System.out.println("Server: Waiting for connections on port ..." + SERVER_PORT);
                clientSocket = serverSocket.accept();
                clientNumber++;
                System.out.println("Server: Listening for connections on port ..." + SERVER_PORT);

                System.out.println("Server: Client " + clientNumber + " has connected.");
                System.out.println("Server: Port number of remote client: " + clientSocket.getPort());
                System.out.println("Server: Port number of the socket used to talk with client " + clientSocket.getLocalPort());

                Thread t = new Thread(new ClientHandler(clientSocket, clientNumber));
                t.start();
            }

        } catch (IOException e) {
            System.out.println("Error creating server socket: " + e.getMessage());
            System.exit(1);
        } finally {

            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }

            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing server socket: " + e.getMessage());
            }
        }
        System.out.println("Server: Server has stopped");
    }
}

/**
 * Main Author: Bianca Valicec
 **/
class ClientHandler implements Runnable {
    final int clientNumber;
    private final UserDaoInterface IUserDao = new MySqlUserDao();
    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket clientSocket;

    public ClientHandler(Socket clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
        try {
            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error creating client handler: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    @Override
    public void run() {
        try {
            while (true) {
                String command = socketReader.readLine();
                if (command == null) {
                    break;
                }
                System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + command);

                int choice = Integer.parseInt(command);
                switch (choice) {
                    case 1:
                        findAllUsers();
                        break;
                    case 2:
                        findUserByStudentId();
                        break;
                    case 3:
                        deleteUserByStudentId();
                        break;
                    case 4:
                        insertNewUser();
                        break;
                    case 5:
                        updateUserByStudentId();
                        break;
                    case 6:
                        findUsersUsingFilter();
                        break;
                    case 7:
                        convertUserToJson();
                        break;
                    case 8:
                        convertUsersListToJson();
                        break;
                    case 9:
                        displayEntityById();
                        break;
                    case 10:
                        displayAllEntities();
                        break;
                    case 11:
                        String jsonRequest = socketReader.readLine();
                        addEntity(jsonRequest, socketWriter);
                        break;
                    case 0:
                        handleExit(socketWriter);
                        break;
                    default:
                        socketWriter.println("error I'm sorry I don't understand your request");
                        System.out.println("Server message: Invalid request from client.");
                        break;
                }
                System.out.println("Server: (ClientHandler): Command processed.");
            }
        } catch (IOException ex) {
            System.out.println("Error reading from client: " + ex.getMessage());
        } finally {
            try {
                socketWriter.close();
                socketReader.close();
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println("Error closing resources: " + ex.getMessage());
            }
        }
        System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
    }

    /**
     * Main Author: Liam Moore
     * Other Contributors: Bianca Valicec
     **/
    private void findAllUsers() {
        try {
            List<User> users = IUserDao.findAllUsers();
            if (users.isEmpty()) {
                socketWriter.println("No users found.");
            } else {
                StringBuilder response = new StringBuilder();
                response.append(String.format("%-10s %-10s %-15s %-15s %-10s %-20s %-10s %-10s%n",
                        "ID", "Student ID", "First Name", "Last Name", "Course ID", "Course Name", "Grade", "Semester"));
                for (User user : users) {
                    response.append(formatUser(user)).append("\n");
                }
                socketWriter.println(response.toString());
            }
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        }
    }

    /**
     * Main Author: Liam Moore
     * Other Contributors: Bianca Valicec
     **/
    private void findUserByStudentId() {
        try {
            //flush the buffer
            socketReader.readLine();

            String studentIdStr = "";
            //while (studentIdStr.isEmpty()) {
            studentIdStr = socketReader.readLine();
//                if (studentIdStr == null) {
//                    break;
//                }
            //}
            if (studentIdStr != null) {
                int studentId = Integer.parseInt(studentIdStr);
                System.out.println("Server: (ClientHandler): Received student ID from client: " + studentId); // Debug statement
                User user = IUserDao.findUserByStudentId(studentId);
                displayUser(user);
            } else {
                socketWriter.println("Error: No input received from client.");
            }
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            socketWriter.println("Invalid input. Please enter a valid student ID.");
        } catch (IOException e) {
            socketWriter.println("Error reading input from client: " + e.getMessage());
        }
    }


    /**
     * Main Author: Bianca Valicec
     **/
    private void deleteUserByStudentId() {
        try {
            socketReader.readLine(); // Flush the buffer
            String studentIdStr = socketReader.readLine();
            int studentId = Integer.parseInt(studentIdStr);
            boolean deletionResult = IUserDao.deleteUserByStudentId(studentId);
            if (deletionResult) {
                socketWriter.println("User with student ID " + studentId + " deleted successfully.");
            } else {
                socketWriter.println("No user found with student ID " + studentId + ".");
            }
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        } catch (NumberFormatException | IOException e) {
            socketWriter.println("Invalid input. Please enter a valid student ID.");
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void insertNewUser() {
        try {
            socketReader.readLine(); // Flush the buffer

            int studentId = Integer.parseInt(socketReader.readLine());

            String firstName = socketReader.readLine();

            String lastName = socketReader.readLine();

            int courseId = Integer.parseInt(socketReader.readLine());

            String courseName = socketReader.readLine();

            float grade = Float.parseFloat(socketReader.readLine());

            String semester = socketReader.readLine();

            User newUser = new User(studentId, firstName, lastName, courseId, courseName, grade, semester);
            User insertedUser = IUserDao.insertUser(newUser);
            socketWriter.println("User inserted successfully. ID: " + insertedUser.getId());
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        } catch (NumberFormatException | IOException e) {
            socketWriter.println("Invalid input. Please enter valid data.");
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void updateUserByStudentId() {
        try {
            socketReader.readLine(); // Flush the buffer

            int studentIdToUpdate = Integer.parseInt(socketReader.readLine());

            String newFirstName = socketReader.readLine();

            String newLastName = socketReader.readLine();

            int newCourseId = Integer.parseInt(socketReader.readLine());

            String newCourseName = socketReader.readLine();

            float newGrade = Float.parseFloat(socketReader.readLine());

            String newSemester = socketReader.readLine();

            User updatedUser = new User(newFirstName, newLastName, newCourseId, newCourseName, newGrade, newSemester);
            IUserDao.updateUserByStudentId(studentIdToUpdate, updatedUser);
            socketWriter.println("User updated successfully.");
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        } catch (NumberFormatException | IOException e) {
            socketWriter.println("Invalid input. Please enter valid data.");
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void findUsersUsingFilter() {
        try {
            List<User> filteredUsers = IUserDao.findUsersUsingFilter(new UserGradeComparator());
            displayUsers(filteredUsers);
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void convertUserToJson() {
        try {
            socketReader.readLine(); // Flush the buffer

            int studentId = Integer.parseInt(socketReader.readLine());
            String userJson = IUserDao.findUserJsonByStudentId(studentId);
            socketWriter.println(userJson);
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        } catch (NumberFormatException | IOException e) {
            socketWriter.println("Invalid input. Please enter a valid student ID.");
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void convertUsersListToJson() {
        try {
            List<User> users = IUserDao.findAllUsers();
            String json = IUserDao.usersListToJson(users);
            socketWriter.println("JSON representation of users:\n" + json);
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    // Helper methods for displaying users
    private void displayUsers(List<User> users) {
        StringBuilder response = new StringBuilder();
        if (users.isEmpty()) {
            response.append("No users found.");
        } else {
            // Append header
            response.append(String.format("%-10s %-10s %-15s %-15s %-10s %-20s %-10s %-10s%n",
                    "ID", "Student ID", "First Name", "Last Name", "Course ID", "Course Name", "Grade", "Semester"));
            // Append each user
            for (User user : users) {
                response.append(String.format("%-10d %-10d %-15s %-15s %-10d %-20s %-10.2f %-10s%n",
                        user.getId(), user.getStudentId(), user.getFirstName(), user.getLastName(),
                        user.getCourseId(), user.getCourseName(), user.getGrade(), user.getSemester()));
            }
        }
        socketWriter.println(response.toString());
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void displayUser(User user) {
        if (user != null) {
            StringBuilder response = new StringBuilder();
            // Append header
            response.append(String.format("%-10s %-10s %-15s %-15s %-10s %-20s %-10s %-10s%n",
                    "ID", "Student ID", "First Name", "Last Name", "Course ID", "Course Name", "Grade", "Semester"));
            // Append user
            response.append(String.format("%-10d %-10d %-15s %-15s %-10d %-20s %-10.2f %-10s%n",
                    user.getId(), user.getStudentId(), user.getFirstName(), user.getLastName(),
                    user.getCourseId(), user.getCourseName(), user.getGrade(), user.getSemester()));
            socketWriter.println(response.toString());
        } else {
            socketWriter.println("User not found.");
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private String formatUser(User user) {
        return String.format("%-10d %-10d %-15s %-15s %-10d %-20s %-10.2f %-10s",
                user.getId(), user.getStudentId(), user.getFirstName(), user.getLastName(),
                user.getCourseId(), user.getCourseName(), user.getGrade(), user.getSemester());
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void displayEntityById() {
        try {
            socketReader.readLine(); // Flush the buffer
            int entityId = Integer.parseInt(socketReader.readLine());
            // Assuming you have a method in UserDaoInterface to find entity by ID
            User entity = IUserDao.findUserByStudentId(entityId);
            if (entity != null) {
                String json = IUserDao.findUserJsonByStudentId(entityId);
                socketWriter.println(json);
            } else {
                socketWriter.println("Entity not found.");
            }
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        } catch (NumberFormatException | IOException e) {
            socketWriter.println("Invalid input. Please enter a valid ID.");
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void displayAllEntities() {
        try {
            List<User> users = IUserDao.findAllUsers(); // Assuming IUserDao has a method to retrieve all users
            String json = convertUsersListToJson(users);
            socketWriter.println(json);
        } catch (DaoException e) {
            socketWriter.println("Error: " + e.getMessage());
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private String convertUsersListToJson(List<User> users) {
        Gson gson = new Gson();
        return gson.toJson(users);
    }


    /**
     * Main Author: Bianca Valicec
     **/
    private void addEntity(String jsonRequest, PrintWriter out) {
        Gson gson = new Gson();
        try {
            // Deserialize JSON request into User object
            User newUser = gson.fromJson(jsonRequest, User.class);

            // Insert the User object into the database
            User insertedUser = IUserDao.insertUser(newUser);

            if (insertedUser != null) {
                // Serialize the inserted User object (including the auto-generated ID if applicable) into JSON format
                String jsonResponse = gson.toJson(insertedUser);
                out.println(jsonResponse); // Send successful response to client
            } else {
                out.println("Error: Failed to insert user."); // Send error response to client
            }
        } catch (DaoException e) {
            out.println("Error: " + e.getMessage()); // Send error response to client
        }
    }


    /**
     * Main Author: Bianca Valicec
     **/
    private void deleteEntityById(String jsonRequest, PrintWriter out) throws DaoException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonRequest, JsonObject.class);

        // Extract entity ID from JSON request
        int entityId = jsonObject.get("id").getAsInt();

        // Perform deletion based on entity ID
        boolean deletionResult = IUserDao.deleteUserByStudentId(entityId);

        // Send appropriate response to the client
        if (deletionResult) {
            out.println("Entity with ID " + entityId + " deleted successfully.");
        } else {
            out.println("Error: Failed to delete entity with ID " + entityId + ".");
        }
    }

    /**
     * Main Author: Bianca Valicec
     **/
    private void handleExit(PrintWriter out) {
        out.println("Client is quitting. Goodbye.");
        // Optionally, perform any cleanup or logging operations
        // Close the PrintWriter and BufferedReader
        out.close();
        // Terminate the client handler thread
        Thread.currentThread().interrupt();
    }


}