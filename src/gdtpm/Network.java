package gdtpm;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import libgd.TexturePack;

/**
 * @author Sonic
 */
class Network {
    
    protected static void atlp(String str, Color col){
        mainFrame.appendToPane(mainFrame.jTextPane1, str, col);
    }    
    static Socket cliSock;
    static InetSocketAddress server = new InetSocketAddress("84.22.115.98", 65531);   //IP and port of my server. Warning! I do NOT own the host! 
    
    //Code 1000
    public static boolean isOnline(){
        boolean isOnline = false;
        try {
            cliSock = new Socket();
            cliSock.connect(server);
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            dos.writeInt(1000);
            isOnline = true;
            cliSock.close();
        } catch (IOException ex) {
            isOnline = false;
        }
        return isOnline;
    }
    
    //Code 100
    public static TexturePack getTexturePack(long id){
        TexturePack receivedTP = null;
        try {
            atlp("\nStarted code 100\n", Color.BLUE);
            
            cliSock = new Socket();
            cliSock.connect(server);
            atlp("Connected\n", Color.BLUE);
            
            ObjectInputStream ois = new ObjectInputStream(cliSock.getInputStream());
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            DataInputStream dis = new DataInputStream(cliSock.getInputStream());
            
            dos.writeInt(100); //Texture pack meta request code
            atlp("Code out\n", Color.WHITE);
            
            dos.writeLong(id);
            atlp("Data out\n", Color.WHITE);
            
            receivedTP = (TexturePack) ois.readObject();
            atlp("Data in\n", Color.WHITE);
            int response = dis.readInt();
            if(response == 0)
                atlp("Got data, network finished for code 100\n", Color.GREEN); 
            else
                atlp("Server reported a fail, network finished for code 100\nMessage: "+response+"\n", Color.ORANGE);             
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            atlp("Caught "+ex.toString()+"\nMsg: "+ex.getMessage()+"\n\n", Color.RED);
        }
        return receivedTP;
    }
    
    //Code 200
    public static long findByName(String name){  
        long id = 0;
        try {
            atlp("\nStarted code 200\n", Color.BLUE);
            cliSock = new Socket();
            cliSock.connect(server);
            atlp("Connected\n", Color.BLUE);
            DataInputStream dis = new DataInputStream(cliSock.getInputStream());
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            dos.writeInt(200); //Search request code
            atlp("Code out\n", Color.WHITE);
            dos.writeUTF(name);
            atlp("Data out\n", Color.WHITE);
            id = dis.readLong();
            atlp("Data in\n", Color.WHITE);
            int response = dis.readInt();
            if(response == 0)
                atlp("Got data, network finished for code 200\n", Color.GREEN); 
            else
                atlp("Server reported a fail, network finished for code 200\nMessage: "+response+"\n", Color.ORANGE);  
            dis.close();
            dos.close();
            cliSock.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            atlp("Caught "+ex.toString()+"\nMsg: "+ex.getMessage()+"\n\n", Color.RED);
        }
        return id;
    }
    
    //Experimental, code 300
    public static Map<String, Long> getTPUpdates(){
        Map<String, Long> updates = new HashMap<>();
        try {
            atlp("\nStarted code 300\n", Color.BLUE);
            cliSock = new Socket();
            cliSock.connect(server);
            atlp("Connected\n", Color.BLUE);
            ObjectInputStream ois = new ObjectInputStream(cliSock.getInputStream());
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            dos.writeInt(300); //Updates request code
            atlp("Code out\n", Color.WHITE);
            updates = (Map<String, Long>) ois.readObject();
            atlp("Data in\n", Color.WHITE);
            ois.close();
            dos.close();
            cliSock.close();  
            atlp("Got data, network finished for code 300\n", Color.GREEN);           
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            atlp("Caught "+ex.toString()+"\nMsg: "+ex.getMessage()+"\n\n", Color.RED);
        }
        return updates;
    }
    
    //Code 400
    public static String getUpdateMessage(){
        String msg = "";
        try {
            atlp("\nStarted code 400\n", Color.BLUE);
            cliSock = new Socket();
            cliSock.connect(server);
            atlp("Connected\n", Color.BLUE);
            DataInputStream dis = new DataInputStream(cliSock.getInputStream());
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            dos.writeInt(400); //Update message request code
            atlp("Code out\n", Color.WHITE);
            msg = dis.readUTF();
            atlp("Data in\n", Color.WHITE);
            int response = dis.readInt();
            if(response == 0)
                atlp("Got data, network finished for code 400\n", Color.GREEN); 
            else
                atlp("Server reported a fail, network finished for code 400\nMessage: "+response+"\n", Color.ORANGE);
            dis.close();
            dos.close();
            cliSock.close();  
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            atlp("Caught "+ex.toString()+"\nMsg: "+ex.getMessage()+"\n\n", Color.RED);
        }
        return msg;
    }
    
    //Code 500
    public static List<Long> getUsedIDs(){
        List<Long> toreturn = null;
        try {
            atlp("\nStarted code 500\n", Color.BLUE);
            cliSock = new Socket();
            cliSock.connect(server);
            atlp("Connected\n", Color.BLUE);
            ObjectInputStream ois = new ObjectInputStream(cliSock.getInputStream());
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            DataInputStream dis = new DataInputStream(cliSock.getInputStream());
            dos.writeInt(500);
            atlp("Code out\n", Color.WHITE);
            toreturn = (List<Long>) ois.readObject();
            atlp("Data in\n", Color.WHITE);
            int response = dis.readInt();
            if(response == 0)
                atlp("Got data, network finished for code 500\n", Color.GREEN); 
            else
                atlp("Server reported a fail, network finished for code 500\nMessage: "+response+"\n", Color.ORANGE);  
            ois.close();
            dis.close();
            dos.close();
            cliSock.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            atlp("Caught "+ex.toString()+"\nMsg: "+ex.getMessage()+"\n\n", Color.RED);
        }
        return toreturn;
    }
    
    //Code 600
    public static void upload(TexturePack tp){
        try {
            atlp("\nStarted code 600\n", Color.BLUE);
            cliSock = new Socket();
            cliSock.connect(server);
            atlp("Connected\n", Color.BLUE);
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            DataInputStream dis = new DataInputStream(cliSock.getInputStream());
            dos.writeInt(600);
            atlp("Code out\n", Color.WHITE);
            
            String name = tp.getName();
            String author = tp.getAuthor();
            String meta = tp.getMeta();
            long id = tp.getID();
            float gameVer = tp.getGameVersion();
            String URL = tp.getURL().toString();
            double pSize = tp.getPackageSize();
            
            dos.writeUTF(name);
            dos.flush();
            dos.writeUTF(author);
            dos.flush();
            dos.writeUTF(meta);
            dos.flush();
            dos.writeLong(id);
            dos.flush();
            dos.writeFloat(gameVer);
            dos.flush();
            dos.writeUTF(URL);
            dos.flush();
            dos.writeDouble(pSize);
            dos.flush();
            
            atlp("Data out\n", Color.WHITE);
            int response = dis.readInt();
            if(response == 0)
                atlp("Got data, network finished for code 600\n", Color.GREEN); 
            else
                atlp("Server reported a fail, network finished for code 600\nMessage: "+response+"\n", Color.ORANGE);   
            
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            atlp("Caught "+ex.toString()+"\nMsg: "+ex.getMessage()+"\n\n", Color.RED);
        }
    }
    
    //Unnecessary, kept for future needs
    public static void init(){
    }
    
}
