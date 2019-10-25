/*
* @Author: Puffrora
* @Date:   2019-10-25 14:56:25
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-25 15:23:30
*/
package whiteboard;
import java.awt.*;
import javax.swing.*;
import java.io.*;

import java.awt.Graphics;

public class drawings implements Serializable {
    public int x1, y1, x2, y2;
    int R, G, B;
    float stroke;
    int type;
    String s1;
    String s2;
    void draw(Graphics2D g2d) {};
}