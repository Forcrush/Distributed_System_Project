import message.MsgOperation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatboxServer {

    // Declare the port number
    // private static int port = 3005;
    // Identifies the user number connected
    private static int counter = 0;
    static String dicpath = "";
    static String portString = "";
    static int port = 0;
    // private static String dicpath = "dic.txt";
    static String ip = "localhost";
    static String Username = "Root";
    static int serverPos = 0;

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

        Thread th = new Thread(() -> createServerWindow());
        th.start();

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

    private static void createServerWindow(){

        JFrame frame = new JFrame("ChatBox (Server Side)");
        final JTextArea input = new JTextArea();
        input.setBounds(30, 20, 720, 60);
        final JTextArea output = new JTextArea();
        output.setBounds(30, 160, 720, 360);

        JScrollPane scroll1 = new JScrollPane();
        scroll1.setBounds(30, 20, 720, 60);
        JScrollPane scroll2 = new JScrollPane();
        scroll2.setBounds(30, 160, 720, 360);

        JButton add = new JButton("Send");
        add.setBounds(330, 110, 100, 30);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String res = sendMsg(ip, port, Username, input.getText(), serverPos);
                String[] content = res.split("( )*::( )*");
                if (content.length > 1 && content[1].length() != 0) {
                    serverPos = Integer.parseInt(content[0]);
                    try {
                        Thread.currentThread().sleep(150);
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    output.append(content[1]);
                }
                // output.setBackground(Color.cyan);
                input.setText("");
            }
        });

        frame.add(add);
        frame.add(scroll1);
        frame.add(scroll2);
        scroll1.setViewportView(input);
        scroll2.setViewportView(output);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        while (true){
            try {
                Thread.currentThread().sleep(600);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            String res = sendMsg(ip, port, Username, "", serverPos);
            String[] content = res.split("( )*::( )*");
            if (content.length > 1 && content[1].length() != 0) {
                serverPos = Integer.parseInt(content[0]);
                try {
                    Thread.currentThread().sleep(150);
                }
                catch (InterruptedException iee) {
                    iee.printStackTrace();
                }
                output.append(content[1]);
            }
        }
    }

    public static String sendMsg(String ip, int port, String Username, String sendData, int clientPos){
        String message = "";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
        try (Socket socket = new Socket(ip, port)) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());


            output.writeUTF(String.valueOf(clientPos) + " : " + timestamp + " " + Username + " : " + sendData);
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();

            // waiting for a short time, quiting quickly can lead to msg lost
            try {
                Thread.currentThread().sleep(200);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                //return "InterruptedException occurs\n";
            }
            if (input.available() > 0) {
                message = input.readUTF();
            }
            else return "Msg LOST !" + "\n";

            if (socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    // e.printStackTrace();
                    return "IOException occurs\n";
                }
            }

        }
        catch (UnknownHostException e) {
            // e.printStackTrace();
            return "UnknownHostException occurs";
        }
        catch (IOException e) {
            // e.printStackTrace();
            return "IOException occurs";
        }
        return message;
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
