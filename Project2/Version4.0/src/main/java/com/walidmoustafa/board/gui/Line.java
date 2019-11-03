package com.walidmoustafa.board.gui;/*
 * Name: Walid Moustafa
 * Student ID: 563080
 * Subject: COMP90015 - Distributed Systems
 * Assignment: Assignment 2 - Distributed Whiteboard
 * Project: com.walidmoustafa.board.App
 * File: com.walidmoustafa.board.gui.Line.java
*/

import java.awt.*;
import java.io.Serializable;

public class Line implements Shape, Serializable {

    private static final long serialVersionUID = 1L;
    private final Point startPoint;
    private final Point endPoint;

    public Line(Point sPoint, Point ePoint) {
        startPoint = sPoint;
        endPoint = ePoint;
    }

    public static void draw(Graphics gfx, Point sPoint, Point ePoint) {
        gfx.drawLine(sPoint.x, sPoint.y, ePoint.x, ePoint.y);
    }

    @Override
    public void draw(Graphics gfx) {
        gfx.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }
}
