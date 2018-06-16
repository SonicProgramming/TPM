package libgd;

import java.io.Serializable;
import java.net.URL;
import java.util.Objects;

/**
 * @author Sonic
 */
public class TexturePack implements Serializable {

    static final long serialVersionUID = 900900900L;
    
    //Information about TPs
    //Is also referred to as "TP meta" in the comments
    //Don't occasionally think that only meta string is mentioned
    private String name = "";
    private String author = "";
    private String meta = "";
    private long id = -1;
    private float gameVersion = 0;
    private URL downloadURL = null;
    private double packSize = 0; //Unnecessary, 0 by default
    
    public TexturePack(String pname, String auth, String metadata, long ID, float ver, URL durl){
        name = pname;
        author = auth;
        meta = metadata;
        id = ID;
        gameVersion = ver;
        downloadURL = durl;
    }
    
    public TexturePack(String pname, String auth, String metadata, long ID, float ver, URL durl , double size){
        name = pname;
        author = auth;
        meta = metadata;
        id = ID;
        gameVersion = ver;
        downloadURL = durl;
        packSize = size;
    }
    
    @Override
    public int hashCode(){
        String cnv = String.valueOf(id);
        return Integer.parseInt(cnv.substring((cnv.length()-1)/2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TexturePack other = (TexturePack) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Float.floatToIntBits(this.gameVersion) != Float.floatToIntBits(other.gameVersion)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.downloadURL, other.downloadURL)) {
            return false;
        }
        return true;
    }
    
    public String getName(){
        return name;
    }
    
    public String getAuthor(){
        return author;
    }
    
    public String getMeta(){
        return meta;
    }
    
    public long getID(){
        return id;
    }
    
    public float getGameVersion(){
        return gameVersion;
    }
    
    public URL getURL(){
        return downloadURL;
    }
    
    public double getPackageSize(){
        return packSize;
    }
    
}
