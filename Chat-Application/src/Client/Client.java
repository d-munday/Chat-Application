package Client;
import FTP.FTClient;
import java.net.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Client{
    int portNumber;
    DataOutputStream output;
    Socket clientsocket;
    WritableGUI gui;
    String clientName;
    ClientThread listen = null;
    //FTClient ftClient;
    PopsUp popUp;
    public final static double COVERTER = 1024;
    public final static String EXCLCLIENT = "/(EXCL_CLIENT)\\ ";
    public final static String FILEIDENTIFER = "//FILE\\: ";
    public final static String SIZEIDENTIFER = "//SIZE\\: ";
    public final static String SERVERMSG = "//SERVERMSG\\ ";
    public final static String FILEIDNAME = "//FILEINFO-NAME\\ ";


    // initialises with host (server) name, port number and client name
    public Client(String hostName, int port, String clientName){
        this.clientName=clientName;
        try{ 
            clientsocket = new Socket(hostName, port);
        } catch (IOException e){
            System.out.println(e);
        }
        popUp = new PopsUp();
    }
    
    public String getName(){
        return clientName;
    }
    // allows the client to write to the GUI
    public void setGUI(WritableGUI gui){
        this.gui = gui;
    }
    
    public void sendFile(File file){
        FTClient ftClient = new FTClient(file);
        System.out.print("START SERVER PROCESS");
        ftClient.start();
        try {
            sendToServer(file.getName() + FILEIDNAME);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println(file.getTotalSpace());
            send4File(file.getName(), convert(file.length()));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void acceptFile(int answer, String fileName) throws IOException {
        if(answer == JOptionPane.YES_OPTION){
            FTClient ftClient = new FTClient(fileName, false);
            ftClient.start();
        }
    }
    
    public String convert(long size){
        String number = "";
        DecimalFormat df = new DecimalFormat("##.00");
        if(size < 1000){
            number = df.format(size) + "B";
        }else if(size < Math.pow(1000, 2)){
            number = df.format(size/COVERTER) + "KB";
        }else if(size < Math.pow(1000, 3)){
            number = df.format(size/Math.pow(COVERTER, 2)) + "MB";
        }else{
            number = df.format(size/Math.pow(COVERTER, 3)) + "GB";
        }
        System.out.println(number + " Space");
        return number;
    }
    
    // initialises a thread to listen for messages from the server
    public void start()
    {
	if(listen == null)
	{
            listen = new ClientThread(clientsocket,this);
            listen.start();
	}
    }
    
    // sends user input to the server with regards to info about the incoming
    // file
    public synchronized void sendToServer(String message) throws IOException{                  
        output = new DataOutputStream(clientsocket.getOutputStream());
        String line = message + " " + SERVERMSG + "from " + clientName;
        System.out.println("Sever Message: " + line);
        output.writeUTF(line);
    }

    // sends user input to the server
    public synchronized void send(String message) throws IOException{                  
        output = new DataOutputStream(clientsocket.getOutputStream());
        String line = clientName + ": " + message;
        System.out.println("Sending: "+ line);
        output.writeUTF(line);
    }
    
    //Message sender for file
    public synchronized void send4File(String name, String size) throws IOException{                  
            //file.getName() + ": " + convert(file.getTotalSpace())
            output = new DataOutputStream(clientsocket.getOutputStream());
            String line = clientName + " has sent a file\n" + name + ": " + size;
            System.out.println("Sending: "+ line);
            output.writeUTF(line + EXCLCLIENT + FILEIDENTIFER + name + " " + SIZEIDENTIFER + size);
    }
    
    // listens for messages from server
    public synchronized void receive(String msg) throws IOException{                          
        System.out.println(msg);
        if( msg.contains(EXCLCLIENT.trim()) ){
            int excl = msg.lastIndexOf(EXCLCLIENT.trim() );
            int fileNamePos = msg.lastIndexOf(FILEIDENTIFER) + FILEIDENTIFER.length();
            int fileSizePos = msg.lastIndexOf(SIZEIDENTIFER) + SIZEIDENTIFER.length();

            String client = msg.split(" ")[0];
            String fileName = msg.substring(fileNamePos, msg.lastIndexOf(SIZEIDENTIFER)).trim();
            String fileSize = msg.substring(fileSizePos).trim();
            msg = msg.substring(0, excl);

            if( client.equals(getName()) ){
                gui.write(getName() + ", you have a sent file\n" 
                        + fileName + ": " + fileSize);
                return;
            }

            gui.write(msg);
            //sendToServer(fileName + FILEIDNAME);
            acceptFile(popUp.pop(fileSize, client), fileName);
            return;
        }
        if(msg!=null)
            gui.write(msg);
    }
}

    

