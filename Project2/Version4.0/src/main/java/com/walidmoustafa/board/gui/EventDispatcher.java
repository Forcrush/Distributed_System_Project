package com.walidmoustafa.board.gui;/*
 * Name: Walid Moustafa
 * Student ID: 563080
 * Subject: COMP90015 - Distributed Systems
 * Assignment: Assignment 2 - Distributed Whiteboard
 * Project: com.walidmoustafa.board.App
 * File: com.walidmoustafa.board.gui.EventDispatcher.java
*/

import com.walidmoustafa.board.App;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;


public class EventDispatcher implements Runnable {

    private final int LINE = 0;
    private final int RECT = 1;
    private final int OVAL = 2;
    private final int FREE = 3;
    private final int TEXT = 4;

    private final SharedPanel panel;
    private final UserPanel userPanel;
    private boolean joinRequestReplied = false;
    private boolean authorized = false;

    private int nextEventID = 0;

    public EventDispatcher(SharedPanel mainPanel, UserPanel uPanel) {
        panel = mainPanel;
        userPanel = uPanel;
    }

    private void dispatchEvent(BoardEvent event) {
        switch (event.eventType) {
            case "Terminate":
                JOptionPane.showMessageDialog(panel.mainFrame, "Whiteboard Session was terminated by Admin",
                        "Session Terminated", JOptionPane.INFORMATION_MESSAGE);
                try {
                    panel.boardServer.bounceUser(App.userID);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            case "joinRequest":
                int choice = JOptionPane.showConfirmDialog(panel.mainFrame,
                        event.userID + " wants to join the Whiteboard. Allow user?",
                        "Join Request", JOptionPane.YES_NO_OPTION);
                try {
                    if (choice == JOptionPane.YES_OPTION) {
                        App.boardServer.approveUser(event.userID);
                    } else {
                        App.boardServer.bounceUser(event.userID);
                    }
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                break;
            case "userList":
                authorized = false;
                for (String auser : event.userList) {
                    if (auser.equals(App.userID)) {
                        joinRequestReplied = true;
                        authorized = true;
                        break;
                    } else if (auser.equals("#" + App.userID)) {
                        joinRequestReplied = true;
                        authorized = false;
                        break;
                    }
                }
                if (joinRequestReplied) {
                    if (authorized) {
                        panel.requestFocusInWindow();
                        panel.mainFrame.setVisible(true);
                        Vector<String> uIDs = new Vector<>(event.userList);
                        userPanel.refresh(uIDs);
                    } else {
                        JOptionPane.showMessageDialog(panel.mainFrame, "Access to Board was denied by Admin",
                                "Access Denied", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }
                break;
            case "loadBoard":
                panel.shapes = event.shapes;
                panel.mainFrame.repaint();
                break;
            case "keyTyped":
                Text.draw(panel.getGraphics(), event.startPoint, event.textInput);
                break;
            case "mouseMoved":
                panel.addShape(new Text(event.startPoint, event.textInput));
                break;
            case "mouseDragged":
                if (event.erasing) {
                    Eraser.draw(panel.getGraphics(), event.eraserSize, event.points);
                } else {
                    switch (event.currentShape) {
                        case LINE:
                            Line.draw(panel.getGraphics(), event.startPoint, event.endPoint);
                            break;
                        case RECT:
                            Rect.draw(panel.getGraphics(), event.startPoint, event.endPoint, event.currentMode, event.currentColor);
                            break;
                        case OVAL:
                            Oval.draw(panel.getGraphics(), event.startPoint, event.endPoint, event.currentMode, event.currentColor);
                            break;
                        case FREE:
                            FreeHand.draw(panel.getGraphics(), event.points);
                            break;
                        case TEXT:
                            break;
                        default:
                            System.err.println("Unsupported shape type");
                            break;
                    }
                }
                panel.mainFrame.repaint();
                break;
            case "mouseReleased":
                if (event.erasing) {
                    panel.addShape(new Eraser(event.points, event.eraserSize));
                } else {
                    switch (event.currentShape) {
                        case LINE:
                            panel.addShape(new Line(event.startPoint, event.endPoint));
                            break;
                        case RECT:
                            panel.addShape(new Rect(event.startPoint, event.endPoint, event.currentMode, event.currentColor));
                            break;
                        case OVAL:
                            panel.addShape(new Oval(event.startPoint, event.endPoint, event.currentMode, event.currentColor));
                            break;
                        case FREE:
                            panel.addShape(new FreeHand(event.points));
                            break;
                        case TEXT:
                            break;
                        default:
                            System.err.println("Unsupported shape type");
                            break;
                    }
                }
                panel.mainFrame.repaint();
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            ArrayList<BoardEvent> boardEvents;
            try {
                boardEvents = panel.boardServer.getBoardEvents(nextEventID);

                for (BoardEvent event : boardEvents) {
                    if (event.eventType.equals("joinRequest") && App.isAdmin) {
                        dispatchEvent(event);
                    }
                }

                BoardEvent latestUserList = null;
                for (BoardEvent event : boardEvents) {
                    if (event.eventType.equals("userList")) {
                        latestUserList = event;
                    }
                }

                if (latestUserList != null) {
                    dispatchEvent(latestUserList);
                }

                if (!joinRequestReplied) {
                    continue;
                }

                for (BoardEvent event : boardEvents) {
                    if (!(event.eventType.equals("joinRequest") || event.eventType.equals("userList"))) {
                        dispatchEvent(event);
                    }
                }

                if (boardEvents.size() > 0) {
                    nextEventID = boardEvents.get(boardEvents.size() - 1).eventID + 1;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
