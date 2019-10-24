package ClientServer;
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
    Graphics g;
    WhiteBoard newPad;
    WhiteBoard.drawings nb;
    String userName = "Client";
    Socket client;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        JoinWhiteBoard CP = new JoinWhiteBoard();
        CP.creat();
        CP.ShowUI();
    }
    //产生一个Socket类用于连接服务器，并得到输入流
    public void creat() {
        try {
            client =new Socket("localhost", 9090);
//            is = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            is = new DataInputStream(client.getInputStream());
            iss = new ObjectInputStream(client.getInputStream());
            os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        g = newPad.getGraphics();
        }

    }