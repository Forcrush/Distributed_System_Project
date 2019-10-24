package ClientServer;
/*
* @Author: Puffrora
* @Date:   2019-09-20 15:35:02
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-23 12:43:51
*/
import whiteboard.*;
import whiteboard.WhiteBoard.drawings;
import ClientServer.SessionManager.*;

import java.awt.*;
import java.awt.event.*;

import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.io.*;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateWhiteBoard {
    static int port = 9090;
    static int counter = 0;
    static String userName = "Server";
    volatile static ArrayList<drawings> sumDraw = new ArrayList<drawings>();
    static HashMap<String,Socket> userMap = new HashMap<>();

    static ArrayList<Socket> clientList = new ArrayList<Socket>();
    static ArrayList<User> userList = new ArrayList<>();

    static WhiteBoard newPad;
    static SessionManagerPanel sessPanel;

    public static void main(String args[]) throws ClassNotFoundException, IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        newPad = new WhiteBoard(userName);
        newPad.setTitle("Server Side");
        newPad.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        //SessionManger
        System.out.println("Session Manager");

        sessPanel= new SessionManagerPanel(port);
        sessPanel.setVisible(true);

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
        //              System.exit(1);
                }
    }

    public void drawToClient(drawings newOb) throws IOException {
    	DataOutputStream os;
    	for(Socket client:clientList) {
        	os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
//          oos = new ObjectOutputStream(client.getOutputStream());
        	String data = newOb.x1 + "," + newOb.y1 + "," + newOb.x2 + "," + newOb.y2 + "," + newOb.R + "," + newOb.G + "," + newOb.B + "," + newOb.stroke + "," + newOb.type + "," + newOb.s1 + "," + newOb.s2;
        	os.writeUTF(data);
        	os.flush();
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
                System.out.println(1);
                os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
                System.out.println(2);
                //oos = new ObjectOutputStream(client.getOutputStream());
                System.out.println(3);
                is = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                //ois = new ObjectInputStream(client.getInputStream());

//                BufferedReader request = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
//                BufferedWriter response = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


                String clientUserName = is.readUTF();
                System.out.println(clientUserName);
                if(clientUserName !=null) {
                    System.out.println(4);
                    System.out.println(5);
                    JOptionPane.showConfirmDialog(null, clientUserName + " wants to join your session", "Do you authorise?",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    System.out.println(6);
                    //copied
                    if (JOptionPane.NO_OPTION==0) {
                        System.out.println(clientUserName + " quit!");
                        os.writeUTF("No");
                        os.flush();
                    } else if (JOptionPane.YES_OPTION==0) {
                        System.out.println(clientUserName + "join!");
                        System.out.println("login success: id = " + " , " + this.client);

                        //create new user object
                        sessPanel.update(new User(counter, clientUserName, client));

                        os.writeUTF("Yes");
                        os.flush();
                    }
                }



                //x1,y1为起始点坐标，x2,y2为终点坐标。四个点的初始值设为0


                int count = 0;
                Graphics g = newPad.getGraphics();
                while (true) {
                	String test = is.readUTF();
                	String[] data = test.split(",");
                	if(data.length == 11) {
	                	drawings newDraw = newPad.new drawings();
	                	newDraw.x1 = Integer.parseInt(data[0]);
	                	newDraw.y1 = Integer.parseInt(data[1]);
	                	newDraw.x2 = Integer.parseInt(data[2]);
	                	newDraw.y2 = Integer.parseInt(data[3]);
	                	newDraw.R = Integer.parseInt(data[4]);
	                	newDraw.G = Integer.parseInt(data[5]);
	                	newDraw.B = Integer.parseInt(data[6]);
	                	newDraw.stroke = Float.parseFloat(data[7]);
	                	newDraw.type = Integer.parseInt(data[8]);
	                	newDraw.s1 = data[9];
	                	newDraw.s1 = data[10];
	                	newPad.createNewItemInClient(newDraw);
                	}
                	
                	
                	
                	
                	
                    for(Socket client:clientList) {
                    	os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
//                        oos = new ObjectOutputStream(client.getOutputStream());
                        os.writeUTF(test);
                        os.flush();
                    }
//                    int x1 = is.readInt();
//                    if(x1 < -10000) {
//                		x1 = is.readInt();
//                	}
//                    int y1 = is.readInt();
//                    int x2 = is.readInt();
//                    int y2 = is.readInt();
//                    g.drawLine(x1, y1, x2, y2);
//                	
//                    for(Socket client:clientList) {
//                    	os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
////                        oos = new ObjectOutputStream(client.getOutputStream());
//                        os.writeInt(x1);
//                    	os.writeInt(y1);
//                    	os.writeInt(x2);
//                    	os.writeInt(y2);
//                    	os.flush();
//                    }
                }
            } catch (IOException e) {
                System.out.println("IOException");
                 e.printStackTrace();
            }
//            catch (ClassNotFoundException cnfe) {
//                System.out.println("ClassNotFoundException");
//                cnfe.printStackTrace();
//            }
        }
    }

}