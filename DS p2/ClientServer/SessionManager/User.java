package ClientServer.SessionManager;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    /**
     *
     */
    private String name;
    private int id;
    Socket socket;

    public User(String myname) {
        this.name= myname;
    }
    public User(int id, String name,Socket socket) {
        this.name = name;
        this.socket = socket;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " + "User: " + name;

    }

    //
//    public boolean equals(String s){
//        if (name=="Guest"){return false;}
//        return this.name==s;
//    }
//    public boolean equals(Socket inet){
//        if (this.ip==null){return false;}
//        return this.ip==inet;
//    }
//    public boolean equals(User user){
//        return ((this.name==user.name)&&(this.ip==user.ip));
//    }
//    public String toString(){
//        return this.id+ "  "+ this.name +" "+this.ip.getInetAddress().getHostAddress();
//    }

}








