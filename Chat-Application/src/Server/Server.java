package Server;
import Client.Client;
import FTP.FTServer;
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{
    ServerSocket serverSocket;
    HashSet<ServerThread> clients;
    Thread thread = null;
    FTServer ftServer;
    /**
    * sets up the server socket to listen to and accept
    * connection requests given a port number
    * @param port The port number to listen on
    */
    public Server(int port){
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
        clients = new HashSet<>();
        ftServer = new FTServer();
        System.out.println("Moeny");
    }
    
    @Override
    public void run(){ 
        System.out.println("FTP SERVER");
        initiateConnection(); 
    }
    
    // a thread that listens for new client connection requests
    public void start()
    {
	if(thread == null)
	{
            ftServer.start();
            thread = new Thread(this);
            thread.start();
	}
    }
    //start connection
    public void initiateConnection(){ 
        try{
            while(thread != null){
                Socket clientSocket = serverSocket.accept();
                ServerThread st = new ServerThread(this, clientSocket);
                clients.add(st); // add client thread to set of connected clients
                st.start();
                System.out.println("connected...");
            }
        } catch (IOException e){   
            System.out.println(e);
        }   
    }

    // removes a client from the hash set
    public synchronized void removeclient(ServerThread client)
    {	
        clients.remove(client);
    }
    
   // sends a message to all clients
    public synchronized void sendMsg(String msg){
        if(msg.contains(Client.SERVERMSG)){
            int nameEnd = msg.indexOf(Client.FILEIDNAME);
            String fileName = msg.substring(0, nameEnd).trim();
            System.out.println(fileName + " Intercepted");
            try {
                Socket sendFTSMsg = new Socket("localhost", 5001);
                DataOutputStream output = new DataOutputStream(sendFTSMsg.getOutputStream());
                output.writeUTF(fileName + Client.FILEIDNAME);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            ftServer.setName(fileName);
            return;
        }
        System.out.println("Sending '" + msg + "'");
        clients.stream().forEach((s) -> { // lambda expression to loop through clients in set
            s.transmitMsg(msg);
        });
    }
    
    public static void main(String[] args) throws IOException{
        Server server = new Server(1200);
        server.start();
    }
}
