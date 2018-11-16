/*
 * Mrh4hdChatroomClient
 * CS 4850 - Lab 3 Version 2 (BONUS)
 * Email: mrh4hd@mail.missouri.edu
 * Student ID: 14233000
 * The following class implements the client side of the program for the
 * version 2 of Lab 3.
 */

package mrh4hdchatroomclient;
 // @author hudso

// imports
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import static mrh4hdchatroomclient.ANSI.*;


public class ChatroomClient {
    
    // Class variable declarations
    // =========================================================================
    Scanner scanner = new Scanner(System.in);
    Socket socket;
    DataOutputStream out = null;
    DataInputStream in   = null;
    
    // strings for inputs
    String username = null;
    String password = null;
    String destUser = null;
    String buf;
    
    private boolean serverAlive = true; /* used to exit program upon server
    termination */
    
    // =========================================================================
    
    public ChatroomClient() { /* constructor contains method for
    running the client */
    
        try { // first try-catch catches various IOExceptions in the method
            try { // second try-catch is to attempt to open a connection
                socket = new Socket();
                InetSocketAddress address = new InetSocketAddress(
                        "127.0.0.1",13000); // loopback to host
                System.out.print("Attempting to connect to the server => ");
                socket.connect(address, 5000); // timeout: 5 seconds
                System.out.println(ANSI_GREEN + "connected!"+ ANSI_RESET);
            } catch (IOException e) {
                System.out.println(ANSI_RED + "Server unavailable."
                        + ANSI_RESET);
                return;
            }
            
            System.out.print("Waiting for open connection => ");
            out = new DataOutputStream(socket.getOutputStream());
            in  = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            
            // this creates a listener that waits for input from the server.
            // the listener will output messages it recieves from the server.
            InputListener inputListen = new InputListener(in,System.out,this);
            Thread thread = inputListen;
            thread.start();
            
            
            String input = "";
            OUTER:
            while (!input.equals("logout") && serverAlive) {
                input = scanner.next();
                if(!serverAlive) break;
                
                // switch on the first token (i.e. command)
                switch (input) {
                    // login implementation
                    case "login":
                        username = scanner.next();
                        password = scanner.next();
                        out.writeUTF("login" + " " + username + " " + password);
                        break;
                    
                    case "send":
                    // send all & send <user> implementation
                        buf = scanner.next();
                        if (buf.equals("all")) { // send all
                            input = scanner.nextLine();
                            out.writeUTF("all" + input);
                        }
                        else {
                            destUser = buf;
                            input = scanner.nextLine();
                            out.writeUTF("unicast " + destUser + input );
                        }   break;
                    // newuser implementation
                    case "newuser":
                        username = scanner.next();
                        password = scanner.next();
                        if ((username.length() < 32) && ((password.length() >= 4) && (password.length() <= 8)))
                            out.writeUTF("newuser" + " " + username + " " + password);
                        else System.out.println(ANSI_RED + "Error: username or password invalid.\n"
                                + "Username should be less than 32 characters and password between 4 and 8 characters."
                                + ANSI_RESET);
                        break;
                    // who implementation
                    case "who":
                        out.writeUTF("who");
                        break;
                    // help functions to display available commands
                    case "help":
                        System.out.println("    <== Commands ==>                 <== Function ==>\n"
                                + "login <username> <password>   - login to the server\n"
                                + "send all <message>            - send a message to all users\n"
                                + "send <username> <message>     - whisper a message to another user\n"
                                + "who                           - list all users on the server\n"
                                + "newuser <username> <password> - create a new user account\n"
                                + "logout                        - logout and exit the program\n"
                                + "help                          - displays this menu.");
                        break;
                    // logout implementation
                    case "logout":
                        break OUTER;
                    // default for unknown command
                    default:
                        System.out.println("Invalid command. Type \"help\" for options.");
                        break;
                }
            }
            
            // shutdown phase
            inputListen.close();
            out.writeUTF("logout");
            scanner.close();
            in.close();
            out.close();
            socket.close();
            
        } catch (IOException ex) {
            System.out.println("Error: server is busy or offline. Try again later.");
        }
    }
    
    // method used to kill this thread
    public void kill() {
        serverAlive = false;
    }
    
    // main method simply invokes the constructor.
    public static void main(String[] args) throws UnknownHostException, IOException {
        ChatroomClient client = new ChatroomClient();
    }
}
