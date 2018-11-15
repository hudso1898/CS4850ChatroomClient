# CS4850ChatroomClient
Client program for CS4850 Lab 3 Version 2 (BONUS)

This program implements the client side of the CS4850 Lab 3 Version 2 (BONUS).
An executable .jar file can be found in dist.
Usage: $ java -jar Mrh4hdChatroomClient.jar

Available commands:

   <== Commands ==>                 <== Function ==>
   
login <username> <password>   - login to the server
   
send all <message>            - send a message to all users
   
send <username> <message>     - whisper a message to another user
   
who                           - list all users on the server

newuser <username> <password> - create a new user account
   
logout                        - logout and exit the program

help                          - displays this menu.

The program will attempt to connect to the server program on the localhost (127.0.0.1).
If the server is offline, the program will quit automatically.

If the server is currently handling the maximum amount of clients (in this lab, 3), the client
will indefinitely wait to connect until a connection is made available. You may type "logout"
to exit the program at any time, but any other command will not go through until a connection is
made.

If the server shuts down, the server will broadcast a shutdown message. After inputing any command,
the program will quit automatically.
