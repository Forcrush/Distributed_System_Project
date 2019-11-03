package dataSource;

import java.net.*;

public class User {
    private String name = "Guest";
    private int id = 0;
    public Socket ip;

    public User() {
    }

    public User(String s) {
        this.name = s;
    }

    public User(int id, String s, Socket inet) {
        this.id=id;
        this.name = s;
        this.ip = inet;
    }

    public User(Socket inet) {
        this.ip = inet;
    }

    public User(int id, Socket inet) {
        this.id=id;
        this.ip = inet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getIp() {
        return ip;
    }

    public void setIp(Socket ip) {
        this.ip = ip;
    }

    public boolean equals(String s){
        if (name == "Guest") return false;
        return this.name == s;
    }
    public boolean equals(Socket inet){
        if (this.ip==null) return false;
        return this.ip==inet;
    }
    public boolean equals(User user){
        return ((this.name == user.name) && (this.ip == user.ip));
    }
    public String toString(){
        return this.id + "  " + this.name + " " + this.ip.getInetAddress().getHostAddress();
    }

}