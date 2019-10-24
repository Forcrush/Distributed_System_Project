//import message.MsgOperation;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import javax.net.ServerSocketFactory;
//
//public class ChatboxServer {
//
//    // Declare the port number
//    // private static int port = 3005;
//    // Identifies the user number connected
//    private static int counter = 0;
//    static String dicpath = "";
//    static String portString = "";
//    static int port = 0;
//    // private static String dicpath = "dic.txt";
//
//    public static void main(String[] args) {
//        try {
//            portString = args[0];
//            port = Integer.parseInt(portString);
//            dicpath = args[1];
//        }
//        catch (ArrayIndexOutOfBoundsException ae) {
//            // ae.printStackTrace();
//            System.out.println("ArrayIndexOutOfBoundsException occurs\nPlease ensure input args in legal form");
//            return;
//        }
//        catch (NumberFormatException ne) {
//            // ne.printStackTrace();
//            System.out.println("NumberFormatException occurs\nPlease ensure input args in legal form");
//            return;
//        }
//
//        ServerSocketFactory factory = ServerSocketFactory.getDefault();
//
//        try (ServerSocket server = factory.createServerSocket(port)) {
//            System.out.println("Waiting for client connection-");
//
//            // Wait for connections.
//            while (true) {
//                Socket client = server.accept();
//                counter ++;
//                System.out.println("Client " + counter + ": Applying for connection!");
//
//                // Start a new thread for a connection
//                Thread t = new Thread(() -> serveClient(client, dicpath));
//                t.start();
//            }
//
//        }
//        catch (IOException e) {
//            // e.printStackTrace();
//            System.out.println("IOException occurs");
//        }
//
//    }
//
//    private static void serveClient(Socket client, String dicpath) {
//        MsgOperation op = new MsgOperation();
//
//        try (Socket clientSocket = client) {
//            // Input stream
//            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
//            // Output Stream
//            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
//            String msg = input.readUTF();
//            System.out.println("CLIENT: " + clientSocket.getInetAddress().getHostName() + " " + clientSocket.getLocalPort() + " " + msg);
//
//            output.writeUTF(op.getMsg(dicpath, msg));
//            System.out.println("delete request has been DONE !");
//
//
//        }
//        catch (SocketException e) {
//            System.out.println("closed ...");
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
