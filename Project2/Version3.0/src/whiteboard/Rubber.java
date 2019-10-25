/*
* @Author: Puffrora
* @Date:   2019-10-25 14:56:25
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-25 15:23:38
*/
package whiteboard;
import java.awt.*;
import javax.swing.*;

import java.awt.Graphics;

public class Rubber extends drawings {
    void draw(Graphics2D g2d) {
        g2d.setPaint(new Color(255, 255, 255));
        g2d.setStroke(new BasicStroke(stroke + 3,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g2d.drawLine(x1, y1, x2, y2);
    }
}