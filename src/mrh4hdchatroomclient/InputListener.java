/*
 * InputListener
 * CS 4850 - Lab 3 Version 2 (BONUS)
 * Email: mrh4hd@mail.missouri.edu
 * Student ID: 14233000
 * The following class implements the listener on the client side of the program
 * for the version 2 of Lab 3. This class is a thread that listens for input on
 * a socket, and prints out that output. This is done such that the client can
 * accept input and write to the server while also listening for input.
 */
package mrh4hdchatroomclient;

// imports
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import static mrh4hdchatroomclient.ANSI.*;

 // @author hudso
public class InputListener extends Thread {

    // Class variable declarations
    // =========================================================================
    private DataInputStream in = null;
    private PrintStream out = null;
    private boolean close = false;
    private String serverInput;
    private ChatroomClient host;
    // =========================================================================
    
    // constructor (gets input,output streams and the host that's running this
    // thread.
    InputListener (DataInputStream in,PrintStream out,ChatroomClient host) {
        this.in = in;
        this.out = out;
        this.host = host;
    }
    
    // method used to shut down this thread
    public void close() {
        this.close = true;
    } 
    
    @Override
    public void run() {
       if (in != null) {
           while (!close) {
               try {
                   if (in.available() > 0) serverInput = in.readUTF();
               } catch (IOException ex) {
                   System.err.println("Error trying to read input stream!");
               }
            if (serverInput != null) {
                
                out.println(serverInput);
                if(serverInput.equals(ANSI_YELLOW
                        + "<<< SERVER IS SHUTTING DOWN >>>" + ANSI_RESET)) {
                    host.kill(); // kill the host if server shutdown message
                    return;
                }
            }
           
            serverInput = null;
               try {
                   Thread.sleep(100);
               } catch (InterruptedException ex) {
                   System.err.println("Error sleeping: " + ex);
               }
            
           }
       }
    } // end method run
    
} // end class
