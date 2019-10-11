import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import whiteboard.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class cli extends JFrame implements Runnable{
    int x1,x2,y1,y2,curchoice;
    DataInputStream is;
    ObjectInputStream iss;
    Graphics g;
    WhiteBoard newPad;
    WhiteBoard.drawings nb;

    public static void main(String args[]) {
        cli CP = new cli();
        CP.creat();
        CP.ShowUI();
    }
    //产生一个Socket类用于连接服务器，并得到输入流
    public void creat() {
        try {
            Socket client =new Socket("localhost", 9090);
            is = new DataInputStream(client.getInputStream());
            iss = new ObjectInputStream(client.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //构造客户端界面并启动线程
    public void ShowUI() {
        try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        newPad = new WhiteBoard("Client");
        newPad.setTitle("Client Side A");
        newPad.setSize(1800, 200);
        newPad.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        g = newPad.getGraphics();
        while (true) {
            try {
//                    nb = (WhiteBoard.drawings)iss.readObject();
//                    System.out.println("Get suc");
                    
                    x1=is.readInt();
                    y1=is.readInt();
                    x2=is.readInt();
                    y2=is.readInt();
                    g.drawLine(x1, y1, x2, y2);
                    
            } catch (IOException e) {
            	e.printStackTrace();
            }
//        catch (ClassNotFoundException ee) {
//            ee.printStackTrace();
//        }
        }

    }
    //将is输入流终中的坐标得到，并根据坐标信息画出相应的线段。
    @Override
    public void run() {
        while (true) {
            try {
//                    nb = (WhiteBoard.drawings)iss.readObject();
//                    System.out.println("Get suc");
                    
                    x1=is.readInt();
                    y1=is.readInt();
                    x2=is.readInt();
                    y2=is.readInt();
                    g.drawLine(x1, y1, x2, y2);
                    
            } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (ClassNotFoundException ee) {
//            ee.printStackTrace();
//        }
        }
    }

}