package ClientServer;
/*
* @Author: Puffrora
* @Date:   2019-09-20 15:35:02
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-24 11:53:15
*/
import whiteboard.*;
import whiteboard.WhiteBoard.drawings;

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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateWhiteBoard {
	public final static int MIN_PORT = 0;
	public final static int MAX_PORT = 65535;
	public static final String TYPE = "server";
    public static int port = 9090;
    public static String address = "localhost";
    public static int counter = 0;
    public static String userName = "Server";
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
			if(isNumeric(args[1]) && MIN_PORT <= Integer.parseInt(args[1]) && Integer.parseInt(args[1]) <= MAX_PORT) {
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
        newPad = new WhiteBoard(TYPE, userName);
        newPad.setTitle(userName);
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
    public void drawToClient(drawings newOb) throws IOException {
    	DataOutputStream os;
    	for(Socket client:clientList) {
        	os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        	String data = newOb.x1 + "," + newOb.y1 + "," + newOb.x2 + "," + newOb.y2 + "," + newOb.R + "," + newOb.G + "," + newOb.B + "," + newOb.stroke + "," + newOb.type + "," + newOb.s1 + "," + newOb.s2;
        	os.writeUTF(data);
        	os.flush();
//        	os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
//          os.writeInt(newOb.x1);
//        	os.writeInt(newOb.y1);
//        	os.writeInt(newOb.x2);
//        	os.writeInt(newOb.y2);
//        	os.flush();
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

        ServerThread(Socket client) {
            this.client = client;
            clientList.add(client);
        }

        public void run() {
            try {
                //连接成功后得到数据输出流
                os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
                oos = new ObjectOutputStream(client.getOutputStream());
                
                is = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                ois = new ObjectInputStream(client.getInputStream());


                //x1,y1为起始点坐标，x2,y2为终点坐标。四个点的初始值设为0

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
//                    int x1 = is.readInt();
//                    if(x1 < -10000) {
//                		x1 = is.readInt();
//                	}
//                    int y1 = is.readInt();
//                    int x2 = is.readInt();
//                    int y2 = is.readInt();
//                    g.drawLine(x1, y1, x2, y2);
                    for(Socket client:clientList) {
                        os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
                        os.writeUTF(test);
                        os.flush();
//                    	os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
//                      os.writeInt(x1);
//                    	os.writeInt(y1);
//                    	os.writeInt(x2);
//                    	os.writeInt(y2);
//                    	os.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException");
                 e.printStackTrace();
            } /*
            catch (ClassNotFoundException cnfe) {
                System.out.println("ClassNotFoundException");
                cnfe.printStackTrace();
            }*/
        }
    }

    public static void startChatboxService(){

        ServerSocketFactory factoryc = ServerSocketFactory.getDefault();

        try (ServerSocket serverc = factoryc.createServerSocket(2029)) {
            System.out.println("Waiting for client connection-");

            // Wait for connections.
            while (true) {
                Socket clientc = serverc.accept();
                 counter ++;
                 System.out.println("Client " + counter + ": Applying for connection!");

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