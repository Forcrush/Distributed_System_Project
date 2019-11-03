import WordIO.WordOperation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.net.ServerSocketFactory;

public class DictionaryServer {

    // Declare the port number
    // private static int port = 3005;
    // Identifies the user number connected
    private static int counter = 0;
    static String dicpath = "";
    static String portString = "";
    static int port = 0;
    // private static String dicpath = "dic.txt";

    public static void main(String[] args) {
        try {
            portString = args[0];
            port = Integer.parseInt(portString);
            dicpath = args[1];
        }
        catch (ArrayIndexOutOfBoundsException ae) {
            // ae.printStackTrace();
            System.out.println("ArrayIndexOutOfBoundsException occurs\nPlease ensure input args in legal form");
            return;
        }
        catch (NumberFormatException ne) {
            // ne.printStackTrace();
            System.out.println("NumberFormatException occurs\nPlease ensure input args in legal form");
            return;
        }

        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try (ServerSocket server = factory.createServerSocket(port)) {
            System.out.println("Waiting for client connection-");

            // Wait for connections.
            while (true) {
                Socket client = server.accept();
                counter ++;
                System.out.println("Client " + counter + ": Applying for connection!");

                // Start a new thread for a connection
                Thread t = new Thread(() -> serveClient(client, dicpath));
                t.start();
            }

        }
        catch (IOException e) {
            // e.printStackTrace();
            System.out.println("IOException occurs");
        }

    }

    private static void serveClient(Socket client, String dicpath) {
        WordOperation op = new WordOperation();

        try (Socket clientSocket = client) {
            // Input stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            String msg = input.readUTF();
            System.out.println("CLIENT: " + clientSocket.getInetAddress().getHostName() + " " + clientSocket.getLocalPort() + " " + msg);

            String[] method = msg.split("(\\s)*::(\\s)*");
            if (method.length < 2) {
                // output.writeUTF("Server: Hi Client " + counter + " The content is illegal !");
                output.writeUTF(" The content is illegal !");
                throw new Exception ("The content from client is illegal !");
            }
            else if (method[0].toLowerCase().equals("query")) {
                // output.writeUTF("Server: Hi Client "+ counter +" this is result for u:\n" + op.queryword(dicpath, method[1]));
                output.writeUTF(op.queryword(dicpath, method[1]));
                System.out.println("query request has been DONE !");
            }
            else if (method[0].toLowerCase().equals("add")) {
                // output.writeUTF("Server: Hi Client "+ counter +" this is result for u:\n" + op.addword(dicpath, method[1]));
                output.writeUTF(op.addword(dicpath, method[1]));
                System.out.println("add request has been DONE !");
            }
            else if (method[0].toLowerCase().equals("delete")) {
                // output.writeUTF("Server: Hi Client "+ counter +" this is result for u:\n" + op.deleteword(dicpath, method[1]));
                output.writeUTF(op.deleteword(dicpath, method[1]));
                System.out.println("delete request has been DONE !");
            }

        }
        catch (SocketException e) {
            System.out.println("closed ...");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
