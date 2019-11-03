package whiteboardgui;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class ColorPanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;

	private int rColor[] = { 0x00, 0xA9, 0x2F, 0x8B, 0x00, 0x00, 0x8A, 0x7F,
			0xFF, 0x00, 0x00, 0xFF, 0xFF, 0x8F, 0xFF, 0x00, 0xAD, 0x80, 0xFF,
			0xFF, 0x80, 0x80, 0x8B, 0x87, 0x00, 0x40, 0xFF, 0x9A };

	private int gColor[] = { 0x00, 0xA9, 0x4F, 0x00, 0xFF, 0x00, 0x2B, 0xFF,
			0x7F, 0x64, 0x00, 0x8c, 0x14, 0xBC, 0xFF, 0x80, 0xFF, 0x80, 0x00,
			0xA5, 0x80, 0x00, 0x45, 0xCE, 0xFF, 0xE0, 0xFF, 0xCD };

	private int bColor[] = { 0x00, 0xA9, 0x4F, 0x00, 0xFF, 0xFF, 0xE2, 0x00,
			0x50, 0x00, 0x8B, 0x00, 0x93, 0x8B, 0xFF, 0x00, 0x2D, 0x80, 0x00,
			0x00, 0x00, 0x80, 0x13, 0xEB, 0x7F, 0xD0, 0x00, 0x32 };

	private ColorGrid colorGrid[] = new ColorGrid[28];
	private ColorShower colorShower = new ColorShower();

	private Color foreColor = Color.BLACK;// forecolor
	private Color backColor = Color.WHITE;// backcolor
	private PaintCanvas area = null;

	public ColorPanel(PaintCanvas area) {
		JPanel grids = new JPanel(new GridLayout(2, 14, 1, 1));
		this.area = area;
		int i = 0;
		for (i = 0; i < colorGrid.length; i++) {
			Color color = new Color(rColor[i], gColor[i], bColor[i]);
			colorGrid[i] = new ColorGrid(color);
			grids.add(colorGrid[i]);
			colorGrid[i].addMouseListener(this);
			colorGrid[i]
					.setToolTipText("Click the left mouse button to adjust the foreground color, background color right adjustment!");
		}
		JPanel colorPanel = new JPanel(null);
		int colorPanelHeight = 40;
		int colorPanelWidth = 500;

		colorPanel.setPreferredSize(new Dimension(colorPanelWidth,
				colorPanelHeight));
		colorPanel.add(grids);
		colorPanel.add(colorShower);
		colorShower.setBounds(0, 2, colorPanelWidth, colorPanelHeight);
		grids.setBounds(colorPanelHeight + 5, 2, colorPanelWidth,
				colorPanelHeight);

		add(colorPanel);

	}

	

	private class ColorGrid extends JPanel {

		private static final long serialVersionUID = 1L;
		Color color = null;

		public ColorGrid(Color color) {
			this.color = color;
			setBorder(BorderFactory.createLoweredBevelBorder());
		}

		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(color);
			g.fillRect(2, 2, getWidth(), getHeight());
		}
	}

	private class ColorShower extends JPanel {

		private static final long serialVersionUID = 1L;

		public ColorShower() {
			setBorder(BorderFactory.createLoweredBevelBorder());
		}

		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(backColor);
			int width = getWidth();
			int height = getHeight();
			g.fill3DRect(width / 100, height / 2, width / 2, height / 2, true);
			g.setColor(foreColor);
			g.fill3DRect(width / 100, height / 20, width / 2, height / 2, true);
		}
	}

	public Color getBackColor() {
		return backColor;
	}

	public Color getForeColor() {
		return foreColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	public void setForeColor(Color foreColor) {
		this.foreColor = foreColor;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		ColorGrid grid = (ColorGrid) e.getSource();
		int clickCount = e.getClickCount();

		if (clickCount == 1) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				foreColor = grid.color;
				area.setPencilColor(foreColor);
			} else {
				backColor = grid.color;
				area.setEraserColor(backColor);
			}
		} else if (clickCount == 2) {
			Color color = JColorChooser.showDialog(grid, "Edit Color",
					grid.color);
			if (color == null)
				return;
			foreColor = color;
			area.setPencilColor(foreColor);
		} else
			return;
		this.getParent().repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
