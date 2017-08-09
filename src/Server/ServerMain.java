package Server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

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

    // currently not being accessed in a threadsafe way.
    private static ArrayList<ClientHandlerThread> currentUsers = new ArrayList<ClientHandlerThread>();

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
        private String username = "";

        private ClientHandlerThread(Socket socket) {
            this.socket = socket;
        }

        public String getUsername() {
            return this.username;
        }

        @Override
        public void run() {
            try (
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                // Wait for connection message from client, if unexpected, close.
                if(in.readLine().equalsIgnoreCase("Hello Server")) {
                    out.println("Hello Client");
                }
                else {
                    out.println("User not using recognized protocol. Aborting.");
                    return;
                }

                out.println("Please provide a Username: ");
                this.username = in.readLine();
                out.println("Username set to " + username);

                // Add thread to current users
                currentUsers.add(this);

                int i = 1;

                String response = "";
                do {
                    String message = "Current users: (send r to refresh)\n";

                    for (ClientHandlerThread user : currentUsers) {
                        if (user.getUsername().equals(username))
                            continue;
                        message.concat(i + ":\t" + user.getUsername() + "\n");
                    }

                    out.println(message);
                    response = in.readLine();

                } while(response.equals("r"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // remove user from current users
                currentUsers.remove(this);
            }
        }
    }
}
