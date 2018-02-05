package net.thedragonteam.pec;

public class Reference {

    public static final String MCVERSION = "1.12.2";
    public static final int MAJOR = 1;
    public static final int MINOR = 2;
    public static final int PATCH = 0;
    public static final String VERSION = MCVERSION + "-" + MAJOR + "." + MINOR + "." + PATCH;
    public static final String MODID = "pec";
    public static final String MODNAME = "PEC";
    public static final String UPDATE_JSON = "https://download.nodecdn.net/containers/thedragonteam/pec-updater.json";
    public static final String DEPEND = "required-after:forge@[14.23.2.2611,);" +
            "required-after:thedragonlib@[1.12.2-5.2.0,)";
    public static final String GUI_FACTORY = "net.thedragonteam.pec.client.gui.config.ConfigGuiFactory";

}
