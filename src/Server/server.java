package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

// defines the server in client-server communication
public class server {
    ServerSocket serverSocket; // MyService
    Socket clientSocket;
    DataInputStream input;
    PrintStream output;
    HashSet<Socket> clients;
    
    // constructor
    public server(int port){
        /**
        * sets up the server socket to listen to and accept
        * connection requests given a port number
        */
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
        clients = new HashSet<>();
    }

    public server(){
        /**
        * sets up the server socket to listen to and accept
        * connection requests given a default port number = 1025
        */
        this(1025);
    }
    
    // creates a client socket to facilitate communication with 
    // a specific client
    public void initiateConnection(int port){ 
        clientSocket = null;
        try{
            clientSocket = serverSocket.accept();
            clients.add(clientSocket);
        } catch (IOException e){
            System.out.println(e);
        }   
    }

    // gets message data from client socket
    public String getInput(){
        try{
            input = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e){
            System.out.println(e);
        }
        return input.toString();
    }

    // sends a message to the client
    public void outputMessage(){
        try{
            output = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e){
            System.out.println(e);
        }
    }

    // closes socket of specfic client
    public void closeClientSocket(Socket clientsSocket){
        try{
            clients.remove(clientsSocket);
            clientsSocket.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }
    
    // closes all sockets and data streams
    public void close(){
        try{
            output.close();
            input.close();
            serverSocket.close();
            clientSocket.close();
            clients.clear();
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
