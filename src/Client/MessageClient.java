package Client;

import java.net.*;
import java.io.*;

/**
 * Client for the Messaging service. This is the only class needed
 * by a consumer end user to use the service.
 *
 * Using the client requires that the user provide a host name and a port number.
 *
 * @author      Harry Gaskin
 * @version     %I%, %G%
 */
public class MessageClient {
    public static void main(String args[]) {
        if(args.length != 2) {
            System.err.println("Client.MessageClient requires command line parameters: <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];

        int portNumber = Integer.parseInt(args[1]);

        try(
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
                ) {
            socketOut.println("Hello Server");
            System.out.println("From Server: " + socketIn.readLine());
            System.out.println("From Server: " + socketIn.readLine());
            socketOut.println(stdIn.readLine());
            System.out.println("From Server: " + socketIn.readLine());
            System.out.println(socketIn.readLine());
            socketOut.println(stdIn.readLine());
            System.out.println(socketIn.readLine());
            socketOut.println(stdIn.readLine());
            System.out.println(socketIn.readLine());
            socketOut.println(stdIn.readLine());
        }
        catch (UnknownHostException e) {
            System.err.println("Could not establish connection with " + hostName + "on port " + portNumber);
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
