package boardsocket;

import java.io.*;
import java.net.*;
import java.awt.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import whiteboardgui.DrawStyle;
import whiteboardgui.PaintCanvas;
import whiteboardgui.WhiteBoardFrame;

import dataSource.*;

public class PanelClient extends Thread implements DrawStyle {
    private String userName;
    private InetAddress address=null;
    private Socket socket;
    private BufferedReader in; 
    public BufferedWriter out; 

    // private WhiteBoardFrame whiteBoardFrame
    private PaintCanvas canvas = PaintCanvas.getInstance();

    public int id = 0;
    private Point pencilPoint = null; 
    private String dataBuffer;
    private String[] ss; 
    
    private Color pencilColor;
	private Color eraserColor;
	private Stroke pencilStroke;
	private Stroke eraserStroke;
	private String text;
	private String fonttype;
	private int bolder;
	private int fontsize;
	private int type;

    private static PanelClient panelClient = new PanelClient();

    public static PanelClient getInstance() {
        return panelClient;
    }

    public static void main(String[] args) {
        PanelClient client = PanelClient.getInstance();
    }

    public PanelClient() {
        this.initialization();
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(userName);
            out.newLine();
            out.flush();

            id = Integer.parseInt(in.readLine());

            WhiteBoardFrame frame = WhiteBoardFrame.getInstance(false);
            frame.setVisible(true);
            this.start();

        } catch(SocketException e){
            JOptionPane.showMessageDialog(null,
                    "Failed to join in whiteboard", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IOException");
            System.exit(1);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Unkown Exception£¡");
            System.exit(1);
        }

    }

    private void initialization() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String serverIP;
        String portString;
        int inputPort = 0;
        boolean legalPort = false;

        do {
            try {
                JTextField nameText= new JTextField();
                JTextField IPText= new JTextField();
                JTextField portText= new JTextField();
                nameText.setText("Guest" + (int)(Math.random()*100));
                IPText.setText(InetAddress.getLocalHost().getHostAddress());
                portText.setText("2029");
                Object complexMsg[] = {new JLabel("Please input your name"), nameText,
                        new JLabel("Please input IP of server"), IPText, new JLabel("Please input the port:"), portText};
                JOptionPane optionPane = new JOptionPane();
                optionPane.setMessage(complexMsg);
                optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                optionPane.setOptionType(optionPane.OK_CANCEL_OPTION);
                JDialog dialog = optionPane.createDialog(null, "Width 100");
                dialog.setVisible(true);
                int value = ((Integer)optionPane.getValue()).intValue();
                if (value == JOptionPane.OK_OPTION) {
                    userName = nameText.getText();
                    if (userName.length() == 0){
                        throw (new NameNullException());
                    }
                    serverIP = IPText.getText();
                    if (serverIP.length() == 0){
                        serverIP = InetAddress.getLocalHost().getHostAddress();
                    }
                    address = InetAddress.getByName(serverIP);

                    if (portText.getText().length() == 0){
                        portString = "2029";
                    } else{
                        portString = portText.getText();
                    }
                    inputPort = Integer.parseInt(portString);
                    if ((inputPort < 1024) || (inputPort > 65535)) {
                        JOptionPane.showMessageDialog(null,
                                "Port number should be in 1024 ~ 65535", "warning", JOptionPane.ERROR_MESSAGE);
                    } else {
                        socket = new Socket(address,inputPort);
                        legalPort = true;
                    }
                } else{
                    System.exit(0);
                }
            } catch (NameNullException e)  {
                JOptionPane.showMessageDialog(null,
                        "Please input a vaild user name", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Please input a vaild number", "Error", JOptionPane.ERROR_MESSAGE);
             }catch (HeadlessException e1) { 
                 JOptionPane.showMessageDialog(null,
                         "HeadlessException", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (UnknownHostException e1) {
                JOptionPane.showMessageDialog(null,
                        "UnknownHostException", "Error", JOptionPane.ERROR_MESSAGE);
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Unkown Exception", "Error", JOptionPane.ERROR_MESSAGE);
            }       
        } while (!legalPort);

        System.out.println(inputPort);

    }

    public void run() {
        try {
            readMsg();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Reading Error £¡");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }

    }

    public void readMsg() throws IOException {

        while (true) {
            dataBuffer = in.readLine(); 
            System.out.println(dataBuffer);
            if (dataBuffer.equalsIgnoreCase("KICK")||dataBuffer.equalsIgnoreCase("quit")){
                JOptionPane.showMessageDialog(null,
                        "You have been kicked out or Some reason to quit", "warning",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0); 
                out.close();
            }
            ss = dataBuffer.split("\\.");
            if (ss.length > 4) {
                handleStartMsg();
            } else {
                handleDragMsg();
            }
        }
    }

    private void handleStartMsg() {
		int id, type, scaleX, scaleY;
		Point startPoint;
		id = Integer.parseInt(ss[0]); 
		if (id == this.id) return;
		type = Integer.parseInt(ss[1]); 
		pencilStroke = new BasicStroke((float) Integer.parseInt(ss[2]));
		eraserStroke = new BasicStroke((float) Integer.parseInt(ss[3]));
		pencilColor = ColorConvert.String2Color(ss[4]);
		eraserColor = ColorConvert.String2Color(ss[5]);
		scaleX = Integer.parseInt(ss[6]); 
		scaleY = Integer.parseInt(ss[7]);
		startPoint = new Point(scaleX, scaleY); 
		pencilPoint = startPoint; 
		text = ss[8];
		fonttype =ss[9];
		bolder = Integer.parseInt(ss[10]);
		fontsize = Integer.parseInt(ss[11]);
		canvas.getArr().addData(new DataSource(id, type, new Point(scaleX, scaleY), new Point(scaleX, scaleY),
                pencilColor, eraserColor,pencilStroke, eraserStroke, text, fonttype, bolder, fontsize));
	}

	private void handleDragMsg() {
		int id, scaleX, scaleY;
		Point endPoint;
		if (dataBuffer.equals("x.x.x")) return;
		id = Integer.parseInt(ss[0]);
		if (id == this.id) return;
		scaleX = Integer.parseInt(ss[1]);
		scaleY = Integer.parseInt(ss[2]);
		endPoint = new Point(scaleX, scaleY);

		int i;
		for (i = canvas.getArr().array.size() - 1; id != canvas.getArr().array.get(i).getId(); i--);
		if (canvas.getArr().array.get(i).getPaintType() == FREE_DRAW) {
			canvas.getArr().addData(
					new DataSource(id, type,new Point(pencilPoint), new Point(endPoint),
                            pencilColor,eraserColor,pencilStroke,eraserStroke,text, fonttype,bolder,fontsize));
			pencilPoint = endPoint;
		} else if (canvas.getArr().array.get(i).getPaintType() == ERASER){
			canvas.getArr().addData(
					new DataSource(id, type,new Point(pencilPoint), new Point(endPoint),
                            pencilColor,eraserColor,pencilStroke,eraserStroke,text, fonttype,bolder,fontsize));
			pencilPoint = endPoint;
		} else {
			canvas.getArr().array.get(i).setEndPoint(new Point(endPoint));
		}
		canvas.repaint(); 
	}

    public Socket getSocket() {
        // TODO Auto-generated method stub
        return this.socket;
    }

}

class NameNullException extends Exception {

}
