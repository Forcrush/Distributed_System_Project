package whiteboardgui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;

import dataSource.ListData;
import dataSource.User;

import java.io.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.font.*;

import boardsocket.PanelClient;

import java.net.*;
import java.io.*;
import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;


public class WhiteBoardFrame extends JFrame implements ActionListener, Runnable {

    private Container c = getContentPane();
    // menu areas
    private String menuBar[] = { "Flie(F)", "EDIT(E)", "Help(H)" };
    private String menuItem[][] = {
            { "New(N)|78", "Open(O)|79", "Save(S)|83", "Save as(A)",
                    "Exit(X)|88" }, { "Cls(C)|67" },
            { "Help(H)|72", "About(F)|70" } };
    private JMenuItem jMenuItem[][] = new JMenuItem[3][5];
    private JMenu jMenu[];
    // panel areas
    private JPanel jPanel[] = new JPanel[6];// 0all,1tool,2function,3tool&function,
                                            // 4color panel,5server
                                            // information,6color panel
    private JLabel jLabel = new JLabel("ready");
    private int drawPanelWidth = 1000, drawPanelHeight = 500;
    private PencileStroke setPanel;
    private EraserStroke setPanel2;
    private TextPanel setPanel3;
    private PaintCanvas drawPanel;
    private allDrawPanel bigDrawPanel;
    private ColorPanel colorPanel;
    // tools areas as follows
    private String toolButtonName[] = { "Pencil", "Line", "Rect", "Oval",
            "Eraser", "Text", "Circle", "Round_rect", "Curve", "Background" };
    private JToggleButton toggleButton[];
    private Icon tool[] = new ImageIcon[10];
    private String toolFilePath[] = { "src/img/1.gif", "src/img/2.gif",
            "src/img/3.gif", "src/img/4.gif", "src/img/5.gif", "src/img/6.gif",
            "src/img/7.gif", "src/img/9.gif", "src/img/11.gif", "src/img/12.gif" };
    private final int FREE_DRAW = 0;
    private final int ERASER = 1;
    private final int RECTANGLE = 2;
    private final int OVAL = 3;
    private final int CIRCLE = 8;
    private final int LINE = 5;
    private final int POLYGON = 11;
    private final int TEXT = 7;
    private final int ROUND_RECT = 6;
    private final int CURVE = 4;
    
    private int defaultTool;
    // server area
    private JLabel serverText;
    private int isFilled;
    private WindowAdapter quit;
    private int change = 0;
    private String fileName;
    private String filePath;
    private int x2, y2;
    private int saveAs = 0;

    // text

    // all the thing for the server

    private JList userList;
    private InetAddress inet = null;
    private JPanel serverInfo;
    private JPanel userInfo;
    private JPanel serverOper;
    private ArrayList<User> users = new ArrayList<User>(30);
    private ListData model = new ListData(users);
    private Comparator cmp = Collator.getInstance(java.util.Locale.ENGLISH);
    public ServerSocket serverSocket = null;
    public int port = 0;
    public static Socket socket = null;

    public static boolean server = true;
    public static WhiteBoardFrame frame = new WhiteBoardFrame();

