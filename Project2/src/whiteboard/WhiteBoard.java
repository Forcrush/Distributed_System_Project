/*
 * @Author: Puffrora
 * @Date:   2019-09-13 12:36:07
 * @Last Modified by:   Puffrora
 * @Last Modified time: 2019-10-06 09:31:23
 */
package whiteboard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;

public class WhiteBoard extends JFrame
{
    private JButton choices[];
    private String names[] = {"New", "Open", "Save", "Pencil", "Line", "Rect", "Oval", "Circle", "Rubber", "Color", "Stroke", "Word"};
    private String styleNames[] = {"Garamond"};
    private Icon items[];
    private Color color = Color.black;
    private JLabel statusBar;
    public DrawPanel drawingArea;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private int width = 1800, height = 1000;
    private int currentChoice = 5;
    private float stroke = 1.0f;
    int R, G, B;
    int genre1, genre2;
    int index = 0;
    String styleCur;
    JToolBar buttonPanel;
    JCheckBox bold, italic;
    JComboBox<String> styles;
    DataOutputStream os;
    DataInputStream is;
    ObjectOutputStream oss;
    int number = 0;
    public volatile drawings newOb = null,newOb2=null;
    drawings[] iArray = new drawings[9999];
    
    private static int counter = 0;
    private static int port = 9090;

