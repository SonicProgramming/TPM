package gdtpm;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import libgd.TexturePack;

/**
 * @author Sonic
 */
class Network {
    
    //Append To Log Pane
    protected static void atlp(String str, Color col){
        mainFrame.appendToPane(mainFrame.jTextPane1, str, col);
    }    
    static Socket cliSock;
    static InetSocketAddress server;
    static {
        init();
    }
    
    //Finally i found how to use this
    public static void init() {
        server = new InetSocketAddress(Manager.serverAddress, 65531);   //IP and port of my server. Warning! I DO (now) own the host! 
    }
    
    public static boolean updateServerAddress() {
        try {
            //Using gist.github.com as a reliable way to store a text value
            String addressURL = "https://gist.githubusercontent.com/SonicProgramming/8e815e4e014f17881462c99fac48a7f8/raw/3bf97e546bdce551153413cd2d5774a8f383400d/GDTPM_server_address.txt";
            URL url = new URL(addressURL);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();            
            String address = new Scanner(is).useDelimiter("\\A").next();
            atlp("Server address should be " + address + "\n", Color.orange);
            Manager.serverAddress = address;
            init();
            atlp("Server address updated\n", Color.green);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //Code 1000
    public static boolean isOnline(){
        try {
            cliSock = new Socket();
            cliSock.setSoTimeout(1000);
            cliSock.connect(server, 1000);
            DataOutputStream dos = new DataOutputStream(cliSock.getOutputStream());
            dos.writeInt(1000);
            cliSock.close();
        } catch (IOException ex) {
            return false;
        }
        return true; 
    }
    
    //Code 100
    public static TexturePack getTexturePack(long id){
        TexturePack receivedTP = null;
        try {
            atlp("\nStarted code 100\n", Color.BLUE);
            
            cliSock = new Socket();
            cliSock.setSoTimeout(5000);
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
            cliSock.setSoTimeout(5000);
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
            cliSock.setSoTimeout(5000);
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
            cliSock.setSoTimeout(5000);
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
            cliSock.setSoTimeout(5000);
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
            cliSock.setSoTimeout(5000);
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
    
}
