import whiteboard.*;
import whiteboard.WhiteBoard.drawings;

import java.awt.*;
import java.awt.event.*;
import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static int port = 9090;
    static int counter = 0;
    static String userName = "Server";
    volatile static ArrayList<drawings> sumDraw = new ArrayList<drawings>();
    static ArrayList<Socket> clientList = new ArrayList<Socket>();
    static WhiteBoard newPad;

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        newPad = new WhiteBoard(userName);
        newPad.setTitle(userName);
        newPad.addWindowListener(
                new WindowAdapter() {
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
//    			System.exit(1);
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
                //连接成功后得到数据输出流
                os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
//                os = new DataOutputStream(client.getOutputStream());
                oos = new ObjectOutputStream(client.getOutputStream());
                is = new DataInputStream(new BufferedInputStream(client.getInputStream()));

                ois = new ObjectInputStream(client.getInputStream());


                //x1,y1为起始点坐标，x2,y2为终点坐标。四个点的初始值设为0

                
                int count = 0;
                Graphics g = newPad.getGraphics();
                while (true) {
                	int x1 = is.readInt();
                	if(x1 < -10000) {
                		x1 = is.readInt();
                	}
                	int y1 = is.readInt();
                	int x2 = is.readInt();
                	int y2 = is.readInt();
                	System.out.println("x1 = " + x1);
                	System.out.println("y1 = " + y1);
                	System.out.println("x2 = " + x2);
                	System.out.println("y2 = " + y2);
                	g.drawLine(x1, y1, x2, y2);
                	for(Socket client:clientList) {
                		os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
                		os.writeInt(x1);
                		os.writeInt(y1);
                		os.writeInt(x2);
                		os.writeInt(y2);
                		os.flush();
                	}
//                    WhiteBoard.drawings draw = (WhiteBoard.drawings) ois.readObject();
//                    sumDraw.add(draw);
//                    oos.writeObject(sumDraw);
                	
                	
                	
                	
                	
                	
//                    if (newOb != null) {
//
//                        System.out.println(newOb.x1 + " " + newOb.y2 + " " + newOb.x2 + " " + newOb.y2);
//                        System.out.println(clientSocket.getPort() + "cacacaa" + clientSocket.getLocalPort());
//                        System.out.println(number);
//
//                        ArrayList<Integer> coordinate = new ArrayList<Integer>();
//
//    //                            oss.writeObject(newOb);
//                        coordinate.add(newOb.x1);
//                        coordinate.add(newOb.y1);
//                        coordinate.add(newOb.x2);
//                        coordinate.add(newOb.y2);
//                        for (int i = 0; i < 4; i++) {
//                            os.writeInt(coordinate.get(i));
//                            System.out.println("wrote " + i);
//                        }
//                        count += 1;
//                        os.flush();
//    //                        if(count == 20) {
//    //                            os.flush();
//    //                            count = 0;
//    //                        }
//
//                        newOb = null;
//    //                        int x1, x2, y1, y2;
//    //                        x1=is.readInt();
//    //                        y1=is.readInt();
//    //                        x2=is.readInt();
//    //                        y2=is.readInt();
//    //                        Graphics g = this.getGraphics();
//    //                        g.drawLine(x1, y1, x2, y2);
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}