package com.walidmoustafa.board.gui;/*
 * Name: Walid Moustafa
 * Student ID: 563080
 * Subject: COMP90015 - Distributed Systems
 * Assignment: Assignment 2 - Distributed Whiteboard
 * Project: com.walidmoustafa.board.App
 * File: com.walidmoustafa.board.gui.Text.java
*/

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Text implements Shape, Serializable {

    private static final long serialVersionUID = 1L;
    private final Point startPoint;
    private ArrayList<String> lines = new ArrayList<>();

    public Text(Point sPoint, ArrayList<String> input) {
        startPoint = sPoint;
        lines = input;
    }

    public static void draw(Graphics gfx, Point sPoint, ArrayList<String> lines) {
        int spacing = gfx.getFontMetrics().getHeight();
        int n = 0;
        for (String line : lines) {
            gfx.drawString(line, sPoint.x, sPoint.y + (n * spacing));
            n++;
        }
    }

    @Override
    public void draw(Graphics gfx) {
        int spacing = gfx.getFontMetrics().getHeight();
        int n = 0;
        for (String line : lines) {
            gfx.drawString(line, startPoint.x, startPoint.y + (n * spacing));
            n++;
        }
    }
}
