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
    
    public void creat() {
        try {
            Socket client =new Socket("localhost", 9090);
            iss = new ObjectInputStream( client.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void ShowUI() {
        try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        newPad = new WhiteBoard();
        newPad.setTitle("Client Side A");
        newPad.setSize(1800, 200);
        newPad.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        g = newPad.getGraphics();
        Thread t = new Thread(this);
        t.start();

    }
    
    @Override
    public void run() {
        while (true) {
            try {
                    nb = (WhiteBoard.drawings)iss.readObject();
                    newPad.createNewItemInClient(nb);

            } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ee) {
            ee.printStackTrace();
        }
        }
    }

}