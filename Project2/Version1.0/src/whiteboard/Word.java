/*
* @Author: Puffrora
* @Date:   2019-10-25 14:56:25
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-25 15:23:35
*/
package whiteboard;
import java.awt.*;
import javax.swing.*;

import java.awt.Graphics;

public class Word extends drawings {
    void draw(Graphics2D g2d) {
        g2d.setPaint(new Color(R, G, B));
        g2d.setFont(new Font(s2, x2 + y2, ((int) stroke) * 16));
        if (s1 != null) {
            g2d.drawString(s1, x1, y1);
        }
    }
}