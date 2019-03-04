package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

// defines the server in client-server communication
public class Server extends Thread{
    int port = 9999; // default port #
    ServerSocket serverSocket;
    PrintStream output;
    HashSet<Socket> clients;
    // default constructor
    public Server(){
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
        clients = new HashSet<>(); // creates hash set to store connected clients
    }
    /**
    * sets up the server socket to listen to and accept
    * connection requests given a port number
    * @param port The port number to listen on
    */
    public Server(int port){
        this.port = port;
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
        clients = new HashSet<>(); // creates hash set to store connected clients
    }
    
    public int getPort(){
        return port;
    }
    
    @Override
    public void run(){
        System.out.println("Listening on port: " + port + "...");
        Socket clientSocket = null;
        InputStream is;
        try{
            clients.add(clientSocket); //This returns a boolean
            while((clientSocket = serverSocket.accept()) != null){
                is = clientSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                System.out.println(line); // print out message
                if(line!= null){
                    if (line.toLowerCase().startsWith("/stopserver")) {
                        System.exit(0);
                    }
                }
                broadcastMsg(clientSocket, line);
            }
            clients.remove(clientSocket);
            clientSocket.close();
        } catch (IOException e){
            System.out.println(e);
        } 
    }
    /**
     * @param s The socket of the connected client
     * @param msg The message to broadcast
     */
    public void broadcastMsg(Socket s, String msg){
        try {
            s.getOutputStream().write(msg.getBytes());
            s.close();
            System.out.println("Sent message: " + msg);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
    }
}