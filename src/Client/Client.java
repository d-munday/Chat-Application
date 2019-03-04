package Client;

import java.io.IOException;
import java.net.Socket;

// defines a client in client-server communication
public class Client extends Thread{
    String hostName, message; // hostName = server address
    int port;
    
    // default constructor
    public Client(){

    }
    // initialises with client's name and port number
    public Client(String msg, String name, int port){
        this.message = msg;
        this.hostName = name;
        this.port = port;
    }
    
    public String getHostName(){
        return this.hostName;
    }
    
    public int getPort(){
        return this.port;
    }

    @Override
    public void run(){
        // open a connection, send a message and close connection
        try {
            Socket s = new Socket(hostName, port);
            s.getOutputStream().write(message.getBytes());
            s.close();
            System.out.println("Sent message: " + message);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}