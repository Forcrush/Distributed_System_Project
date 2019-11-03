package whiteboardgui;

import java.awt.BasicStroke;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TextPanel extends JPanel implements ItemListener {

    
    private PaintCanvas drawPanel;
    private Choice fonttype;
    private Choice bolder;
    private Choice fontsize;

    public TextPanel(PaintCanvas area) {
        this.drawPanel=area;
        JPanel wholePanel = new JPanel(null);
        wholePanel.setLayout(new GridLayout(2, 1));
        int PanelHeight = 50;
        int PanelWidth = 350;
        wholePanel.setPreferredSize(new Dimension(PanelWidth, PanelHeight));
        wholePanel.setLayout(new GridLayout(1, 0, 15, 15));
        wholePanel.setBounds(new Rectangle(0, 0, 100, 160));
        wholePanel.setBorder(new TitledBorder(null, "Text",
                TitledBorder.LEFT, TitledBorder.TOP));
        fontsize = new Choice();
        {
            for (int i = 12; i <= 64; i += 2) {
                String size = String.valueOf(i);
                fontsize.add(size);
            }
        }
        fonttype = new Choice();
        GraphicsEnvironment fonts = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        String ss[] = fonts.getAvailableFontFamilyNames();
        {
            for (int j = 0; j < ss.length; j++)
                fonttype.add(ss[j]);
        }

        String bold[] = { "Font.PLAIN", "Font.ITALIC", "Font.BOLD" };
        // Font.CENTER_BASELINE,Font.CENTER_BASELINE,Font.ROMAN_BASELINE,Font.TRUETYPE_FONT};
        bolder = new Choice();
        {
            for (int i = 0; i < bold.length; i++) {
                bolder.add(bold[i]);
            }
        }

        fonttype.addItemListener(this);
        bolder.addItemListener(this);
        fontsize.addItemListener(this);
      
        wholePanel.add(fonttype);
        wholePanel.add(bolder);
        wholePanel.add(fontsize);
        
        add(wholePanel);
    }
    

    public void itemStateChanged(ItemEvent e) {
        int i = 0;

        // int i = 0;
        if (e.getSource() == fonttype) {
            // if (fonttype.getSelectedIndex() == i) {
            i = fonttype.getSelectedIndex();
            //drawPanel.setFontType(fonttype.getItem(i));
            // }
        }
        if (e.getSource() == bolder) {
            // if (bolder.getSelectedIndex() == i) {
            i = bolder.getSelectedIndex();
            if (i == 0) {
                drawPanel.setBolder(Font.PLAIN);
            } else if (i == 1) {
                drawPanel.setBolder(Font.ITALIC);
            } else if (i == 2) {
                drawPanel.setBolder(Font.BOLD);
            }
            // }
        }
        if (e.getSource() == fontsize) {
            // if (fontsize.getSelectedIndex() == i) {
            i = fontsize.getSelectedIndex();
            //drawPanel.setFontSize(Integer.valueOf(fontsize.getItem(i)));
            // }
        }
    }
}
