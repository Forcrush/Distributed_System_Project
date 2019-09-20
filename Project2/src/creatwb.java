/*
 * @Author: Puffrora
 * @Date:   2019-09-20 15:35:02
 * @Last Modified by:   Puffrora
 * @Last Modified time: 2019-09-20 15:39:46
 */
import whiteboard.*;


import java.awt.event.*;
import javax.swing.*;

public class creatwb {
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        WhiteBoard newPad = new WhiteBoard();
        newPad.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
    }
}