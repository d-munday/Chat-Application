package Client;
import java.net.*;
import java.io.*;

// listens for incoming messages from the server
public class ClientThread extends Thread{
    Socket clientSocket = null;
    Client client = null;
    DataInputStream input;
    String message="";
    public ClientThread(Socket clientSocket, Client client)
    {
	this.clientSocket = clientSocket;
	this.client = client;
    }
    
    @Override
    public void run()
    {
	while(true)
	{
            try
            {
                input = new DataInputStream(this.clientSocket.getInputStream());
		String msg = input.readUTF();
                client.receive(msg); // send message to client handler to write to gui
		System.out.println("Received: " + msg);
            } catch(IOException e){
		System.out.println(e);
                break;
            }     
	}
        close();
    }
    // closes all sockets and data streams
    public void close(){
        try{
            input.close(); 
            clientSocket.close(); 
        } catch(IOException e){
            System.out.println(e);
        }
    }
}