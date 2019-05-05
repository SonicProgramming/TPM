package gdtpm;

import java.io.Serializable;

/**
 * @author Sonic
 */
class Prefs implements Serializable {
    
    static final long serialVersionUID = 700700700L;
    
    //Just settings
    protected String cacheFolder = "";
    protected String gameFolder = "";
    protected String serverAddress = "";
    /**
     * @deprecated 
     */
    public Prefs(String cache, String game){
        this.cacheFolder = cache;
        this.gameFolder = game;
    }
    public Prefs(String cache, String game, String server){
        this.cacheFolder = cache;
        this.gameFolder = game;
        this.serverAddress = server;
    }
}
