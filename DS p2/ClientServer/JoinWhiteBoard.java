package ClientServer;
import java.awt.Graphics;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import whiteboard.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class JoinWhiteBoard extends JFrame {
    int x1,x2,y1,y2,curchoice;
    DataInputStream is;
    DataOutputStream os;
    ObjectInputStream iss;
    Graphics g;
    WhiteBoard newPad;
    WhiteBoard.drawings nb;
    String userName;
    Socket client;
    static boolean outcome = false;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        JoinWhiteBoard CP = new JoinWhiteBoard();
        CP.welcomeFrame();
//        while(true){
//            if (outcome=true){
//                CP.ShowUI(userName);
//                break;
//            }
//
//        }


    }

    public void welcomeFrame() throws IOException, ClassNotFoundException{

        boolean outcome;
        JFrame welcomeFrame = new JFrame("Welcome to PowerPuff Paint");
        // create a object of JTextField with 16 columns and a given initial text
        JTextField welcomeText = new JTextField("Enter username",16);

        // create a new button
        JButton welcomeButton = new JButton("Submit");
        welcomeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e)  {

                        try{
                            //if (!adminServer.userArrayList.contains(welcomeText.getText())) {
                            while (true){
                                if (welcomeText.getText()!=null) {
                                    userName = welcomeText.getText();
                                    System.out.println(userName);
//
                                    welcomeFrame.dispose();
                                    System.out.println("Welcome");
                                    System.out.println("Creating sockets");
                                    creat();
                                    break;
                                }
                                System.out.println(userName);

                            }






                        }catch (Exception w){
                            w.printStackTrace();
                        }


                        // set the text of field to blank
                        //welcomeText.setText("");
                    }
                });

        welcomeFrame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {

                        System.exit(0);
                    }
                });
        // add buttons and textfield to panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.add(welcomeButton);
        welcomePanel.add(welcomeText);

        // add panel to frame
        welcomeFrame.add(welcomePanel);

        // set the size of frame
        welcomeFrame.setSize(300, 200);
        welcomeFrame.setVisible(true);




    }



    //产生一个Socket类用于连接服务器，并得到输入流
    public void creat() {
        try {

            System.out.println(1);
            client =new Socket("localhost", 9090);
//            is = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            System.out.println(2);
            is = new DataInputStream(client.getInputStream());
            System.out.println(3);
            //iss = new ObjectInputStream(client.getInputStream());
            System.out.println(4);
            os = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

//            BufferedReader response = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
//            BufferedWriter request = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            System.out.println(userName);
            os.writeUTF(userName);
            System.out.println(5);
            os.flush();
            System.out.println(6);

            if(is.readUTF().equals("Yes")){
                try{
                    ShowUI();



                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }


            }else{
                System.exit(0);
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //构造客户端界面并启动线程
    public void ShowUI() throws IOException, ClassNotFoundException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(userName + client);
        newPad = new WhiteBoard(userName, client);

        newPad.setTitle(userName);
        newPad.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {

                    System.exit(0);
                }
            });
        g = newPad.getGraphics();
    }









}
