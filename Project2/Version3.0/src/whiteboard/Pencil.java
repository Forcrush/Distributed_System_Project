/*
* @Author: Puffrora
* @Date:   2019-10-25 14:56:25
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-25 15:24:21
*/
package whiteboard;
import java.awt.*;
import javax.swing.*;

import java.awt.Graphics;

public class Pencil extends drawings {
    void draw(Graphics2D g2d) {
        g2d.setPaint(new Color(R, G, B));
        g2d.setStroke(new BasicStroke(stroke,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g2d.drawLine(x1, y1, x2, y2);
    }
}