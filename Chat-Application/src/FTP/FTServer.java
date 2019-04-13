package FTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTServer extends Thread{
    public static final int BUFFER_SIZE = 1024 * 1024 * 1024 * 1024;
    //private byte[] buffer;
    ServerSocket server;
    ServerSocket msgServer;

    HashSet<Socket> sockets;
    File fileTS;
    String fileNameTR;
    ArrayDeque fileQ;
    boolean broadcast;
    boolean running;
    
    public FTServer(){
        try {
            server = new ServerSocket(5000); //FTP sender 
            msgServer = new ServerSocket(5001);
        } catch (IOException ex) {
            Logger.getLogger(FTServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //buffer = new byte[BUFFER_SIZE];
        sockets = new HashSet<>();
        broadcast = false;
    }
    
    public void broadcastFile(File file) throws FileNotFoundException, IOException{
        System.out.println("Broadcasting");
        Socket receiver;
        do{
            receiver = server.accept();
            System.out.println("Accepted connection : " + receiver);
            
            byte[] buffer = new byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            
            fis.read(buffer, 0, buffer.length);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(buffer, 0, buffer.length);
            System.out.println("Sending " + file.getName() + "(" + buffer.length + " bytes)");
            OutputStream os = receiver.getOutputStream();
            os.write(buffer, 0, buffer.length);
            os.flush();

            fis.close();
            os.close();
            receiver.close();
        }while(receiver != null);
        System.out.println("DONE SENDING ");
        broadcast = false;
    }
    
    public void setFileNameTR(String name){
        this.fileNameTR = name;
        System.out.println(name + "DEAD FROM SETTER");
    }
    
    public void receiveFile() throws IOException{
        Socket sender;
        sender = server.accept();
        System.out.println(sender);
        int bytesRead;
        int current = 0;
        //InputStream is = sender.getInputStream();
        FileOutputStream fos;
        BufferedOutputStream bos;
        while(fileNameTR == null || fileNameTR.isEmpty()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        InputStream is = sender.getInputStream();
        System.out.println(System.getProperty("user.home") + "SWS");
        String path = System.getProperty("user.home") 
            + System.getProperty("file.separator") 
            + "Downloads" + System.getProperty("file.separator")
            + "QT_" + fileNameTR;
        System.out.println(path + "SERVER Q");
        
        fos = new FileOutputStream(path);
        bos = new BufferedOutputStream(fos);
        byte[] buffer = new byte[BUFFER_SIZE];
        
        bytesRead = is.read(buffer, 0, buffer.length);
        current = bytesRead;
        
        do {
            bytesRead = is.read(buffer, current, (buffer.length-current));
            if(bytesRead >= 0) current += bytesRead;
        } while(bytesRead > -1);
        
        bos.write(buffer, 0 , current);
        bos.flush();
        
        fileNameTR = null;
        is.close();
        bos.close();
        fos.close();
        sender.close();
        
        /*
        broadcast = true;
        System.out.println("File Recieved by Server from " + sender);
        broadcastFile( new File(path) );
        */
    }
    
    @Override
    public void run(){
        System.out.println("FTP SERVER + runner");
        Socket receiver;
        while(true){
            
            if(fileNameTR!=null){
                System.out.println(fileNameTR + " FTP SERVER + runner");
                System.out.println("IN");
                try {
                    receiveFile();
                } catch (IOException ex) {
                    Logger.getLogger(FTServer.class.getName()).log(Level.SEVERE, null, ex);
                    //break;
                }
            }else if(fileNameTR==null){
                try {
                    receiver = msgServer.accept();
                    DataInputStream input = new DataInputStream(receiver.getInputStream());
                    String message = input.readUTF();
                    System.out.println(message + "Serve\"");
                    int nameEnd = message.indexOf(Client.Client.FILEIDNAME);
                    fileNameTR = message.substring(0, nameEnd);

                    input.close();
                    receiver.close();
                } catch (IOException ex) {
                    Logger.getLogger(FTServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
 
            try {
                //receiver = msgServer.accept();
                Thread.sleep(10);
                //System.out.println(fileNameTR + "SLEEPER");
            } catch (InterruptedException ex) {
                Logger.getLogger(FTServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}