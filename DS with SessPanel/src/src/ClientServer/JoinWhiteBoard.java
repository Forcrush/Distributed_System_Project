package ClientServer;
import java.awt.Graphics;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import whiteboard.*;
import java.awt.*;
import java.awt.event.*;

import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class JoinWhiteBoard extends JFrame {
	public final static int MIN_PORT = 0;
	public final static int MAX_PORT = 65535;
    public static int port = 9090;
    public static String address = "localhost";
    public static final String TYPE = "client";
    public static String userName = "Client";
    DataInputStream is;
    DataOutputStream os;
    ObjectInputStream iss;
    Graphics g;
    static WhiteBoard newPad;
    Socket client;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
    	parseArgs(args);
        JoinWhiteBoard CP = new JoinWhiteBoard();
        CP.creat();
    
		CP.ShowUI();
		newPad.receiveData();
	
		System.out.println("Class not found");
			
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
	
    //äº§ç”Ÿä¸€ä¸ªSocketç±»ç”¨äºŽè¿žæŽ¥æœ�åŠ¡å™¨ï¼Œå¹¶å¾—åˆ°è¾“å…¥æµ�
    public void creat() {
        try {
            client =new Socket(address, port);
//            is = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            is = new DataInputStream(client.getInputStream());
            iss = new ObjectInputStream(client.getInputStream());
            os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            Thread t = new Thread(() -> sendtosessPanel());
            t.start();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //æž„é€ å®¢æˆ·ç«¯ç•Œé�¢å¹¶å�¯åŠ¨çº¿ç¨‹
    public void ShowUI() throws IOException, ClassNotFoundException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        newPad = new WhiteBoard(TYPE, userName, client);
        newPad.setTitle(userName);
        newPad.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {

                    System.exit(0);
                }
            });
        g = newPad.getGraphics();
    }
    
    public void sendtosessPanel() {
    	try (Socket socket = new Socket("localhost", 2129)) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String status;
            output.writeUTF("u:"+userName);
            System.out.println("Data sent to Server--> ");
            output.flush();

            // waiting for a short time, quiting quickly can lead to msg lost
            while(true) {
	            if (input.available() > 0) {
	                status = input.readUTF();
	                
			            if(status.equals("Q:")) {
			            	JOptionPane.showConfirmDialog(null,"You have been denied!", "Admin Message", 
			                		JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE); 
			              
			            	System.exit(0);
			            	break;
			            }
			            
			            if(status.contentEquals("G:")) {
			            	break;
			            }
			            
			        }
	        }
            
          if (socket != null) {
                try {
                	System.out.println("Socket closed");
                    socket.close();
                }
                catch (IOException e) {
                    System.out.println("IO Excpetion");
                    
                }
          }
            
        }catch (UnknownHostException e) {
            System.out.println("UnknownHostException");
         }catch (IOException e) {
        	System.out.println("IOException");
            
        }
    	
    }
}