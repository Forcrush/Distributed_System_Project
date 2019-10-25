/*
* @Author: Puffrora
* @Date:   2019-10-25 14:56:25
* @Last Modified by:   Puffrora
* @Last Modified time: 2019-10-25 15:25:57
*/
package whiteboard;
import java.awt.*;
import javax.swing.*;

import java.awt.Graphics;

public class Circle extends drawings {
    void draw(Graphics2D g2d) {
        g2d.setPaint(new Color(R, G, B));
        g2d.setStroke(new BasicStroke(stroke));
        g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2),
                Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
                Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
    }
}