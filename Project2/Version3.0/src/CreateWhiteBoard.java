/*
* @Author: Puffrora
* @Date:   2019-09-20 15:35:02
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-25 18:40:13
*/
import whiteboard.*;

import java.awt.*;
import java.awt.event.*;

import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.io.*;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

import message.MsgOperation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateWhiteBoard {
    static int counter = 0;
    static int port;
    static String userName;
    static String address;
    volatile static ArrayList<drawings> sumDraw = new ArrayList<drawings>();
    static ArrayList<Socket> clientList = new ArrayList<Socket>();
    static WhiteBoard newPad;
    static String dicpath = "chatmsg.txt";

    public static void main(String args[]) {
        parseArgs(args);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Somethings wrong in 'UIManager.setLookAndFeel' process");
        }

        Thread WBT = new Thread(() -> startWBService());
        WBT.start();

        Thread chatboxT = new Thread(() -> startChatboxService());
        chatboxT.start();
    }

    private static void parseArgs(String[] args) {
        if(args.length == 3) {
            if(isNumeric(args[1]) && 1024 <= Integer.parseInt(args[1]) && Integer.parseInt(args[1]) <= 65535) {
                address = args[0];
                port = Integer.parseInt(args[1]);
                userName = args[2];
            }
            else {
                System.out.println("Port number is invalid");
                System.exit(1);
            }
        }
        else {
            System.out.println("Argument input is invalid");
            System.exit(1);
        }
    }
    
    private static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            System.out.println("Invalid port");
            System.exit(1);
        }
        return true;
    }

    public static void startWBService() {
        newPad = new WhiteBoard(userName);
        newPad.setTitle("Server Side");
        newPad.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(port)) {
            System.out.println("Waiting for client connection to port number: " + port);

            // Wait for connections.
            while (true) {
                Socket client = null;
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                counter++;
                System.out.println("Client " + counter + ": Applying for connection! in port num: " + client.getPort());

                // Start a new thread for a connection
                ServerThread t = new ServerThread(client);
                t.start();
            }

        } catch (IOException e) {
            System.out.println("Unable to setup server, try another port.");
            // System.exit(1);
        }

    }

    static class ServerThread extends Thread {
        // Client sends the query here and this thread will produce the responses to the client. In this case, client sends the drawings here
        // And the drawings will be combined with other drawings then send back to the client.
        Socket client;
        DataInputStream is;
        DataOutputStream os;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        BufferedReader in;

        ServerThread(Socket client) {
            this.client = client;
            clientList.add(client);
        }

        public void run() {
            String clientDrawing;
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());

                while (true) {
                    drawings nb = (drawings) ois.readObject();
                    newPad.createNewItemInClient(nb);
                    for(Socket client:clientList) {
                    	
                        oos = new ObjectOutputStream(client.getOutputStream());
                        oos.writeObject(nb);
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException");
                 e.printStackTrace();
            } 
            catch (ClassNotFoundException cnfe) {
                System.out.println("ClassNotFoundException");
                cnfe.printStackTrace();
            }
        }
    }

    public static void startChatboxService(){

        ServerSocketFactory factoryc = ServerSocketFactory.getDefault();

        try (ServerSocket serverc = factoryc.createServerSocket(2029)) {
            System.out.println("Waiting for client connection-");

            // Wait for connections.
            while (true) {
                Socket clientc = serverc.accept();
                // counter ++;
                // System.out.println("Client " + counter + ": Applying for connection!");

                // Start a new thread for a connection
                Thread t = new Thread(() -> serveClient(clientc, dicpath));
                t.start();
            }

        }
        catch (IOException e) {
            // e.printStackTrace();
            System.out.println("IOException occurs");
        }
    }

    private static void serveClient(Socket client, String dicpath) {
        MsgOperation op = new MsgOperation();

        try (Socket clientSocket = client) {
            // Input stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            String msg = input.readUTF();
            System.out.println("CLIENT: " + clientSocket.getInetAddress().getHostName() + " " + clientSocket.getLocalPort() + " " + msg);

            output.writeUTF(op.getMsg(dicpath, msg));

        }
        catch (SocketException se) {
            System.out.println("SocketException, closed ...");
        }
        catch (IOException e) {
            System.out.println("IOException");
            // e.printStackTrace();
        }
        catch (Exception e) {
            // e.printStackTrace();
            System.out.println("UnknownError");
        }
    }

}