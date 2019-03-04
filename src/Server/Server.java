package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
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
    WritableGUI gui; // to allow output to the GUI
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
    // parameterised constructor
    public Server(WritableGUI gui, int port){
        this.port = port;
        this.gui = gui;
        /**
        * sets up the server socket to listen to and accept
        * connection requests given a port number
        */
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
        clients = new HashSet<>(); // creates hash set to store connected clients
    }
    
    @Override
    public void run(){
        Socket clientSocket = null;
        InputStream is;
        try{
            clientSocket = serverSocket.accept();
            clients.add(clientSocket); //This returns a boolean
            while(clientSocket != null){
                is = clientSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                if(line!= null){
                    if (line.toLowerCase().startsWith("/exitApp")) {
                        gui.write(clientSocket.getInetAddress().getHostName() + "is leaving");
                        return;
                    }
                    gui.write(clientSocket.getInetAddress().getHostName() + ": " + line);
                }
            }
            clients.remove(clientSocket);
            clientSocket.close();
        } catch (IOException e){
            System.out.println(e);
        } finally {

            try { 
                clientSocket.close(); clients.remove(clientSocket); 
            } catch (IOException e) {
                System.out.println("Socket Closed");
            }
        } 
    }

}