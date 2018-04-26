package gdtpm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import libgd.TexturePack;

/**
 * @author Sonic
 */
public class GDTPM {
    //What is done before your old good GUI loads
    static File pf = new File(System.getProperty("user.dir") + "/prefs");
    private static boolean loaded = false;
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        File fi = new File(System.getProperty("user.dir")+"/files/");
        if(!fi.exists()) fi.mkdir();
        Manager.tpMeta = fi;
        try {
        for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            if(info.getClassName().contains("Windows")) UIManager.setLookAndFeel(info.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GDTPM.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadPrefs();
        try {
            initCache();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getClass().toGenericString(), "Error loading cache!", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getClass().toGenericString(), "Critical error while loading cache!", JOptionPane.ERROR_MESSAGE);
            System.exit(210); //The code for this exception.
        }
        
        mainFrame.frameMain(args);
        
    }
    
    private static void loadPrefs(){
        try {
            if(!pf.exists()) pf.createNewFile();
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(pf));
            Manager.settings = (Prefs) oos.readObject();
            Manager.packageCache = new File(Manager.settings.cacheFolder);
            Manager.gameFolder = Manager.settings.gameFolder;
            System.out.println(Manager.gameFolder);
            loaded = true;
            oos.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getClass().toGenericString(), "Error loading app preferences!", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getClass().toGenericString(), "Critical error while loading app preferences!", JOptionPane.ERROR_MESSAGE);
            System.exit(200); //The code for this exception.
        }
    }
    
    private static void initCache() throws IOException, ClassNotFoundException{
        if(loaded){
            File[] cachedTPs = Manager.tpMeta.listFiles();
            ObjectInputStream oos;
            for(File file : cachedTPs){
                if(file.isFile() && file.getName().endsWith(".gdtp")){
                    oos = new ObjectInputStream(new FileInputStream(file));
                    Manager.TPCache.add((TexturePack) oos.readObject());
                    oos.close();
                }
            }            
            Manager.fullyLoaded = true;
        }
    }
}
