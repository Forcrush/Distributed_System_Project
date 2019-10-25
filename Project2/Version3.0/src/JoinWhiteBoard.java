/*
* @Author: Puffrora
* @Date:   2019-10-25 15:32:22
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-25 18:39:10
*/
import java.awt.Graphics;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import whiteboard.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class JoinWhiteBoard extends JFrame {
    int x1,x2,y1,y2,curchoice;
    DataInputStream is;
    DataOutputStream os;
    ObjectInputStream iss;
    WhiteBoard newPad;
    drawings nb;
    static String userName;
    static String address;
    static int port;
    Socket client;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        parseArgs(args);
        JoinWhiteBoard CP = new JoinWhiteBoard();
        CP.creat();
        CP.ShowUI();
    }
    
    private static void parseArgs(String[] args) {
        if(args.length == 3) {
            if(isNumeric(args[1]) && 1024 <= Integer.parseInt(args[1]) && Integer.parseInt(args[1]) <= 65535) {
                address = args[0];
                port = Integer.parseInt(args[1]);
                System.out.println(port);
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

    //产生一个Socket类用于连接服务器，并得到输入流
    public void creat() {
        try {
            client = new Socket(address, port);
            is = new DataInputStream(client.getInputStream());
            iss = new ObjectInputStream(client.getInputStream());
            os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException");
            // e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            // e.printStackTrace();
        }
    }
    //构造客户端界面并启动线程
    public void ShowUI() throws IOException, ClassNotFoundException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        newPad = new WhiteBoard(userName, client);
        newPad.setTitle(userName);
        newPad.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
            }
        });
        
    }

}
