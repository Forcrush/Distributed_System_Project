/*
 * @Author: Puffrora
 * @Date:   2019-09-13 12:36:07
 * @Last Modified by:   Puffrora
 * @Last Modified time: 2019-10-24 11:54:27
 */
package whiteboard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ClientServer.CreateWhiteBoard;

import java.io.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



import message.MsgOperation;
import whiteboard.WhiteBoard.Circle;
import whiteboard.WhiteBoard.Line;
import whiteboard.WhiteBoard.Oval;
import whiteboard.WhiteBoard.Pencil;
import whiteboard.WhiteBoard.Rect;
import whiteboard.WhiteBoard.RoundRect;
import whiteboard.WhiteBoard.Rubber;
import whiteboard.WhiteBoard.Word;
import whiteboard.WhiteBoard.drawings;
import whiteboard.WhiteBoard.fillCircle;
import whiteboard.WhiteBoard.fillOval;
import whiteboard.WhiteBoard.fillRect;
import whiteboard.WhiteBoard.fillRoundRect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.net.ServerSocketFactory;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WhiteBoard extends JFrame
{
	CreateWhiteBoard serverPad = new CreateWhiteBoard();
    private JButton choices[];
    private String names[] = {
        "New", "Open", "Save", "Pencil", "Line", "Rect", "fRect", "Oval", "fOval", "Circle", 
        "fCircle", "RoundRect", "frRect", "Rubber", "Color", "Stroke", "Word", "ChatBox"};
    private String styleNames[] = {
        " 宋体 ", " 隶书 ", " Times New Roman ", " Serif ", 
        " Monospaced ", " SonsSerif ", " Garamond "
    };
    private Icon items[];
    private String tipText[] = {
        "Draw a new picture",
        "Open a saved picture",
        "Save current drawing",
        "Draw at will",
        "Draw a straight line",
        "Draw a rectangle",
        "Fill a ractangle",
        "Draw an oval",
        "Fill an oval",
        "Draw a circle",
        "Fill a circle",
        "Draw a round rectangle",
        "Fill a round rectangle",
        "Erase at will",
        "Choose current drawing color",
        "Set current drawing stroke",
        "Write down what u want",
        "Open the ChatBox"
    };
    private Color color = Color.black;
    private JLabel statusBar;
    public DrawPanel drawingArea;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private int width = 750, height = 500;
    private volatile int currentChoice = 3;
    private float stroke = 1.0f;
    int R, G, B;
    int genre1, genre2;
    int index = 0;
    String styleCur;
    JToolBar buttonPanel;
    JCheckBox bold, italic;
    JComboBox<String> styles;
    ArrayList<drawings> iArray = new ArrayList<drawings>();

    DataOutputStream os;
    DataInputStream is;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int number = 0;
    Socket client;
    private static String userName;
    private static String type;
    

    static int clientPos = 0;

    volatile drawings newOb = null;

    public WhiteBoard(String type, String userName) {
        super("Distributed WhiteBoard");
        this.userName = userName;
        this.type= type;
        createWB();
    }

    public WhiteBoard(String type, String userName, Socket client) throws IOException, ClassNotFoundException {
        super("Distributed WhiteBoard");
        os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        oos = new ObjectOutputStream(client.getOutputStream());
        is = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        this.userName = userName;
        this.type= type;
        this.client = client;
        createWB();
        receiveData();
    }

    public void createWB() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenuItem newItem = new JMenuItem("New");
        newItem.setMnemonic('N');
        newItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        newFile();
                    }
                });
        fileMenu.add(newItem);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setMnemonic('S');
        saveItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        saveFile();
                    }
                });
        fileMenu.add(saveItem);
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.setMnemonic('L');
        loadItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loadFile();
                    }
                });
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('X');
        exitItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
        fileMenu.add(exitItem);
        bar.add(fileMenu);
        JMenu colorMenu = new JMenu("Color");
        colorMenu.setMnemonic('C');
        JMenuItem colorItem = new JMenuItem("Choose Color");
        colorItem.setMnemonic('O');
        colorItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        chooseColor();
                    }
                });
        colorMenu.add(colorItem);
        bar.add(colorMenu);
        JMenu strokeMenu = new JMenu("Stroke");
        strokeMenu.setMnemonic('S');
        JMenuItem strokeItem = new JMenuItem("Set Stroke");
        strokeItem.setMnemonic('K');
        strokeItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setStroke();
                    }
                });
        strokeMenu.add(strokeItem);
        bar.add(strokeMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutItem = new JMenuItem("About this Whiteboard!");
        aboutItem.setMnemonic('A');
        aboutItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null,
                                "Distributed Whiteboard",
                                "Puffrora",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });
        helpMenu.add(aboutItem);
        bar.add(helpMenu);
        items = new ImageIcon[names.length];
        drawingArea = new DrawPanel();
        choices = new JButton[names.length];
        buttonPanel = new JToolBar(JToolBar.HORIZONTAL);

        ButtonHandlerx handlerx = new ButtonHandlerx();
        ButtonHandlery handlery = new ButtonHandlery();

        for (int i = 0; i < choices.length; i++) {
            // 如果在jbuilder下运行本程序，则应该用这条语句导入图片
            // items[i] = new ImageIcon(WhiteBoard.class.getResource(names[i] +".gif"));
            // 默认的在jdk或者jcreator下运行，用此语句导入图片
            items[i] = new ImageIcon("pic/" + names[i] + ".png");
            choices[i] = new JButton("", items[i]);
            choices[i].setToolTipText(tipText[i]);
            buttonPanel.add(choices[i]);
        }

        for (int i = 3; i < choices.length - 3; i++) {
            choices[i].addActionListener(handlery);
        }

        for (int i = 1; i < 5; i++) {
            choices[choices.length - i].addActionListener(handlerx);
        }

        choices[0].addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        newFile();
                    }
                });
        choices[1].addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loadFile();
                    }
                });
        choices[2].addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        saveFile();
                    }
                });

        styles = new JComboBox<String>(styleNames);
        styles.setMaximumRowCount(8);
        styles.addItemListener(
                new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        styleCur = styleNames[styles.getSelectedIndex()];
                    }
                });
        bold = new JCheckBox("B");
        italic = new JCheckBox("I");
        checkBoxHandler cHandler = new checkBoxHandler();
        bold.addItemListener(cHandler);
        italic.addItemListener(cHandler);
        JPanel wordPanel = new JPanel();
        buttonPanel.add(bold);
        buttonPanel.add(italic);
        buttonPanel.add(styles);
        styles.setMinimumSize(new Dimension(80, 26));
        styles.setMaximumSize(new Dimension(120, 26));

        Container cont = getContentPane();
        super.setJMenuBar(bar);
        cont.add(buttonPanel, BorderLayout.NORTH);
        cont.add(drawingArea, BorderLayout.CENTER);
        statusBar = new JLabel();
        cont.add(statusBar, BorderLayout.SOUTH);

        createNewItem();
        setSize(width, height);
        setVisible(true);

        
    }

    public class ButtonHandlery implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int j = 3; j < choices.length - 3; j++) {
                if (e.getSource() == choices[j]) {
                    currentChoice = j;
                    createNewItem();
                    repaint();
                }
            }
        }
    }

    public class ButtonHandlerx implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == choices[choices.length - 4]) {
                chooseColor();
            }
            if (e.getSource() == choices[choices.length - 3]) {
                setStroke();
            }
            if (e.getSource() == choices[choices.length - 2]) {
                JOptionPane.showMessageDialog(null,
                        "Please click the drawing pad to choose the word input position",
                        "Hint", JOptionPane.INFORMATION_MESSAGE);
                currentChoice = 14;
                createNewItem();
                repaint();
            }
            if (e.getSource() == choices[choices.length - 1]) {
                // setStroke();
                Thread ct = new Thread(() -> createChatbox());
                ct.start();
                
            }
        }
    }

    class mouseEvent1 extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            statusBar.setText("     Mouse Pressed @:[" + e.getX() +
                    ", " + e.getY() + "]");
            iArray.get(index).x1 = iArray.get(index).x2 = e.getX();
            iArray.get(index).y1 = iArray.get(index).y2 = e.getY();
            if (currentChoice == 3 || currentChoice == 13) {
                iArray.get(index).x1 = iArray.get(index).x2 = e.getX();
                iArray.get(index).y1 = iArray.get(index).y2 = e.getY();
                index++;
                createNewItem();
            }
            if (currentChoice == 14) {
                iArray.get(index).x1 = e.getX();
                iArray.get(index).y1 = e.getY();
                String input;
                input = JOptionPane.showInputDialog(
                        "Please input the text you want!");
                iArray.get(index).s1 = input;
                iArray.get(index).x2 = genre1;
                iArray.get(index).y2 = genre2;
                iArray.get(index).s2 = styleCur;
                index++;
                createNewItem();
                drawingArea.repaint();
            }
        }
        public void mouseReleased(MouseEvent e) {
            statusBar.setText("     Mouse Released @:[" + e.getX() +
                    ", " + e.getY() + "]");
            if (currentChoice == 3 || currentChoice == 13) {
                iArray.get(index).x1 = e.getX();
                iArray.get(index).y1 = e.getY();
            }
            iArray.get(index).x2 = e.getX();
            iArray.get(index).y2 = e.getY();
            newOb = iArray.get(index);
            repaint();
            index++;
            createNewItem();
        }
        public void mouseEntered(MouseEvent e) {
            statusBar.setText("     Mouse Entered @:[" + e.getX() +
                    ", " + e.getY() + "]");
        }
        public void mouseExited(MouseEvent e) {
            statusBar.setText("     Mouse Exited @:[" + e.getX() +
                    ", " + e.getY() + "]");
        }

    }

    class mouseEvent2 implements MouseMotionListener{
        ObjectOutputStream oss;
        public void mouseDragged(MouseEvent e) {
            statusBar.setText("     Mouse Dragged @:[" + e.getX() +
                    ", " + e.getY() + "]");

            if (currentChoice == 3 || currentChoice == 13) {
                iArray.get(index - 1).x1 = iArray.get(index).x2 = iArray.get(index).x1 = e.getX();
                iArray.get(index - 1).y1 = iArray.get(index).y2 = iArray.get(index).y1 = e.getY();
                newOb = iArray.get(index);
                index++;
                createNewItem();
            } else {
                iArray.get(index).x2 = e.getX();
                iArray.get(index).y2 = e.getY();
            }
            try {
                if (!type.equals("server")){
                    sendData(newOb);
                }
                else {
                	serverPad.drawToClient(newOb);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println("IOException");
            }
            repaint();
        }
        public void mouseMoved(MouseEvent e) {
            statusBar.setText("     Mouse Moved @:[" + e.getX() +
                    ", " + e.getY() + "]");
        }

    }

    public void sendData(drawings newOb) throws IOException {
    	if (newOb != null) {
    		String data = newOb.x1 + "," + newOb.y1 + "," + newOb.x2 + "," + newOb.y2 + "," + newOb.R + "," + newOb.G + "," + newOb.B + "," + newOb.stroke + "," + newOb.type + "," + newOb.s1 + "," + newOb.s2;
        	os.writeUTF(data);
        	os.flush();
        	System.out.println("Data sent from white board " + data);
//            os.writeInt(newOb.x1);
//            os.writeInt(newOb.y1);
//            os.writeInt(newOb.x2);
//            os.writeInt(newOb.y2);
//            os.flush();
    	}
    }
    
    public void receiveData() throws IOException, ClassNotFoundException {
        while (true) {
        	String test = is.readUTF();
        	String[] data = test.split(",");
        	if(data.length == 11) {
        		drawings newDraw = new drawings();
            	newDraw.x1 = Integer.parseInt(data[0]);
            	newDraw.y1 = Integer.parseInt(data[1]);
            	newDraw.x2 = Integer.parseInt(data[2]);
            	newDraw.y2 = Integer.parseInt(data[3]);
            	newDraw.R = Integer.parseInt(data[4]);
            	newDraw.G = Integer.parseInt(data[5]);
            	newDraw.B = Integer.parseInt(data[6]);
            	newDraw.stroke = Float.parseFloat(data[7]);
            	newDraw.type = Integer.parseInt(data[8]);
            	newDraw.s1 = data[9];
            	newDraw.s1 = data[10];
            	createNewItemInClient(newDraw);
        	}
//            int x1 = is.readInt();
//            int y1 = is.readInt();
//            int x2 = is.readInt();
//            int y2 = is.readInt();
//            Graphics drawer = this.getGraphics();
//            drawer.drawLine(x1, y1, x2, y2);
        }
    }

    public void createChatbox(){

        JFrame frame = new JFrame("ChatBox (Client Side)");
        final JTextArea input = new JTextArea();
        input.setBounds(30, 20, 720, 60);
        final JTextArea output = new JTextArea();
        output.setBounds(30, 160, 720, 360);

        JScrollPane scroll1 = new JScrollPane();
        scroll1.setBounds(30, 20, 720, 60);
        JScrollPane scroll2 = new JScrollPane();
        scroll2.setBounds(30, 160, 720, 360);

        JButton add = new JButton("Send");
        add.setBounds(330, 110, 100, 30);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String res = sendMsg("localhost", 2029, userName, input.getText(), clientPos);
                String[] content = res.split("( )*::( )*");
                if (content.length > 1 && content[1].length() != 0) {
                    clientPos = Integer.parseInt(content[0]);
                    try {
                        Thread.currentThread().sleep(250);
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    output.append(content[1]);
                }
                // output.setBackground(Color.cyan);
                input.setText("");
            }
        });

        frame.add(add);
        frame.add(scroll1);
        frame.add(scroll2);
        scroll1.setViewportView(input);
        scroll2.setViewportView(output);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        while (true){
            try {
                Thread.currentThread().sleep(600);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            String res = sendMsg("localhost", 2029, userName, "", clientPos);
            String[] content = res.split("( )*::( )*");
            if (content.length > 1 && content[1].length() != 0) {
                clientPos = Integer.parseInt(content[0]);
                try {
                    Thread.currentThread().sleep(150);
                }
                catch (InterruptedException iee) {
                    iee.printStackTrace();
                }
                output.append(content[1]);
            }
        }
        // testConnect(ip, port);

    }

    public static String sendMsg(String ip, int port, String Username, String sendData, int clientPos){
        String message = "";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
        try (Socket socket = new Socket(ip, port)) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());


            output.writeUTF(String.valueOf(clientPos) + " : " + timestamp + " " + Username + " : " + sendData);
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();

            // waiting for a short time, quiting quickly can lead to msg lost
            try {
                Thread.currentThread().sleep(200);
            }
            catch (InterruptedException e) {
                // e.printStackTrace();
                return "InterruptedException occurs\n";
            }
            if (input.available() > 0) {
                message = input.readUTF();
            }
            else return "Msg LOST !" + "\n";

            if (socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    // e.printStackTrace();
                    return "IOException occurs\n";
                }
            }

        }
        catch (UnknownHostException e) {
            // e.printStackTrace();
            return "UnknownHostException occurs";
        }
        catch (IOException e) {
            // e.printStackTrace();
            return "IOException occurs";
        }
        return message;
    }


    private class checkBoxHandler implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == bold) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    genre1 = Font.BOLD;
                } else {
                    genre1 = Font.PLAIN;
                }
            }
            if (e.getSource() == italic) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    genre2 = Font.ITALIC;
                } else {
                    genre2 = Font.PLAIN;
                }
            }
        }
    }

    class DrawPanel extends JPanel {
        public DrawPanel() {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            setBackground(Color.white);
            addMouseListener(new mouseEvent1());
            addMouseMotionListener(new mouseEvent2());

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int j = 0;
            while (j < index) {
                draw(g2d, iArray.get(j));
                j++;
            }
        }

        void draw(Graphics2D g2d, drawings i) {
            i.draw(g2d);
        }
    }

    void createNewItem() {
        if (currentChoice == 14) {
            drawingArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        } else {
            drawingArea.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
        switch (currentChoice) {
            case 3:
                iArray.add(index, new Pencil());
                break;
            case 4:
                iArray.add(index, new Line());
                break;
            case 5:
                iArray.add(index, new Rect());
                break;
            case 6:
                iArray.add(index, new fillRect());
                break;
            case 7:
                iArray.add(index, new Oval());
                break;
            case 8:
                iArray.add(index, new fillOval());
                break;
            case 9:
                iArray.add(index, new Circle());
                break;
            case 10:
                iArray.add(index, new fillCircle());
                break;
            case 11:
                iArray.add(index, new RoundRect());
                break;
            case 12:
                iArray.add(index, new fillRoundRect());
                break;
            case 13:
                iArray.add(index, new Rubber());
                break;
            case 14:
                iArray.add(index, new Word());
                break;
        }
        iArray.get(index).type = currentChoice;
//        System.out.println("Set index: " + index + " choice as " + currentChoice);
        iArray.get(index).R = R;
        iArray.get(index).G = G;
        iArray.get(index).B = B;
        iArray.get(index).stroke = stroke;
    }

    public void createNewItemInClient(drawings infoOb) {

        iArray.get(index).x1 = infoOb.x1;
        iArray.get(index).y1 = infoOb.y1;
        iArray.get(index).x2 = infoOb.x2;
        iArray.get(index).y2 = infoOb.y2;
        currentChoice = infoOb.type;
        // ===== testing clause
        if (index > 1){
            if (currentChoice != iArray.get(index - 1).type) {
//                System.out.println("index:"+index+" currentChoice "+currentChoice+" index-1~choice: " + iArray.get(index - 1).type);
            }
        }
        // ===== 
        R = infoOb.R;
        G = infoOb.G;
        B = infoOb.B;
        stroke = infoOb.stroke;
        //System.out.println();
        index ++;
        repaint();
        createNewItem();
        //System.out.println(index+" "+iArray.get(index).x1+" "+iArray.get(index).y1+" "+iArray.get(index).x2+" "+iArray.get(index).y2);
        
    }

    public void chooseColor() {
        color = JColorChooser.showDialog(WhiteBoard.this,
                "Choose color", color);
        R = color.getRed();
        G = color.getGreen();
        B = color.getBlue();
        iArray.get(index).R = R;
        iArray.get(index).G = G;
        iArray.get(index).B = B;
    }

    public void setStroke() {
        String input;
        input = JOptionPane.showInputDialog(
                "Please input the size of stroke!");
        stroke = Float.parseFloat(input);
        iArray.get(index).stroke = stroke;
    }

    public void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File fileName = fileChooser.getSelectedFile();
        fileName.canWrite();
        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(fileChooser, "Invalid File Name",
                    "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                fileName.delete();
                FileOutputStream fos = new FileOutputStream(fileName);
                output = new ObjectOutputStream(fos);
                drawings record;
                output.writeInt(index);
                for (int i = 0; i < index; i++) {
                    drawings p = iArray.get(i);
                    output.writeObject(p);
                    output.flush();
                }
                output.close();
                fos.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }

    public void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File fileName = fileChooser.getSelectedFile();
        fileName.canRead();
        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(fileChooser, "Invalid File Name",
                    "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                input = new ObjectInputStream(fis);
                drawings inputRecord;
                int countNumber = 0;
                countNumber = input.readInt();
                for (index = 0; index < countNumber; index++) {
                    inputRecord = (drawings) input.readObject();
                    iArray.add(index, inputRecord);
                }
                createNewItem();
                input.close();
                repaint();
            } catch (EOFException endofFileException) {
                JOptionPane.showMessageDialog(this, "no more record in file",
                        "class not found", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException classNotFoundException) {
                JOptionPane.showMessageDialog(this, "Unable to Create Object",
                        "end of file", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "error during read from file",
                        "read Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void newFile() {
        index = 0;
        currentChoice = 3;
        color = Color.black;
        stroke = 1.0f;
        createNewItem();
        repaint();
    }

    public class drawings implements Serializable
    {
        public int x1, y1, x2, y2;
        public int R, G, B;
        public float stroke;
        public int type;
        public String s1;
        public String s2;
        void draw(Graphics2D g2d) {};
    }

    class Pencil extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }


    class Line extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }


    class Rect extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

    class fillRect extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.fillRect(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

    class Oval extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

    class fillOval extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.fillOval(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

    class Circle extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2),
                    Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
                    Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
        }
    }

    class fillCircle extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.fillOval(Math.min(x1, x2), Math.min(y1, y2),
                    Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
                    Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
        }
    }


    class RoundRect extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.drawRoundRect(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2),
                    50, 35);
        }
    }


    class fillRoundRect extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.fillRoundRect(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2),
                    50, 35);
        }
    }

    class Rubber extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(255, 255, 255));
            g2d.setStroke(new BasicStroke(stroke + 3,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    class Word extends drawings
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setFont(new Font(s2, x2 + y2, ((int) stroke) * 16));
            if (s1 != null) {
                g2d.drawString(s1, x1, y1);
            }
        }
    }
}