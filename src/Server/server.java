package Server;
import java.net;
import java.io.IOException;

// defines the server in client-server communication
public class server {
    ServerSocket serverSocket; // MyService
    Socket clientSocket;
    DataInputStream input;
    PrintStream output;
    // constructor
    public server(int port){
        /**
        * sets up the server socket to listen to and accept
        * connection requests given a port number
        */
        try{
            ss = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // creates a client socket to facilitate communication with 
    // a specific client
    public void initiateConnection(int port){ 
        clientSocket = null;
        try{
            clientSocket = ss.accept();
        } catch (IOException e){
            System.out.println(e);
        }   
    }

    // gets message data from client socket
    public String getInput(){
        try{
            input = new DataInputStream(ss.getInputStream());
        } catch (IOException e){
            System.out.println(e);
        }
    }

    // sends a message to the client
    public void outputMessage(){
        try{
            output = new PrintStream(ss.getOutputStream());
        } catch (IOException e){
            System.out.println(e);
        }
    }

    // closes all sockets and data streams
    public void close(){
        try{
            output.close();
            input.close();
            ss.close();
            clientSocket.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
