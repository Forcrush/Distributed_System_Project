/*
* @Author: Puffrora
* @Date:   2019-09-20 15:35:02
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-01 15:25:26
*/
import whiteboard.*;

import java.awt.*;
import java.awt.event.*;
import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class chy {
    static int port = 9090;
    static int counter = 0;
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        WhiteBoard newPad = new WhiteBoard("Server");
        newPad.setTitle("Server Side");
        newPad.setSize(500, 500);
        newPad.addWindowListener(
            new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}