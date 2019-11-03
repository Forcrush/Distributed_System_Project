package whiteboardgui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;

import boardsocket.PanelServer;

import java.net.*;
import java.util.ArrayList;

public class ServerPanel extends JFrame implements ActionListener, Runnable{

    private Container container = getContentPane();
    private JList clientList;
    private InetAddress inet = null;
    private ArrayList<User> users = new ArrayList<>(10);
    private ListData model = new ListData(users);
    private int port;
    private JPanel serverPanel = new JPanel();

    public ServerPanel(int port) {
        this.port = port;
        this.run();
        PanelServer panServer = new PanelServer(model, port);
        panServer.start();
    }

    public void run() {
        // set the port for the server;
        try {
            this.inet = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // set the server;

        // set the users list.
        this.users.clear();
        // set as the system maximum screen
        this.setBasicEvent();
        // make the screen the maximum screen base on the system
        this.setScreen();
        this.setPanel();
        this.setVisible(true);
    }
    
    private void setBasicEvent() {
        String depict = "Server Controller";
        setTitle(depict);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        try {
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    int response = JOptionPane.showConfirmDialog(null,
                            "Are you sure to close ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        System.exit(1);
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unkown error occurs", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        this.setIconImage(Toolkit.getDefaultToolkit().createImage(
                "src/img/logo.gif"));

    }

    private void setScreen() {
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width, height;
        width = screen.width;
        height = screen.height;
        if (width < 1024 || height < 680) {
            this.setSize(width, height);
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {          
                this.setSize(320, 600);
            this.setLocationRelativeTo(null);
        }
    }

    private void setPanel() {
        serverPanel.setLayout(new BorderLayout());
        JPanel serverInfo = new JPanel();
        JLabel serverText = new JLabel("new server has been started !<br>");
        String text = "<html><br><b>IP:</b>" + this.inet.getHostAddress() + "<br><b>Port:</b>" +  this.port + "<br></html>";
        serverText.setText(text);
        serverInfo.add(serverText, BorderLayout.CENTER);
        serverInfo.setLayout(new GridLayout(1, 0, 15, 15));
        serverInfo.setBounds(new Rectangle(0, 0, 120, 180));
        serverInfo.setBorder(new TitledBorder(null, "Server Details", TitledBorder.LEFT, TitledBorder.TOP));
        serverPanel.add(serverInfo, BorderLayout.WEST);

        JPanel clientInfo = new JPanel();
        clientList = new JList(model);
        clientInfo.setLayout(new GridLayout(1, 0, 15, 15));
        clientInfo.setBounds(new Rectangle(0, 0, 120, 180));
        clientInfo.setBorder(new TitledBorder(null, "Client List", TitledBorder.LEFT, TitledBorder.TOP));
        clientList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        clientInfo.add(clientList);

        JPanel serverOper = new JPanel();
        JButton kick = new JButton("kick out");
        JButton fresh = new JButton("update");
        kick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.removeElement(clientList.getSelectedIndex());
            }
        });
        fresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                model.refresh();
            }
        });

        clientInfo.add(new JScrollPane(clientList), BorderLayout.CENTER);
        serverOper.add(kick, BorderLayout.WEST);
        serverOper.add(fresh, BorderLayout.EAST);

        serverPanel.add(clientInfo, BorderLayout.CENTER);
        serverPanel.add(serverOper, BorderLayout.SOUTH);
        container.add(serverPanel);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
