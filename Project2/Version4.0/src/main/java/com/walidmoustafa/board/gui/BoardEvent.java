package com.walidmoustafa.board.gui;/*
 * Name: Walid Moustafa
 * Student ID: 563080
 * Subject: COMP90015 - Distributed Systems
 * Assignment: Assignment 2 - Distributed Whiteboard
 * Project: com.walidmoustafa.board.gui.BoardServer
 * File: com.walidmoustafa.board.gui.BoardEvent.java
*/

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;


public class BoardEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    public int eventID;
    public final String eventType;
    public String userID;
    public ArrayList<String> userList;
    public int currentShape;
    public int currentMode;
    public Color currentColor;
    public boolean erasing;
    public int eraserSize;
    public Point startPoint;
    public Point endPoint;
    public ArrayList<Point> points;
    public ArrayList<String> textInput;
    public ArrayList<Shape> shapes;

    public BoardEvent(String eType) {
        eventType = eType;
    }

}
