package FTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTClient extends Thread{
    public static final int BUFFER_SIZE = 1024 * 1024 * 1024 * 1024;
    private byte[] buffer;
    InputStream is;
    BufferedInputStream bis;
    boolean sending;
    Socket socket;
    File fileTS; // To Send
    String fileName;
    FileOutputStream fos;
    BufferedOutputStream bos;
    OutputStream os;
    String downloads;
    SimpleDateFormat formatter;
    
    /**
     *
     * @param fileName
     * @param send
     */
    public FTClient(String fileName, boolean send){
        downloads = System.getProperty("user.home") + System.getProperty("file.separator") 
            + "Downloads" + System.getProperty("file.separator");
        
        try {
            socket = new Socket("localhost", 5000);
        } catch (IOException ex) {
            Logger.getLogger(FTClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        formatter = new SimpleDateFormat("_ddMMyyyy_HHmmss"); 
        if(fileName == null){
            this.fileName = "";
        }else{
            this.fileName = fileName;
        }
        sending = send;
        
        //System.out.println(formatter.format(calendar.getTime()));
    }
    
    public FTClient(File file){
        this(file.getName(), true);
        this.fileTS = file;
    }
    
    public FTClient(){
        this("", false);
    }
    
    
    public void receive() throws FileNotFoundException, IOException{
        int dot = fileName.lastIndexOf('.');
        String ext = fileName.substring(dot);
        System.out.println(fileName + " EXT HERE");
        fileName = "CA" + fileName.substring(0, dot);

        int bytesRead;
        int current = 0;
        
        String path = downloads + fileName + ThreadLocalRandom.current().nextInt(0, 10000 + 1) + ext;
        System.out.println(path + " PATH");

        is = socket.getInputStream();
        bytesRead = is.read(buffer, 0, buffer.length);
        System.out.println(bytesRead + " BYTESREAD");
        current = bytesRead;
        
        do {
            bytesRead =
                is.read(buffer, current, (buffer.length-current));
            if(bytesRead >= 0) current += bytesRead;
        } while(bytesRead > -1);
        
        fos = new FileOutputStream(path);
        bos = new BufferedOutputStream(fos);
        
        bos.write(buffer, 0, current);
        bos.flush();

        is.close();
        fos.close();
        socket.close();

    }

    private void send() throws FileNotFoundException, IOException{
        buffer = new byte[(int)fileTS.length()];
        //System.out.println(fileTS.length() + " THIS BABY");
        is = new FileInputStream(fileTS);
        //bis = new BufferedInputStream(new FileInputStream(fileTS));
        is.read(buffer,0,buffer.length);
        
        System.out.println("Sending " + fileTS.getName() + " ("+ buffer.length +")");
        os = socket.getOutputStream();
        int count;
        while((count = is.read(buffer)) > 0){
            os.write(buffer, 0, count);
        }
        
        //os.flush();
        
        //is.close();
        is.close();
        os.close();
        socket.close();
        sending = false;
    } 
    
    @Override
    public void run(){
        System.out.println("THIS BABY");
        if(sending){
            try {
                System.out.println(fileTS.length() + " THIS BABY");
                send();
            } catch (IOException ex) {
                Logger.getLogger(FTClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            while(true){
                if(fileName != null){
                    try {
                        receive();
                    } catch (IOException ex) {
                        Logger.getLogger(FTClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}