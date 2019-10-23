import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import java.awt.event.*;

public class ChatboxClient {

    // IP and port
    static String ip = "";
    static String portString = "";
    static String Username = "";
    static int port = 0;
    static int clientPos = 0;

    public static void main(String[] args) {
        // server-address and port
        try {
            ip = args[0];
            portString = args[1];
            Username = args[2];
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

        JFrame frame = new JFrame("ChatBox");
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
                String res = sendMsg(ip, port, Username, input.getText(), clientPos);
                String[] content = res.split("( )*::( )*");
                if (content.length > 1 && content[1].length() != 0) {
                    clientPos = Integer.parseInt(content[0]);
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
            String res = sendMsg(ip, port, Username, "", clientPos);
            String[] content = res.split("( )*::( )*");
            if (content.length > 1 && content[1].length() != 0) {
                clientPos = Integer.parseInt(content[0]);
                output.append(content[1]);
            }
        }
        // testConnect(ip, port);

    }

    public static String sendMsg(String ip, int port, String Username, String sendData, int clientPos){
        String message = "";

        try (Socket socket = new Socket(ip, port)) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeUTF(String.valueOf(clientPos) + " : " + Username + " : " + sendData);
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

}
