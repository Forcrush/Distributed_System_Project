package boardsocket;

import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

import whiteboardgui.ListData;
import whiteboardgui.User;

public class PanelServer extends Thread {
    private ListData model;
    private int id = 0;
    private int port;

    public PanelServer(ListData model, int port){
        this.port = port;
        this.model = model;
    }

    public void run() {
        OutputList output = new OutputList();
        ServerSocket s = null;
        try {
            s = new ServerSocket(port);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        do {
            System.out.print(id);
            try {
                while (true) {
                    id++;
                    Socket socket = s.accept();
                    User newUser = new User(id, socket);
                    new Mult(socket, output, newUser, model);
                }
            } catch (SocketException se) {
                System.out.println("SocketException\n" + se.getMessage());
                System.exit(0);
            } catch (IOException ioe) {
                System.out.println("IOException\n " + ioe);
                ioe.printStackTrace();
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException\n" + nfe);
                nfe.printStackTrace();
            }
        } while (true);
    }

}

class Mult extends Thread {
    private Socket client;
    private BufferedReader in; 
    private BufferedWriter out; 
    private static int count; 
    private int id;
    private OutputList outputList;
    private User user;
    private ListData model;

    public Mult(Socket socket, OutputList output, User newUser, ListData model) throws IOException {

        this.client = socket;
        this.outputList = output;
        this.user = newUser;
        this.model = model;
        if (handleLogonMsg()) {
            this.start();
            model.add(this.user);
            model.refresh();  
        }
        else{
            out.write("quit");
            out.newLine();
            out.flush();
        }
    }

    public void run() {
        try {
            outputList.append(out);
            handleDrawMsg();
            outputList.remove(out);
            System.out.println("quit success£ºid = " + this.id + " , " + this.client);

        } catch (IOException e) {
            System.out.println("IOException");
        } finally {
            try {
                if (client != null)
                    client.close();
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
    }

    private boolean handleLogonMsg() throws IOException {

        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        user.setName(in.readLine());
        int response = JOptionPane.showConfirmDialog(null, user.getName()+" want to join in?", "allow",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            System.out.println(user.getName() + "quit!");
            return false;
        } else if (response == JOptionPane.YES_OPTION) {
            System.out.println(user.getName() + "join!");
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println(user.getName() + "join!");
        }
        this.id = count++;
        out.write(Integer.toString(id));
        out.newLine();
        out.flush();
        System.out.println("Login successfully: id = " + this.id + " , " + this.client);
        return true;
    }

    private void handleDrawMsg() throws IOException {

        String dataBuffer;
        while (true) {
            dataBuffer = in.readLine(); 
            if (dataBuffer.equals("quit")) break;
            outputList.update(dataBuffer); 
        }
    }

}
