package com.walidmoustafa.board.gui;/*
 * Name: Walid Moustafa
 * Student ID: 563080
 * Subject: COMP90015 - Distributed Systems
 * Assignment: Assignment 2 - Distributed Whiteboard
 * Project: com.walidmoustafa.board.App
 * File: com.walidmoustafa.board.gui.Eraser.java
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;


public class Eraser implements Shape, Serializable {
    private static final int seed = 3;
    private static final long serialVersionUID = 1L;
    private int size = 1;
    private ArrayList<Point> points = new ArrayList<>();

    public Eraser(ArrayList<Point> pts, int size) {
        this.size = size;
        points = pts;
    }

    private static void erasePoint(Graphics gfx, int size, Point point) {
        size = size * seed;
        int x = (point.x - size) > 0 ? (point.x - size) : 0;
        int y = (point.y - size) > 0 ? (point.y - size) : 0;
        int width = size * 2;
        int height = size * 2;

        gfx.fillRect(x, y, width, height);
    }

    public static void draw(Graphics gfx, int size, ArrayList<Point> points) {
        Color clr = gfx.getColor();
        gfx.setColor(new Color(240, 240, 240));
        for (Point point : points) {
            erasePoint(gfx, size, point);
        }
        gfx.setColor(clr);
    }

    @Override
    public void draw(Graphics gfx) {
        Color clr = gfx.getColor();
        gfx.setColor(new Color(240, 240, 240));
        for (Point point : points) {
            erasePoint(gfx, size, point);
        }
        gfx.setColor(clr);
    }
}
