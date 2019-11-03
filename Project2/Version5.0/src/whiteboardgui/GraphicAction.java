package whiteboardgui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import javax.swing.JOptionPane;

public class GraphicAction implements DrawStyle {

	Point startPoint;
	Point endPoint;

	public GraphicAction(Point endPoint, Point startPoint) {
		super();
		this.endPoint = endPoint;
		this.startPoint = startPoint;
	}

	public static void drawGraphics(int type, String text, Point startPoint,
			Point endPoint, Color pencilColor, Stroke pencilStroke,  Color eraserColor, Stroke eraserStroke,Graphics2D g) {
		switch (type) {
		case ERASER:
			drawPen(startPoint, endPoint, eraserColor,eraserStroke, g);
			break;
		case LINE:
			drawLine(startPoint, endPoint, pencilColor, pencilStroke, g);
			break;
		case RECTANGLE:
			drawRect(startPoint, endPoint, pencilColor, pencilStroke, g);
			break;
		case ROUND_RECT:
			drawRoundRect(startPoint, endPoint, pencilColor, pencilStroke, g);
			break;
		case OVAL:
			drawOval(startPoint, endPoint, pencilColor, pencilStroke, g);
			break;
		case CURVE:
			drawCurve(startPoint, endPoint, pencilColor, pencilStroke, g);
			break;
		case FREE_DRAW:
			drawPen(startPoint, endPoint, pencilColor, pencilStroke, g);
			break;
		case TEXT:
			drawString(text, startPoint, pencilColor, pencilStroke, g);
			break;

		}
	}

	private static void drawRoundRect(Point startPoint, Point endPoint,
			Color pencilColor, Stroke pencilStroke, Graphics2D g) {
		g.setColor(pencilColor);
		g.setStroke(pencilStroke);
		int w, h, width, height;
		w = endPoint.x - startPoint.x;
		h = endPoint.y - startPoint.y;
		width = Math.abs(w);
		height = Math.abs(h);
		int x, y;
		if (startPoint.x > endPoint.x) {
			x = endPoint.x;
		} else {
			x = startPoint.x;
		}
		if (startPoint.y > endPoint.y) {
			y = endPoint.y;
		} else {
			y = startPoint.y;
		}
		g.drawRoundRect(x, y, width, height, 15, 15);
	}

	private static void drawOval(Point startPoint, Point endPoint,
			Color pencilColor, Stroke pencilStroke, Graphics2D g) {
		g.setColor(pencilColor);
		g.setStroke(pencilStroke);
		int width, height;

		width = Math.abs(endPoint.x - startPoint.x);
		height = Math.abs(endPoint.y - startPoint.y);
		int x, y;
		if (startPoint.x > endPoint.x) {
			x = endPoint.x;
		} else {
			x = startPoint.x;
		}
		if (startPoint.y > endPoint.y) {
			y = endPoint.y;
		} else {
			y = startPoint.y;
		}
		g.drawOval(x, y, width, height);
	}

	private static void drawRect(Point startPoint, Point endPoint,
			Color pencilColor, Stroke pencilStroke, Graphics2D g) {
		g.setColor(pencilColor);
		g.setStroke(pencilStroke);
		int w, h, width, height;
		w = endPoint.x - startPoint.x;
		h = endPoint.y - startPoint.y;
		width = Math.abs(w);
		height = Math.abs(h);
		int x, y;
		if (startPoint.x > endPoint.x) {
			x = endPoint.x;
		} else {
			x = startPoint.x;
		}
		if (startPoint.y > endPoint.y) {
			y = endPoint.y;
		} else {
			y = startPoint.y;
		}
		g.drawRect(x, y, width, height);
	}

	private static void drawLine(Point startPoint, Point endPoint,
			Color pencilColor, Stroke pencilStroke, Graphics2D g) {
		g.setColor(pencilColor);
		g.setStroke(pencilStroke);
		g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

	private static void drawCurve(Point startPoint, Point endPoint,
			Color pencilColor, Stroke pencilStroke, Graphics2D g) {
		g.setColor(pencilColor);
		g.setStroke(pencilStroke);
		g.drawArc(startPoint.x, startPoint.y,
				Math.abs(endPoint.x - startPoint.x),
				Math.abs(endPoint.y - startPoint.y), -20, 180);
	}

	private static void drawPen(Point startPoint, Point endPoint,
			Color pencilColor, Stroke pencilStroke, Graphics2D g) {
		g.setColor(pencilColor);
		g.setStroke(pencilStroke);
		g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

	private static void drawString(String string, Point startPoint, Color pencilColor, Stroke pencilStroke, Graphics2D g) {
		g.setColor(pencilColor);
		g.setStroke(pencilStroke);
		g.drawString(string, startPoint.x, startPoint.y);
	}
}