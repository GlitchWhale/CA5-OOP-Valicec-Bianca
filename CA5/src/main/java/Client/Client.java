package Client;

import Server.DTOs.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Main Author: Bianca Valicec
 **/
public class Client {

    private static final String IMAGE_PATH = "";
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST = "localhost";

    private static final Map<Integer, String> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put(1, "Find All Users");
        COMMANDS.put(2, "Find User By Student ID");
        COMMANDS.put(3, "Delete User By Student ID");
        COMMANDS.put(4, "Insert New User");
        COMMANDS.put(5, "Update User By Student ID");
        COMMANDS.put(6, "Find Users Using Filter");
        COMMANDS.put(7, "Convert List of Users to JSON");
        COMMANDS.put(8, "Convert a single user to JSON");
        COMMANDS.put(9, "Display Entity by Id");
        COMMANDS.put(10, "Display All Entities"); // Added for Feature 10
        COMMANDS.put(11, "Add Entity"); // Added for Feature 11
        COMMANDS.put(12, "Delete Entity by Id"); // Added for Feature 12
        COMMANDS.put(13, "Download Image"); // Added for Feature 13
        COMMANDS.put(0, "Exit");
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Client running and Connected to Server");

            while (true) {
                printCommands();
                System.out.print("Enter the number of the command you would like to run: ");
                String command = consoleInput.readLine();
                int commandInt = Integer.parseInt(command);
                out.println(commandInt);

                switch (commandInt) {
                    case 2:
                        findUserByStudentId(out, in, consoleInput);
                        break;
                    case 3:
                        deleteUserByStudentId(out, in, consoleInput);
                        break;
                    case 4:
                        insertNewUser(out, in, consoleInput);
                        break;
                    case 5:
                        updateUserByStudentId(out, in, consoleInput);
                        break;
                    case 7:
                        convertUserToJson(out, in, consoleInput);
                        break;
                    case 9:
                        displayEntityById(out, in, consoleInput);
                        break;
                    case 10:
                        displayAllEntities(in);
                        break;
                    case 11:
                        addEntity(out, in, consoleInput);
                        break;
                    case 12:
                        deleteEntityById(out, in, consoleInput);
                        break;
                    case 13:
                        out.println("13"); // Send command for Download Image
                        String fileName = getImageListSendRequest(out, in);
                        receiveFile(new BufferedInputStream(socket.getInputStream()), IMAGE_PATH + fileName);
                        break;
                    case 0:
                        exit(out);
                        break;
                    case 8:
                    case 6:
                    case 1:
                        handleStandardCommand(in);
                        break;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        System.out.println("Client Closed");
    }

    private void printCommands() {
        System.out.println("\n---- Commands ----");
        COMMANDS.forEach((key, value) -> System.out.println(key + ". " + value));
    }

    private void handleStandardCommand(BufferedReader in) throws IOException {
        String response;
        while ((response = in.readLine()) != null) {
            System.out.println(response);
            if (response.isEmpty()) {
                break;  // Exit the loop when an empty line is received
            }
        }
    }

    private void findUserByStudentId(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        try {
            out.println("2"); // Send command for Find User By Student ID
            System.out.print("Enter the student ID of the user to find: ");
            String studentId = consoleInput.readLine();
            out.println(studentId);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.isEmpty()) {
                    break;  // Exit the loop when an empty line is received
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    private void deleteUserByStudentId(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        try {
            out.println("3"); // Send command for Delete User By Student ID
            System.out.print("Enter the student ID of the user to delete: ");
            String studentId = consoleInput.readLine();
            out.println(studentId);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.isEmpty()) {
                    break;  // Exit the loop when an empty line is received
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    private void insertNewUser(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        try {
            out.println("4"); // Send command for Insert New User
            System.out.print("Enter the student ID of the user to insert: ");
            String studentId = consoleInput.readLine();
            out.println(studentId);

            System.out.print("Enter the first name of the user to insert: ");
            String firstName = consoleInput.readLine();
            out.println(firstName);

            System.out.print("Enter the last name of the user to insert: ");
            String lastName = consoleInput.readLine();
            out.println(lastName);

            System.out.println("Enter a course ID of the User to insert: ");
            String courseId = consoleInput.readLine();
            out.println(courseId);

            System.out.println("Enter a course name of the User to insert: ");
            String courseName = consoleInput.readLine();
            out.println(courseName);

            System.out.println("Enter a course grade of the User to insert: ");
            String courseGrade = consoleInput.readLine();
            out.println(courseGrade);

            System.out.println("Enter the semester of the User to insert: (Semester 1, Semester 2)");
            String semester = consoleInput.readLine();
            out.println(semester);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.isEmpty()) {
                    break;  // Exit the loop when an empty line is received
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    private void updateUserByStudentId(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        try {
            out.println("5"); // Send command for Update User By Student ID
            System.out.print("Enter the student ID of the user to update: ");
            String studentId = consoleInput.readLine();
            out.println(studentId);

            System.out.print("Enter the first name of the user to update: ");
            String firstName = consoleInput.readLine();
            out.println(firstName);

            System.out.print("Enter the last name of the user to update: ");
            String lastName = consoleInput.readLine();
            out.println(lastName);

            System.out.println("Enter a course ID of the User to update: ");
            String courseId = consoleInput.readLine();
            out.println(courseId);

            System.out.println("Enter a course name of the User to update: ");
            String courseName = consoleInput.readLine();
            out.println(courseName);

            System.out.println("Enter a course grade of the User to update: ");
            String courseGrade = consoleInput.readLine();
            out.println(courseGrade);

            System.out.println("Enter the semester of the User to update: (Semester 1, Semester 2)");
            String semester = consoleInput.readLine();
            out.println(semester);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.isEmpty()) {
                    break;  // Exit the loop when an empty line is received
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    private void convertUserToJson(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        try {
            out.println("7"); // Send command for Convert List of Users to JSON
            System.out.print("Enter the student ID of the user to convert to JSON: ");
            String studentId = consoleInput.readLine();
            out.println(studentId);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.isEmpty()) {
                    break;  // Exit the loop when an empty line is received
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    private void displayEntityById(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        try {
            out.println("9"); // Send command for Display Entity by id
            System.out.print("Enter the ID of the entity to display: ");
            String entityId = consoleInput.readLine();
            out.println(entityId);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.isEmpty()) {
                    break;  // Exit the loop when an empty line is received
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    private void addEntity(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
//        Implement a client-side menu item that will allow the user to input data for an
//entity, serialize the data into a JSON formatted request and send the JSON request
//to the server. The server will extract the received JSON data and add the entity
//details to the database using a relevant DAO method (INSERT), and will send a
//success/failure response to the client. On successful insertion, the response will
//return the Entity object (as JSON) data incorporating the newly allocated ID (if the ID
//was auto generated). This will be sent from server to client, and the client will
//display the newly added entity, along with its auto generated ID. If the insert fails,
//and appropriate error (as JSON) should be returned to the client and an appropriate
//client-side message displayed for the user.
        Gson gson = new Gson();

        // Input new entity data from user
        System.out.println("Enter new user details:");
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(consoleInput.readLine());
        System.out.print("First Name: ");
        String firstName = consoleInput.readLine();
        System.out.print("Last Name: ");
        String lastName = consoleInput.readLine();
        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(consoleInput.readLine());
        System.out.print("Course Name: ");
        String courseName = consoleInput.readLine();
        System.out.print("Grade: ");
        float grade = Float.parseFloat(consoleInput.readLine());
        System.out.print("Semester: ");
        String semester = consoleInput.readLine();

        // Create User object
        User newUser = new User(studentId, firstName, lastName, courseId, courseName, grade, semester);
        String jsonRequest = gson.toJson(newUser);

        // Send JSON request to server
        out.println(jsonRequest);

        // Receive response from server
        String jsonResponse = in.readLine();
        handleInsertResponse(jsonResponse);
    }

    private void handleInsertResponse(String jsonResponse) {
        Gson gson = new Gson();
        if (jsonResponse.startsWith("{")) { // Check if response is a JSON object
            User insertedUser = gson.fromJson(jsonResponse, User.class);
            System.out.println("Entity inserted successfully:");
            System.out.println(insertedUser);
        } else if (jsonResponse.startsWith("Error:")) { // Check if response is an error message
            System.out.println(jsonResponse);
        } else { // Unexpected response
            System.out.println("Unexpected response from server.");
        }
    }


    private void deleteEntityById(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
//       In a manner similar to above, provide a menu item that will delete an entity by ID,
//send a command (as JSON) to the server to undertake the delete, and display an
//appropriate message on the client.

        // Request user input for entity ID to delete
        System.out.println("Enter ID of the entity to delete: ");
        String entityIdStr = consoleInput.readLine();
        int entityId = Integer.parseInt(entityIdStr);

        // Construct JSON request with the entity ID to delete
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("id", entityId);

        // Send JSON request to the server
        out.println(jsonRequest.toString());

        // Receive response from the server
        String response = in.readLine();
        System.out.println("Server response: " + response);

    }

    private void displayAllEntities(BufferedReader in) throws IOException {
        String json = in.readLine();
        Gson gson = new Gson();
        List<User> users = gson.fromJson(json, new TypeToken<List<User>>() {
        }.getType());
        displayUsers(users);
    }

    private void displayUsers(List<User> users) {
        System.out.println("Users:");
        for (User user : users) {
            System.out.println(user); // Assuming User class has a toString() method
        }
    }


    private String getImageListSendRequest(PrintWriter out, BufferedReader in) throws IOException {
        String response;
        while ((response = in.readLine()) != null) {
            System.out.println(response);
            if (response.isEmpty()) {
                break;  // Exit the loop when an empty line is received
            }
        }
        //get users choice
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the name of the image you would like to download: ");
        String fileName = consoleInput.readLine();
        //send users choice back to server
        out.println(fileName);
        int fileNumber = Integer.parseInt(fileName);
        switch (fileNumber) {
            case 1:
                fileName = "flamingo.jpg";
                break;
            case 2:
                fileName = "macaw.jpg";
                break;
            case 3:
                fileName = "robin.jpg";
                break;
            case 4:
                fileName = "toucan.jpg";
                break;
            default:
                System.out.println("Invalid response from server.");
                break;
        }

        return fileName;
    }

    private static void receiveFile(BufferedInputStream inputStream, String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        // read the size of the file in bytes (the file length)
        StringBuilder fileLengthBuilder = new StringBuilder();
        int nextByte;
        while ((nextByte = inputStream.read()) != '\n') {
            fileLengthBuilder.append((char) nextByte);
        }
        long fileLength = Long.parseLong(fileLengthBuilder.toString());
        System.out.println("Client: File size in bytes = " + fileLength);

        // create a buffer to receive the incoming bytes from the socket
        byte[] buffer = new byte[4 * 1024]; // 4 kilobyte buffer
        System.out.println("Client: Receiving file...");

        int bytesRead; // number of bytes read from the socket
        long bytesRemaining = fileLength; // bytes remaining to be read (initially equal to file size)

        // next, read the raw bytes in chunks (buffer size) that make up the image file
        while (bytesRemaining > 0 && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
            // write the buffer data into the local file
            fileOutputStream.write(buffer, 0, bytesRead);

            // reduce the 'bytesRemaining' to be read by the number of bytes read
            bytesRemaining -= bytesRead;
        }

        System.out.println("Client: File received successfully.");

        // close the file
        fileOutputStream.close();
    }


    private void exit(PrintWriter out) {
        // Send a notification to the server that the client is quitting
        out.println("0");
        // Close the PrintWriter and BufferedReader
        out.close();
        // Optionally, close any other resources if needed
        // To Terminate the client application
        System.exit(0);
    }
}
