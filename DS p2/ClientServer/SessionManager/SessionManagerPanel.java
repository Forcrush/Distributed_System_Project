package ClientServer.SessionManager;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;


public class SessionManagerPanel extends JFrame{
    /**
     *
     */
    private Container c = getContentPane();

    private static final long serialVersionUID = 1L;
    private int port;
    private JPanel sessionPanel=new JPanel();
    private JPanel sessionInfo;
    private JLabel sessionText;
    private JPanel userInfo;
    @SuppressWarnings("rawtypes")
    public JList userList;
    private JPanel sessButtPnel;
    private static SessionList sessList;

    public SessionManagerPanel(int port) {
        this.port=port;
        String text = null;
        sessList = new SessionList(new ArrayList<User>());
        setTitle("Session Manager");

        //Closing window event
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        try {
            this.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {

                    //aesthetics
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    int response = JOptionPane.showConfirmDialog(null,
                            "Are you sure to exit?", "Exit",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.NO_OPTION) {
                    } else if (response == JOptionPane.YES_OPTION) {
                        System.exit(1);
                    } else if (response == JOptionPane.CLOSED_OPTION) {

                    }
                }
            });
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unkown Error", "warning",
                    JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Tile and window closing set");

        //THis makes it adaptable to different monitor sizes
        //THIS IS COPIED-OPTIONAL
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = 0;
        int height = 0;
        width = screen.width;
        height = screen.height;
        if (width < 1024 || height < 680) {
            this.setSize(width, height);
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            this.setSize(600, 600);
            this.setLocationRelativeTo(null);
        }
        //THIS IS COPIED- SME of this is OPTIONAL

        //Setting up the list
        sessionPanel.setLayout(new BorderLayout());
        sessionInfo = new JPanel();

        //This is not displayed-get rid of
        this.sessionText = new JLabel("new server has been start on port <br>");
        text = "Port:";
        text += this.port;

        this.sessionText.setText(text);
        sessionInfo.add(sessionText);
        sessionInfo.setLayout(new GridLayout(1, 0, 15, 15));
        sessionInfo.setBounds(new Rectangle(0, 0, 100, 160));
        sessionInfo.setBorder(new TitledBorder(null, "Server Info",
                TitledBorder.LEFT, TitledBorder.TOP));
        sessionPanel.add(sessionInfo, BorderLayout.NORTH);

        userInfo = new JPanel();
        userList = new JList(sessList);
        userInfo.setLayout(new GridLayout(1, 0, 15, 15));
        userInfo.setBounds(new Rectangle(0, 0, 100, 160));
        userInfo.setBorder(new TitledBorder(null, "User List",
                TitledBorder.LEFT, TitledBorder.TOP));
        userList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        userInfo.add(userList);

        sessButtPnel = new JPanel();
        JButton kick = new JButton("Kick");
        JButton fresh = new JButton("Refresh");

        //Kick function
        kick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sessList.removeElement(userList.getSelectedIndex());
                System.out.println("Print something");
            }
        });

        //Refresh function
        fresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sessList.refresh();
                System.out.println("Print something");
            }
        });

        userInfo.add(new JScrollPane(userList), BorderLayout.CENTER);
        sessButtPnel.add(kick, BorderLayout.WEST);
        sessButtPnel.add(fresh, BorderLayout.EAST);

        sessionPanel.add(userInfo, BorderLayout.CENTER);
        sessionPanel.add(sessButtPnel, BorderLayout.SOUTH);
        c.add(sessionPanel);



    }

    public void update(User newUser) {
        sessList.add(newUser);
    }

}

