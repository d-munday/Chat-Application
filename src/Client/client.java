package Client;
import java.net;
import java.io.IOException;
import java.net.Socket;

// defines a client in client-server communication
public class client {
    Socket clientSocket;
    DataInputStream input;
    PrintStream output;
    // constructor
    // initialises with client's name and port number
    public client(String name, int port){
        try{
            clientSocket = new Socket(name, port);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    // receives a message
    public void receiveMsg(){
        try{
            input = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e){
            System.out.println(e);
        }
    }

    // outputs a message
    public void sendMsg(){
        try{
            output = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e){
            System.out.println(e);
        }
    }

    // closes connection and input/output streams
    public void close(){
        try{
            input.close();
            output.close();
            clientSocket.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
