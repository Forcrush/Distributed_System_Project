package ClientServer;


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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
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
	private JList userList;
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
        this.setSize(400,600);
        
        
         //THIS IS COPIED- SME of this is OPTIONAL
            
        //Setting up the list
        sessionPanel.setLayout(new BorderLayout());
        sessionInfo = new JPanel();
        
        //This is not displayed-get rid of
        this.sessionText = new JLabel();
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
        
        
        //Kick function
        kick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sessList.removeElement(userList.getSelectedIndex());
                /*try  {
                	Socket socket = new Socket("localhost", 2129);
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    String status;
                    int index = sessList.getElementAt(userList.getSelectedIndex()).getId();
                    output.writeUTF("K:"+index);
                    output.flush();
                    if (socket != null) {
                        try {
                            socket.close();
                        }
                        catch (IOException e1) {
                            System.out.println("IO Excpetion");
                            
                        }
                    }
                }catch(UnknownHostException e1) {
                	System.out.println("UnknownHostException");
                }catch(IOException e1) {
                	System.out.println("IOException");
                }*/
               
            }
        });
    
        userInfo.add(new JScrollPane(userList), BorderLayout.CENTER);
        sessButtPnel.add(kick, BorderLayout.CENTER);

        sessionPanel.add(userInfo, BorderLayout.CENTER);
        sessionPanel.add(sessButtPnel, BorderLayout.SOUTH);
        c.add(sessionPanel);
        
        
		
	}
	
    public void update(User newUser) {
    	sessList.add(newUser);
    }

}

