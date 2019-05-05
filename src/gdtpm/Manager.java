package gdtpm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import libgd.TexturePack;

/**
 * @author Sonic
 */
class Manager {
    //Current settings and runtime data
    protected static List<TexturePack> TPCache = new LinkedList();
    protected static Prefs settings;
    protected static File packageCache = new File("");
    protected static boolean fullyLoaded = false;
    protected static String gameFolder = "";
    protected static File tpMeta = new File("");
    protected static String serverAddress = "148.251.136.19";
}
