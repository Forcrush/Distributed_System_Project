package com.walidmoustafa.board.gui;

/*
  Name: Walid Moustafa
  Student ID: 563080
  Subject: COMP90015 - Distributed Systems
  Assignment: Assignment 2 - Distributed Whiteboard
  Project: com.walidmoustafa.board.App
  File: com.walidmoustafa.board.gui.Rect.java
 */

import java.awt.*;
import java.io.Serializable;

public class Rect implements Shape, Serializable {
    private static final int UNFILLED = 0;
    private static final long serialVersionUID = 1L;
    private final Point startPoint;
    private final Point endPoint;
    private final int mode;
    private final Color color;


    public Rect(Point sPoint, Point ePoint, int currentMode, Color currentColor) {
        startPoint = sPoint;
        endPoint = ePoint;
        mode = currentMode;
        color = currentColor;
    }

    public static void draw(Graphics gfx, Point sPoint, Point ePoint, int currentMode, Color currentColor) {
        int x, y, width, height;

        if (sPoint.x < ePoint.x) {
            x = sPoint.x;
            width = ePoint.x - sPoint.x;
        } else {
            x = ePoint.x;
            width = sPoint.x - ePoint.x;
        }

        if (sPoint.y < ePoint.y) {
            y = sPoint.y;
            height = ePoint.y - sPoint.y;
        } else {
            y = ePoint.y;
            height = sPoint.y - ePoint.y;
        }

        if (currentMode == UNFILLED) {
            gfx.drawRect(x, y, width, height);
        } else {
            gfx.setColor(currentColor);
            gfx.fillRect(x, y, width, height);
        }
    }

    @Override
    public void draw(Graphics gfx) {
        int x, y, width, height;

        if (startPoint.x < endPoint.x) {
            x = startPoint.x;
            width = endPoint.x - startPoint.x;
        } else {
            x = endPoint.x;
            width = startPoint.x - endPoint.x;
        }

        if (startPoint.y < endPoint.y) {
            y = startPoint.y;
            height = endPoint.y - startPoint.y;
        } else {
            y = endPoint.y;
            height = startPoint.y - endPoint.y;
        }

        if (mode == UNFILLED) {
            gfx.drawRect(x, y, width, height);
        } else {
            gfx.setColor(color);
            gfx.fillRect(x, y, width, height);
        }
    }
}
