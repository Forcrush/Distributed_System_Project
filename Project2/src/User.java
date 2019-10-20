import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    private int connectionCount;
    String name = "Guest";
    Socket socket;

    public User(int connectionCount, String name) {
        this.connectionCount = connectionCount;
        this.name = name;
        this.socket = socket;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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








