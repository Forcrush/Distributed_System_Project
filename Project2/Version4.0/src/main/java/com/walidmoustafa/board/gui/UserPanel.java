package com.walidmoustafa.board.gui;/*
 * Name: Walid Moustafa
 * Student ID: 563080
 * Subject: COMP90015 - Distributed Systems
 * Assignment: Assignment 2 - Distributed Whiteboard
 * Project: com.walidmoustafa.board.App
 * File: com.walidmoustafa.board.gui.UserPanel.java
*/

import com.walidmoustafa.board.App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserPanel extends JPanel implements ActionListener, ListSelectionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private final BoardServer boardServer;
    private final String userID;
    private final JScrollPane scrollPane;
    private final JList<String> usersList;
    private JButton bounceButton;
    private final Vector<String> userIDs = new Vector<>();
    private String selectedUser;

    public UserPanel(BoardServer bServer, String uID, JScrollPane sPane) {
        boardServer = bServer;
        userID = uID;
        scrollPane = sPane;
        setBorder(BorderFactory.createTitledBorder("Board Users"));
        setLayout(new BorderLayout());

        if (App.isAdmin) {
            bounceButton = new JButton("Bounce");
            add(bounceButton, BorderLayout.SOUTH);
            bounceButton.addActionListener(this);
        }

        usersList = new JList<>(userIDs);
        UserListRenderer renderer = new UserListRenderer();
        usersList.setCellRenderer(renderer);
        add(usersList, BorderLayout.CENTER);
        usersList.addListSelectionListener(this);
        addKeyListener(this);
    }

    public synchronized void refresh(Vector<String> uIDs) {
        userIDs.removeAllElements();
        for (String auser : uIDs) {
            if (auser.charAt(0) != '#') {
                userIDs.add(auser);
            }
        }
        usersList.setListData(userIDs);
        if (userIDs.size() >= 2) {
            usersList.setSelectedIndex(1);
        } else {
            usersList.setSelectedIndex(0);
        }
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (event.getSource() == usersList && !event.getValueIsAdjusting()) {
            String stringValue = usersList.getSelectedValue();
            if (stringValue != null) {
                selectedUser = stringValue;
                if (bounceButton != null) {
                    if (selectedUser.equalsIgnoreCase(App.adminID)) {
                        bounceButton.setEnabled(false);
                    } else {
                        bounceButton.setEnabled(true);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == bounceButton) {
            int selection = usersList.getSelectedIndex();
            if (selection >= 0) {
                try {
                    boardServer.bounceUser(selectedUser);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        App.sharedPanel.keyTyping(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
