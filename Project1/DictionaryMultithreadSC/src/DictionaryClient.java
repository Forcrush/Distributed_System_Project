import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import java.awt.event.*;

public class DictionaryClient {

    // IP and port
    static String ip = "";
    static String portString = "";
    static int port = 0;
    public static void main(String[] args) {
        // server-address and port
        try {
            ip = args[0];
            portString = args[1];
            port = Integer.parseInt(portString);
        }
        catch (ArrayIndexOutOfBoundsException ae) {
            // ae.printStackTrace();
            System.out.println("ArrayIndexOutOfBoundsException occurs\nPlease ensure input args in legal form");
            return;
        }
        catch (NumberFormatException ne) {
            // ne.printStackTrace();
            System.out.println("NumberFormatException occurs\nPlease ensure input args in legal form");
            return;
        }

        JFrame frame = new JFrame("Demo");
        final JTextArea input = new JTextArea();
        input.setBounds(30, 20, 720, 100);
        final JTextArea output = new JTextArea();
        output.setBounds(30, 200, 720, 320);

        JButton query = new JButton("QUERY");
        query.setBounds(80, 150, 100, 30);
        JButton add = new JButton("ADD");
        add.setBounds(330, 150, 100, 30);
        JButton delete = new JButton("DELETE");
        delete.setBounds(590, 150, 100, 30);

        query.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                output.append(sendWord(ip, port, "query::" + input.getText()) + '\n');
                // output.setBackground(Color.cyan);
                input.setText("");
            }
        });

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                output.append(sendWord(ip, port, "add::" + input.getText())+'\n');
                // output.setBackground(Color.cyan);
                input.setText("");
            }
        });

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                output.append(sendWord(ip, port, "delete::" + input.getText())+'\n');
                // output.setBackground(Color.cyan);
                input.setText("");
            }
        });
        frame.add(query);
        frame.add(add);
        frame.add(delete);
        frame.add(input);
        frame.add(output);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
        // testConnect(ip, port);

    }

    // test if the connection is established, can be executed before query or add or delete

    public static void testConnect(String ip, int port){
        try (Socket socket = new Socket(ip, port)) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeUTF("Hello World !");
            output.flush();
	    	/*
	    	System.out.println("Data sent to Server --> ");

		    if (true && input.available() > 0) {
	    		String message = input.readUTF();
	    		System.out.println(message);
		    }
		    else System.out.println("Msg LOST !");
		    */
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sendWord(String ip, int port, String sendData){
        String message = "";
        try (Socket socket = new Socket(ip, port)) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeUTF(sendData);
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();

            // waiting for a short time, quiting quickly can lead to msg lost
            try {
                Thread.currentThread().sleep(200);
            }
            catch (InterruptedException e) {
                // e.printStackTrace();
                return "InterruptedException occurs";
            }
            if (input.available() > 0) {
                message = input.readUTF();
            }
            else return "Msg LOST !";

            if (socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    // e.printStackTrace();
                    return "IOException occurs";
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
}
