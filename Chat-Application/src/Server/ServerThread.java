package Server;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
* This class listens to incoming messages from clients and sends it back to all clients
*/
public class ServerThread extends Thread{ 
    Server serv;
    String name;
    Socket clientSocket;
    DataOutputStream output;
    DataInputStream input;
   //constructor
    public ServerThread(Server serv, Socket clientSocket){  
        this.serv = serv;
	this.clientSocket = clientSocket;
    }
    
    //returns the server in use
    public Server getServer(){ return serv;}
    
    // send the message to clients
    // this function is used by the server class
    public synchronized void transmitMsg(String msg){ 
        try {
            output = new DataOutputStream(clientSocket.getOutputStream());
            output.writeUTF(msg); // send message to all clients
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // closes all sockets and data streams
    public void close(){
        try{
            output.close();
            input.close();
            clientSocket.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }
    //reads incoming message and sends it back to clients 
    @Override
    public void run(){
	while(true){
            try{
                input = new DataInputStream(clientSocket.getInputStream());
                String message = input.readUTF();
                System.out.println(message);
                serv.sendMsg(message);            		
            } catch (IOException e) {
                System.out.println("Client disconnected!");
                break;
            }
        }
        close();	
    }
}

