/*
* @Author: Puffrora
* @Date:   2019-09-20 15:35:02
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-01 15:25:26
*/
import whiteboard.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class chy {
    public static void main(String args[]) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }
    WhiteBoard newPad = new WhiteBoard();
    newPad.setTitle("Server Side");
    newPad.setSize(1800, 200);
    newPad.addWindowListener(
        new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}