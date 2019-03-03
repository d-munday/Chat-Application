package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

// defines a client in client-server communication
public class Client extends Thread{
    String hostName, message;
    int port;
    
    // constructor
    public Client(){

    }
    // initialises with client's name and port number
    public Client(String msg, String name, int port){
        this.message = msg;
        this.hostName = name;
        this.port = port;
    }

    public void run(){
        try {
            Socket s = new Socket(hostName,port);
            s.getOutputStream().write(message.getBytes());
            s.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