    public WhiteBoard(String name) {
        super("Distributed WhiteBoard");
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
        ButtonHandlery handlery = new ButtonHandlery();
        ButtonHandlerx handlerx = new ButtonHandlerx();
        for (int i = 0; i < choices.length; i++) {
            items[i] = new ImageIcon("pic/" + names[i] + ".png");
            choices[i] = new JButton("", items[i]);
            buttonPanel.add(choices[i]);
        }
        for (int i = 3; i < choices.length - 3; i++) {
            choices[i].addActionListener(handlery);
        }
        for (int i = 1; i < 4; i++) {
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

        if(name.equals("Server")) {
        	ServerSocketFactory factory = ServerSocketFactory.getDefault();
            try(ServerSocket server = factory.createServerSocket(port))
    		{
    			System.out.println("Waiting for client connection to port number: " + port);

    			// Wait for connections.
    			while(true)
    			{
    				Socket client = server.accept();
    				counter++;
    				System.out.println("Client "+counter+": Applying for connection! in port num: " + client.getPort());

    				// Start a new thread for a connection
    				Thread t = new Thread(() -> clientCon(client, counter));
    				t.start();
    			}

    		}
    		catch (IOException e)
    		{
    			System.out.println("Unable to setup server, try another port.");
//    			System.exit(1);
    		}
        }
    }
    void clientCon(Socket client, int number) {
        Socket clientSocket = client;
            try{
                //连接成功后得到数据输出流
                os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
//                os = new DataOutputStream(client.getOutputStream());
                oss = new ObjectOutputStream(clientSocket.getOutputStream());
                is = new DataInputStream(new BufferedInputStream(client.getInputStream()));

            }  catch (IOException e) {
                e.printStackTrace();
            }   
            //x1,y1为起始点坐标，x2,y2为终点坐标。四个点的初始值设为0

            int count = 0;
            while (true) {
                if(newOb != null) {
                    try {

                        System.out.println(newOb.x1+" "+newOb.y2+" "+newOb.x2+" "+newOb.y2);
                        System.out.println(clientSocket.getPort()+"cacacaa"+clientSocket.getLocalPort());
                        System.out.println(number);
                       
//                            oss.writeObject(newOb);

                        os.writeInt(newOb.x1);
                        System.out.println("wrote1");

                        os.writeInt(newOb.y1);
                        System.out.println("wrote2");

                        os.writeInt(newOb.x2);
                        System.out.println("wrote3");

                        os.writeInt(newOb.y2);
                        System.out.println("wrote4");
                        count+=1;
                        if(count == 20) {
                            os.flush();
                            count = 0;
                        }

                        newOb = null;
//                        int x1, x2, y1, y2;
//                        x1=is.readInt();
//                        y1=is.readInt();
//                        x2=is.readInt();
//                        y2=is.readInt();
//                        Graphics g = this.getGraphics();
//                        g.drawLine(x1, y1, x2, y2);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
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
            if (e.getSource() == choices[choices.length - 3]) {
                chooseColor();
            }
            if (e.getSource() == choices[choices.length - 2]) {
                setStroke();
            }
            if (e.getSource() == choices[choices.length - 1]) {
                JOptionPane.showMessageDialog(null,
                        "Please click the drawing pad to choose the word input position",
                        "Hint", JOptionPane.INFORMATION_MESSAGE);
                currentChoice = 14;
                createNewItem();
                repaint();
            }
        }
    }
    class mouseEvent1 extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            statusBar.setText("     Mouse Pressed @:[" + e.getX() +
                    ", " + e.getY() + "]");
            iArray[index].x1 = iArray[index].x2 = e.getX();
            iArray[index].y1 = iArray[index].y2 = e.getY();
            if (currentChoice == 3 || currentChoice == 8) {
                iArray[index].x1 = iArray[index].x2 = e.getX();
                iArray[index].y1 = iArray[index].y2 = e.getY();
                index++;
                createNewItem();
            }
            if (currentChoice == 9) {
                iArray[index].x1 = e.getX();
                iArray[index].y1 = e.getY();
                String input;
                input = JOptionPane.showInputDialog(
                        "Please input the text you want!");
                iArray[index].s1 = input;
                iArray[index].x2 = genre1;
                iArray[index].y2 = genre2;
                iArray[index].s2 = styleCur;
                index++;
                createNewItem();
                drawingArea.repaint();
            }
        }
        public void mouseReleased(MouseEvent e) {
            statusBar.setText("     Mouse Released @:[" + e.getX() +
                    ", " + e.getY() + "]");
            if (currentChoice == 3 || currentChoice == 13) {
                iArray[index].x1 = e.getX();
                iArray[index].y1 = e.getY();
            }
            iArray[index].x2 = e.getX();
            iArray[index].y2 = e.getY();
            newOb = iArray[index];
            newOb2 = iArray[index];
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
        
        //在设置监听器的同时启动监听器对象的线程
        public void mouseDragged(MouseEvent e) {
            statusBar.setText("     Mouse Dragged @:[" + e.getX() +
                    ", " + e.getY() + "]");
            //the reason this works is mousePressed is first enabled
            if (currentChoice == 3 || currentChoice == 8) {
                iArray[index - 1].x1 = iArray[index].x2 = iArray[index].x1 = e.getX();
                iArray[index - 1].y1 = iArray[index].y2 = iArray[index].y1 = e.getY();
                newOb = iArray[index];
                newOb2 = iArray[index];
                index++;
                createNewItem();
            } else {
                iArray[index].x2 = e.getX();
                iArray[index].y2 = e.getY();
            }
            repaint();
        }
        public void mouseMoved(MouseEvent e) {
            statusBar.setText("     Mouse Moved @:[" + e.getX() +
                    ", " + e.getY() + "]");
        }

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
            while (j <= index) {
                draw(g2d, iArray[j]);
                j++;
            }
        }

        void draw(Graphics2D g2d, drawings i) {
            i.draw(g2d);
        }
    }
    void createNewItem() {
        if (currentChoice == 9)
        {
            drawingArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        } else {
            drawingArea.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
        switch (currentChoice) {
            case 3:
                iArray[index] = new Pencil();
                break;
            case 4:
                iArray[index] = new Line();
                break;
            case 5:
                iArray[index] = new Rect();
                break;
            case 6:
                iArray[index] = new Oval();
                break;
            case 7:
                iArray[index] = new Circle();
                break;
            case 8:
                iArray[index] = new Rubber();
                break;
            case 9:
                iArray[index] = new Word();
                break;
        }
        iArray[index].type = currentChoice;
        iArray[index].R = R;
        iArray[index].G = G;
        iArray[index].B = B;
        iArray[index].stroke = stroke;
    }

    public void testClient() {
        System.out.println("testtesttest:  "+index);
        index++;
    }

    public void createNewItemInClient(drawings infoOb) {

        iArray[index].x1 = infoOb.x1;
        iArray[index].y1 = infoOb.y1;
        iArray[index].x2 = infoOb.x2;
        iArray[index].y2 = infoOb.y2;
        currentChoice = infoOb.type;
        R = infoOb.R;
        G = infoOb.G;
        B = infoOb.B;
        stroke = infoOb.stroke;
        //System.out.println();
        index ++;
        repaint();
        createNewItem();
        //System.out.println(index+'/'+iArray[index].x1+'/'+iArray[index].y1+'/'+iArray[index].x2+'/'+iArray[index].y2);
        
    }

    public void chooseColor() {
        color = JColorChooser.showDialog(WhiteBoard.this,
                "Choose color", color);
        R = color.getRed();
        G = color.getGreen();
        B = color.getBlue();
        iArray[index].R = R;
        iArray[index].G = G;
        iArray[index].B = B;
    }
    public void setStroke() {
        String input;
        input = JOptionPane.showInputDialog(
                "Please input the size of stroke!");
        stroke = Float.parseFloat(input);
        iArray[index].stroke = stroke;
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
                    drawings p = iArray[i];
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
                    iArray[index] = inputRecord;
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
        int R, G, B;
        float stroke;
        int type;
        String s1;
        String s2;
        void draw(Graphics2D g2d) {};
    }

    class Pencil extends drawings implements Serializable
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }


    class Line extends drawings implements Serializable
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }


    class Rect extends drawings implements Serializable
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

    class Oval extends drawings implements Serializable
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

    class Circle extends drawings implements Serializable
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setStroke(new BasicStroke(stroke));
            g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2),
                    Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
                    Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
        }
    }

    class Rubber extends drawings implements Serializable
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(255, 255, 255));
            g2d.setStroke(new BasicStroke(stroke + 3,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    class Word extends drawings implements Serializable
    {
        void draw(Graphics2D g2d) {
            g2d.setPaint(new Color(R, G, B));
            g2d.setFont(new Font(s2, x2 + y2, ((int) stroke) * 16));
            if (s1 != null) {
                g2d.drawString(s1, x1, y1);
            }
        }
    }

    public drawings getNewOb() {
        return newOb;
    }

}



