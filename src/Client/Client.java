package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// defines a client in client-server communication
public class Client extends Thread{
    String hostName, message;
    Scanner input;
    PrintWriter output;
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
    
    public String getHostName(){
        return this.hostName;
    }
    
    public int getPort(){
        return this.port;
    }

    @Override
    public void run(){
        try {
            Socket s = new Socket(hostName, port);
            input = new Scanner(s.getInputStream());
            output = new PrintWriter(s.getOutputStream(), true);
            s.getOutputStream().write(message.getBytes());
            while(input.hasNextLine()){
                output.write(input.nextLine() + "\n");
            }
            s.close();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            //s.close();
        }
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
    }
}