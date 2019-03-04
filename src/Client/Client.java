package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;

// defines a client in client-server communication
public class Client extends Thread{
    String alias, hostName, message; // hostName = server address
    ServerSocket s;
    int port;
    
    // default constructor
    public Client(){

    }
    /**
     * 
     * @param alias The client's alias
     * @param msg The message to send
     * @param name The host name
     * @param port The target (server) port
     */
    public Client(String alias, String msg, String name, int port){
        this.alias = alias;
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
    
    public String getAlias(){
        return this.alias;
    }

    public String listen(Socket s, ServerSocket ss){
        String msg = "";
        try{
            InputStream is;
            while((s = ss.accept()) != null){
                is = s.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                msg = br.readLine();
                //System.out.println(msg); // print out message
            }
            s.close();
        } catch (IOException e){
            System.out.println(e);
        }
        return msg;
    }
    @Override
    public void run(){
        // open a connection, send a message and close connection
        try {
            Socket s = new Socket(hostName, port);
            s.getOutputStream().write(message.getBytes());
            System.out.println("Sent message: " + message);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}