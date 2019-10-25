package ClientServer;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "Guest";
	private int id;
    Socket socket;
    
    public User(String myname, int Id) {
    	this.name= myname;
    	this.id = Id;
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

    public String toString(){
        return this.id+ "  "+ this.name +" ";
    }

}









