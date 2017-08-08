package Server;

import java.net.*;
import java.io.*;

/**
 * Server for the Messaging service.
 *
 * Server must be provided a port number via args and must be set up before any clients.
 *
 * @author      Harry Gaskin
 * @version     %I%, %G%
 */
public class MessageServer {
    public static void main(String args[]) {
        if(args.length != 1) {
            System.err.println("Server.MessageServer requires command line argument <port number>");
        }

        int portNumber = Integer.parseInt(args[0]);

        try(
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
            if(in.readLine().equalsIgnoreCase("Hello Server")) {
                out.println("Hello client");
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
