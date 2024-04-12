/**
 * Main Author: Liam Moore
 * Other Contributors: Bianca Valicec
 **/

package Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try (
                Socket socket = new Socket("localhost", 8080);
                PrintWriter out= new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            System.out.println("Client running and Connected to Server");
            Scanner consoleInput = new Scanner(System.in);

            System.out.println("\n---- Commands ----");
            System.out.println("1. Find All Users");
            System.out.println("2. Find User By Student ID");
            System.out.println("3. Delete User By Student ID");
            System.out.println("4. Insert New User");
            System.out.println("5. Update User By Student ID");
            System.out.println("6. Find Users Using Filter");
            System.out.println("7. Convert List of Users to JSON");
            System.out.println("8. Convert a single user to JSON");
            System.out.println("0. Exit");
            System.out.print("Enter the number of the command you would like to run: ");

            String command = consoleInput.nextLine();

            while(true){
                int commandInt = Integer.parseInt(command);
                out.println(commandInt);
                if (commandInt == 1) {
                    String findUsers = in.readLine();
                    System.out.println(findUsers);
                } else if (commandInt == 2) {
                    String findUserById = in.readLine();
                    System.out.println(findUserById);
                } else if (commandInt == 3) {
                    String deleteUserById = in.readLine();
                    System.out.println(deleteUserById);
                } else if (commandInt == 4) {
                    String insertUser = in.readLine();
                    System.out.println(insertUser);
                } else if (commandInt == 5) {
                    String updateUser = in.readLine();
                    System.out.println(updateUser);
                } else if (commandInt == 6) {
                    String filterUsers = in.readLine();
                    System.out.println(filterUsers);
                } else if (commandInt == 7) {
                    String convertListToJson = in.readLine();
                    System.out.println(convertListToJson);
                } else if (commandInt == 8) {
                    String convertUserToJson = in.readLine();
                    System.out.println(convertUserToJson);
                } else if (commandInt == 0) {
                    System.out.println("Exiting Client");
                    System.exit(0);
                } else {
                    System.out.println("Invalid Command");
                }

                System.out.println("\n---- Commands ----");
                System.out.println("1. Find All Users");
                System.out.println("2. Find User By Student ID");
                System.out.println("3. Delete User By Student ID");
                System.out.println("4. Insert New User");
                System.out.println("5. Update User By Student ID");
                System.out.println("6. Find Users Using Filter");
                System.out.println("7. Convert List of Users to JSON");
                System.out.println("8. Convert a single user to JSON");
                System.out.println("0. Exit");
                System.out.print("Enter the number of the command you would like to run: ");
                command = consoleInput.nextLine();

            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        System.out.println("Client Closed");
    }
}