    public static WhiteBoardFrame getInstance(boolean server1) {
        return frame;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public void run() {
        // set the port for the server;

        try {
            this.setInet(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // set the server;

        // set the users list.
        this.users.clear();

        // set as the system maximum screen
        this.setDefault();
        // make the screen the maximum screen base on the system
        // this part for the menu bar
        // this.setMenu();
        this.setScreen();
        this.setPanel();
        // this.pack();
        this.setVisible(true);
    }

    public WhiteBoardFrame() {
        this.server = false;
        this.run();

    }

    public InetAddress getInet() {
        return inet;
    }

    public void setInet(InetAddress inet) {
        this.inet = inet;
    }

    private void setDefault() {
        if (this.server) {
            this.fileName = "Server";
        } else {
            this.fileName = "no_name";
        }
        setTitle(fileName);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        try {
            this.addWindowListener(quit = new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    int response = JOptionPane.showConfirmDialog(null,
                            "Do you want to continue?", "Confirm",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.NO_OPTION) {

                    } else if (response == JOptionPane.YES_OPTION) {
                        try {
                            if (frame.server) {
                                PanelClient.getInstance().out
                                        .write("serverquit");
                                PanelClient.getInstance().out.newLine();
                                PanelClient.getInstance().out.flush();
                            } else {
                                PanelClient.getInstance().out.write("quit");
                                PanelClient.getInstance().out.newLine();
                                PanelClient.getInstance().out.flush();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.exit(1);
                    } else if (response == JOptionPane.CLOSED_OPTION) {

                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unkown Error", "warning",
                    JOptionPane.ERROR_MESSAGE);
        }
        this.defaultTool = 1;
        this.isFilled = 0;
        // set the icon
        this.drawPanelWidth = 900;
        this.drawPanelHeight = 500;
        this.setIconImage(Toolkit.getDefaultToolkit().createImage(
                "src/img/logo.gif"));

    }

    private void setMenu() {

        JMenuBar barTemp = new JMenuBar();
        jMenu = new JMenu[menuBar.length];
        // read the max menu information
        // add all the menu to JMenubar
        for (int i = 0; i < menuBar.length; i++) {
            jMenu[i] = new JMenu(menuBar[i]);
            jMenu[i].setMnemonic(menuBar[i].split("\\(")[1].charAt(0));
            // to find the key
            barTemp.add(jMenu[i]);
        }
        // add all menu item to menu
        for (int i = 0; i < menuItem.length; i++) {
            for (int j = 0; j < menuItem[i].length; j++) {
                jMenu[i].addSeparator();
                jMenuItem[i][j] = new JMenuItem(menuItem[i][j].split("\\|")[0]);
                if (menuItem[i][j].split("\\|").length != 1)
                    jMenuItem[i][j].setAccelerator(KeyStroke.getKeyStroke(
                            Integer.parseInt(menuItem[i][j].split("\\|")[1]),
                            ActionEvent.CTRL_MASK));
                jMenuItem[i][j].addActionListener(this);
                jMenuItem[i][j].setMnemonic(menuItem[i][j].split("\\(")[1]
                        .charAt(0));
                jMenu[i].add(jMenuItem[i][j]);
            }
        }
        // add the menu to the frame
        this.setJMenuBar(barTemp);
    }

    private void setScreen() {
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = 0;
        int height = 0;
        width = screen.width;
        height = screen.height;
        if (width < 1024 || height < 680) {
            this.setSize(width, height);
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            if (this.server) {
                this.setSize(1024, 680);
            } else {
                this.setSize(900, 650);
            }
            this.setLocationRelativeTo(null);
        }
    }

    public void setPanel() {
        if (!this.server) {
            this.setMenu();
        }
        // layout - border layout
        c.setLayout(new BorderLayout());
        for (int i = 0; i < 6; i++) {
            jPanel[i] = new JPanel();
        }
        // settoolbox to panel 1
        this.setToolBox();

        // set darw box to panel 4
        this.setDarw();
        // set the the big draw panel to panel 4
        this.setBigDrawPanel();
        // set server panel to panel 5
        // set function box to panel 2
        this.setColorBox();
        this.setFunctionBox();
        this.setServerPanel();
        this.setLabel();

        // jPanel[3].setLayout(new BoxLayout(jPanel[3], BoxLayout.Y_AXIS));
        // jPanel[3].setLayout(new BoxLayout(jPanel[3], BoxLayout.X_AXIS));

        if (this.server == false) {
            jPanel[3].setLayout(new FlowLayout(FlowLayout.LEFT));
            jPanel[3].add(jPanel[2], FlowLayout.LEFT);
            jPanel[3].add(jPanel[1], FlowLayout.LEFT);

            JPanel temp = new JPanel();
            temp.add(drawPanel);
            temp.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jPanel[0].setLayout(new BorderLayout());
            jPanel[0].add(temp, BorderLayout.CENTER);
            jPanel[0].add(jPanel[3], BorderLayout.NORTH);

            jPanel[0].add(jPanel[4], BorderLayout.SOUTH);
        } else {
            jPanel[0].add(jPanel[5], BorderLayout.CENTER);
        }

        c.add(jPanel[0], BorderLayout.CENTER);
        c.add(jLabel, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    private void setToolBox() {

        ButtonGroup buttonGroup = new ButtonGroup();

        // Tool bar which for keep all the tool
        JToolBar jToolBar = new JToolBar("tool box", JToolBar.HORIZONTAL);

        // add the picture or text JToggleButton and add the
        // action listener to it and add all the tool to the button group
        toggleButton = new JToggleButton[toolButtonName.length];
        for (int i = 0; i < toolButtonName.length; i++) {
            tool[i] = new ImageIcon(toolFilePath[i]);
            // jToggleButton[i] = new JToggleButton(ButtonName[i]);
            toggleButton[i] = new JToggleButton(tool[i]);
            toggleButton[i].addActionListener(this);
            toggleButton[i].setFocusable(false);
            buttonGroup.add(toggleButton[i]);
            jToolBar.add(toggleButton[i]);
            toggleButton[i].setToolTipText(toolButtonName[i]);
        }

        // default selected one
        toggleButton[defaultTool].setSelected(true);

        // this set the layout for the gridlayout
        // jToolBar.setLayout(new GridLayout(0, 2, 15, 15));
        jToolBar.setLayout(new GridLayout(1, 0, 15, 15));
        jToolBar.setBounds(new Rectangle(0, 0, 100, 160));
        jToolBar.setBorder(new TitledBorder(null, "Tool Box",
                TitledBorder.LEFT, TitledBorder.TOP));

        toggleButton[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(FREE_DRAW);
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });

        toggleButton[1].setToolTipText("To draw " + "|, " + "-- or "
                + "/, please hold " + "'shift' button.");
        toggleButton[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(LINE);
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });
        toggleButton[2].setToolTipText("To draw a square, please hold "
                + "'shift' button.");
        toggleButton[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(RECTANGLE);
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });
        toggleButton[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(OVAL);
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });
        toggleButton[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(ERASER);
                setPanel2.setVisible(true);
                setPanel.setVisible(false);
                setPanel3.setVisible(false);

            }
        });
        toggleButton[5].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(TEXT);
                setPanel2.setVisible(false);
                setPanel.setVisible(false);
                setPanel3.setVisible(true);
                String string = JOptionPane
                        .showInputDialog("Please input the text you want!");
                drawPanel.setText(string);
            }
        });
        toggleButton[6].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(OVAL);
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });
        toggleButton[7].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setBackground(colorPanel.getBackColor());
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });
        toggleButton[8].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(ROUND_RECT);
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });
        toggleButton[9].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.setType(CURVE);
                setPanel2.setVisible(false);
                setPanel.setVisible(true);
                setPanel3.setVisible(false);
            }
        });
        this.jPanel[1].add(jToolBar);

    }

    private void setDarw() {

        drawPanel = PaintCanvas.getInstance();

    }

    private void setBigDrawPanel() {
        bigDrawPanel = new allDrawPanel();
        bigDrawPanel.setLayout(null);
        JPanel temp = new JPanel();
        // bigDrawPanel.add(drawPanel);
        bigDrawPanel.add(temp);

        temp.add(drawPanel);
        temp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(
                172, 168, 153)));
        drawPanel
                .setBounds(new Rectangle(2, 2, drawPanelWidth, drawPanelHeight));
        drawPanel.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();

                jLabel.setText("drawing " + x2 + "," + y2 + ".....");
            }

            public void mouseMoved(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();

                jLabel.setText(x2 + "," + y2);
            }

        });

        bigDrawPanel.setBorder(BorderFactory
                .createBevelBorder(BevelBorder.LOWERED));
        bigDrawPanel.setBackground(new Color(128, 128, 128));

    }

    private void setColorBox() {
        colorPanel = new ColorPanel(drawPanel);
        jPanel[4].setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel[4].add(colorPanel);
    }

    private void setFunctionBox() {
        setPanel = new PencileStroke(drawPanel);
        jPanel[2].add(setPanel);
        setPanel2 = new EraserStroke(drawPanel);
        jPanel[2].add(setPanel2);
        setPanel3 = new TextPanel(drawPanel);
        jPanel[2].add(setPanel3);
        setPanel2.setVisible(false);
        setPanel.setVisible(true);
        setPanel3.setVisible(false);
    }

    private void setServerPanel() {

        jPanel[5].setLayout(new BorderLayout());
        serverInfo = new JPanel();
        this.serverText = new JLabel("new server has been start on port <br>");
        String text = "<html><b>IP:</b>";
        text += this.inet.getHostAddress();
        text += "<br><b>Port:</b>";
        text += this.getPort();
        text += "<br></html>";
        this.serverText.setText(text);
        // serverText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        serverInfo.add(serverText);
        serverInfo.setLayout(new GridLayout(1, 0, 15, 15));
        serverInfo.setBounds(new Rectangle(0, 0, 100, 160));
        serverInfo.setBorder(new TitledBorder(null, "Server Info",
                TitledBorder.LEFT, TitledBorder.TOP));
        jPanel[5].add(serverInfo, BorderLayout.NORTH);

        userInfo = new JPanel();
        userList = new JList(model);
        userInfo.setLayout(new GridLayout(1, 0, 15, 15));
        userInfo.setBounds(new Rectangle(0, 0, 100, 160));
        userInfo.setBorder(new TitledBorder(null, "User List",
                TitledBorder.LEFT, TitledBorder.TOP));
        // userList.add(1, index)
        userList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        userInfo.add(userList);

        serverOper = new JPanel();
        JButton kick = new JButton("kick out");
        JButton fresh = new JButton("refresh");
        kick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.removeElement(userList.getSelectedIndex());
            }
        });
        fresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                model.refresh();
            }
        });

        userInfo.add(new JScrollPane(userList), BorderLayout.CENTER);
        serverOper.add(kick, BorderLayout.NORTH);
        serverOper.add(fresh, BorderLayout.SOUTH);

        jPanel[5].add(userInfo, BorderLayout.CENTER);
        jPanel[5].add(serverOper, BorderLayout.SOUTH);
    }

    private void setLabel() {
        jLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    }

    public void save() {
        if (this.fileName == "no_name" || this.saveAs == 1) {
            FileDialog savedialog = new FileDialog(this,
                    "please give a file path", FileDialog.SAVE);
            savedialog.setVisible(true);
            if (savedialog.getFile() != null) {
                try {
                    filePath = savedialog.getDirectory();
                    fileName = savedialog.getFile();
                    FileOutputStream out1 = new FileOutputStream(filePath
                            + fileName + ".jpg");
                    // JPEGImageEncoder encoder =
                    // JPEGCodec.createJPEGEncoder(out);
                    // JPEGEncodeParam param = encoder
                    // .getDefaultJPEGEncodeParam(image);
                    // param.setQuality(1.0f, false);
                    // encoder.setJPEGEncodeParam(param);
                    // encoder.encode(image);
                    // out.close();
                    Component component = drawPanel;
                    BufferedImage bi = (BufferedImage) component.createImage(
                            component.getWidth(), component.getHeight());
                    component.paint(bi.getGraphics());
                    // BufferedOutputStream out = new BufferedOutputStream(new
                    // FileOutputStream("c:/x1.jpg"));

                    //BufferedOutputStream out = new BufferedOutputStream(out1);
                    //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                    //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
                    //param.setQuality(1.0f, false);
                    //encoder.setJPEGEncodeParam(param);
                    //encoder.encode(bi);

                    String dstName = filePath + fileName + ".jpg";
                    String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
                    ImageIO.write(bi, /*"GIF"*/ formatName /* format desired */ , new File(dstName) /* target */ );

                    //out.flush();
                    //out.close();
                    this.setTitle(fileName);
                    this.saveAs = 0;
                } catch (Exception EE) {
                }
            } else {
                this.saveAs = 0;
                return;
            }
        } else {
            try {
                FileOutputStream out2 = new FileOutputStream(filePath
                        + fileName + ".jpg");
                Component component = drawPanel;
                BufferedImage bi = (BufferedImage) component.createImage(
                        component.getWidth(), component.getHeight());
                component.paint(bi.getGraphics());
                // BufferedOutputStream out = new BufferedOutputStream(new
                // FileOutputStream("c:/x1.jpg"));

                //BufferedOutputStream out = new BufferedOutputStream(out1);
                //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
                //param.setQuality(1.0f, false);
                //encoder.setJPEGEncodeParam(param);
                //encoder.encode(bi);

                String dstName = filePath + fileName + ".jpg";
                String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
                ImageIO.write(bi, /*"GIF"*/ formatName /* format desired */ , new File(dstName) /* target */ );

                //out.flush();
                //out.close();
            } catch (Exception EE) {
            }
        }
        // drawPanel.filename = fileDialog.getDirectory() +
        // fileDialog.getFile();
    }

    public void exit() {
        this.quit.windowClosing(null);

    }

    public void open() {
        JFileChooser fileDialog = new JFileChooser(".");
        fileDialog.setMultiSelectionEnabled(false);
        fileDialog.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                String name = f.getName().toLowerCase();
                if (name.endsWith(".jpg") || name.endsWith(".bmp")
                        || name.endsWith(".png") || name.endsWith(".gif")
                        || name.endsWith(".ico") || name.endsWith(".jpeg"))
                    return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "Image File";
            }
        });

        fileDialog.setAcceptAllFileFilterUsed(false);
        fileDialog.showOpenDialog(this);
        // FileDialog fileDialog = new FileDialog(new Frame(),
        // "please select a file", FileDialog.LOAD);
        // fileDialog.setVisible(true);
        File file = fileDialog.getSelectedFile();
        if (file == null)
            return;
        fileName = file.getName();
        filePath = file.getPath();
        this.setTitle(fileName);
        bigDrawPanel.removeAll();
        drawPanel = null;
        drawPanel = new PaintCanvas();
        bigDrawPanel.add(drawPanel);
        drawPanel
                .setBounds(new Rectangle(2, 2, drawPanelWidth, drawPanelHeight));
        // BufferedImage bufImg = new BufferedImage(drawPanelWidth,
        // drawPanelHeight,
        // BufferedImage.TYPE_3BYTE_BGR);
        // Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
        // ImageIcon icon = new ImageIcon(filePath+fileName);
        // g2d_bufImg.drawImage(icon.getImage(), 0, 0, drawPanel);
        BufferedImage bufImg_data = new BufferedImage(drawPanelWidth,
                drawPanelHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) bufImg_data.getGraphics();
        // g2d_bufImg_data.drawImage(bufImg, 0, 0, drawPanel);
        Image image = Toolkit.getDefaultToolkit().getImage(filePath + fileName);
        int x = 0;
        // drawPanel.setImage(image);
        // g.drawImage(image, 0, 0, (x=image.getWidth(this)),
        // image.getHeight(this),
        // drawPanel);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jMenuItem[0][0]
                || e.getSource() == jMenuItem[1][0]) {
            drawPanel.repaint();
        } else if (e.getSource() == jMenuItem[0][1]) {
            this.open();
        } else if (e.getSource() == jMenuItem[0][2]) {
            this.save();
        } else if (e.getSource() == jMenuItem[0][3]) {
            this.saveAs = 1;
            this.save();
        } else if (e.getSource() == jMenuItem[0][4]) {
            this.exit();

        }

    }

    public class allDrawPanel extends JPanel implements MouseListener,
            MouseMotionListener {
        public int x, y;
        float data[] = { 2 };
        public JPanel smallPanel1 = new JPanel(), smallPanel2 = new JPanel(),
                smallPanel3 = new JPanel();

        public allDrawPanel() {
            this.setLayout(null);
            this.add(smallPanel1);
            this.add(smallPanel2);
            this.add(smallPanel3);

            smallPanel1.setBounds(new Rectangle(drawPanelWidth + 3,
                    drawPanelHeight + 3, 5, 5));
            smallPanel1.setBackground(new Color(0, 0, 0));
            smallPanel2.setBounds(new Rectangle(drawPanelWidth + 3,
                    drawPanelHeight / 2, 5, 5));
            smallPanel2.setBackground(new Color(0, 0, 0));
            smallPanel3.setBounds(new Rectangle(drawPanelWidth / 2,
                    drawPanelHeight + 3, 5, 5));
            smallPanel3.setBackground(new Color(0, 0, 0));
            smallPanel1.addMouseListener(this);
            smallPanel1.addMouseMotionListener(this);
            smallPanel2.addMouseListener(this);
            smallPanel2.addMouseMotionListener(this);
            smallPanel3.addMouseListener(this);
            smallPanel3.addMouseMotionListener(this);
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
            drawPanelWidth = x;
            drawPanelHeight = y;

            smallPanel1.setLocation(drawPanelWidth + 3, drawPanelHeight + 3);
            smallPanel2
                    .setLocation(drawPanelWidth + 3, drawPanelHeight / 2 + 3);
            smallPanel3
                    .setLocation(drawPanelWidth / 2 + 3, drawPanelHeight + 3);
            drawPanel.setSize(x, y);
            // drawPanel.resize();
            repaint();
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            if (e.getSource() == smallPanel2) {
                x = e.getX() + drawPanelWidth;
                y = drawPanelHeight;
            } else if (e.getSource() == smallPanel3) {
                x = drawPanelWidth;
                y = e.getY() + drawPanelHeight;
            } else {
                x = e.getX() + drawPanelWidth;
                y = e.getY() + drawPanelHeight;
            }
            repaint();
            jLabel.setText(x + "," + y);
        }

        public void mouseMoved(MouseEvent e) {

        }

        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            super.paint(g2d);

            g2d.setPaint(new Color(128, 128, 128));
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_MITER, 10, data, 0));
            g2d.draw(new Rectangle2D.Double(-1, -1, x + 3, y + 3));
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int inputPort) {

        this.port = inputPort;
    }

}
