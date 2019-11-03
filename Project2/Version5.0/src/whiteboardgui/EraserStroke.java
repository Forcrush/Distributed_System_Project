package whiteboardgui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EraserStroke extends JPanel implements MouseListener,
        ActionListener, ChangeListener {

    private static final long serialVersionUID = 1L;
    private PaintCanvas area;
    JLabel showVal;

    public EraserStroke(PaintCanvas area) {
        JPanel wholePanel = new JPanel(null);
        wholePanel.setLayout(new GridLayout(2, 1));
        int PanelHeight = 50;
        int PanelWidth = 100;
        wholePanel.setPreferredSize(new Dimension(PanelWidth, PanelHeight));
        wholePanel.setLayout(new GridLayout(1, 0, 15, 15));
        wholePanel.setBounds(new Rectangle(0, 0, 100, 160));
        wholePanel.setBorder(new TitledBorder(null, "Eraser Stroke",
                TitledBorder.LEFT, TitledBorder.TOP));

        JSlider slider = new JSlider (JSlider.HORIZONTAL,5,40,5);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.setBounds(0, 0, 100, 50);
        slider.setToolTipText(Long.toString(slider.getValue()));
        SliderListener listener = new SliderListener(slider, area);
        slider.addChangeListener(listener);
        wholePanel.add(slider);
        add(wholePanel);
    }

    class SliderListener implements ChangeListener {
        private JSlider slider;
        private PaintCanvas area;

        public SliderListener(JSlider slider, PaintCanvas area) {
            super();
            this.slider = slider;
            this.area = area;
        }

        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == slider) {
                area.setEraserStroke(new BasicStroke((float) slider.getValue()));
                area.setEraserSize(slider.getValue());
                slider.setToolTipText(Long.toString(slider.getValue()));
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

   }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub

    }

}
