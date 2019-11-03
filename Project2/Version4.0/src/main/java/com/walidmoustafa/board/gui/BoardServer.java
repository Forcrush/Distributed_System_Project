package com.walidmoustafa.board.gui;/*
 * Name: Walid Moustafa
 * Student ID: 563080
 * Subject: COMP90015 - Distributed Systems
 * Assignment: Assignment 2 - Distributed Whiteboard
 * Project: com.walidmoustafa.board.App
 * File: com.walidmoustafa.board.gui.BoardServer.java
*/

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface BoardServer extends Remote {

    String joinBoard(String candidateID) throws RemoteException;

    String getAdmin() throws RemoteException;

    void approveUser(String userID) throws RemoteException;

    void bounceUser(String userID) throws RemoteException;

    void addBoardEvent(BoardEvent event) throws RemoteException;

    ArrayList<BoardEvent> getBoardEvents(int startFrom) throws RemoteException;

}
