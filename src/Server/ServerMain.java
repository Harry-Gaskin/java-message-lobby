package Server;

import java.net.*;
import java.io.*;

/**
 * Server for the messaging service.
 * Server uses threads to handle multiple simultaneous clients.
 *
 * Server must be provided a port number via args and must be set up before any clients.
 *
 * @author      Harry Gaskin
 * @version     %I%, %G%
 */
public class ServerMain {
    public static void main(String args[]) {
        if(args.length != 1) {
            System.err.println("Server.ServerMain requires command line argument <port number>");
        }

        int portNumber = Integer.parseInt(args[0]);

        try(
                ServerSocket serverSocket = new ServerSocket(portNumber)
                //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
                ) {
            boolean allowingConnections = true;

            while(allowingConnections)
                (new Thread(new ClientHandlerThread(serverSocket.accept()))).start();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandlerThread implements Runnable {

        private Socket socket;

        private ClientHandlerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                if(in.readLine().equalsIgnoreCase("Hello Server")) {
                    out.println("Hello Client");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
