package net.thedragonteam.pec;

public class Reference {

    public static final String MCVERSION = "1.12.1";
    public static final int MAJOR = 1;
    public static final int MINOR = 0;
    public static final int PATCH = 0;
    public static final String VERSION = MCVERSION + "-" + MAJOR + "." + MINOR + "." + PATCH;
    public static final String MODID = "pec";
    public static final String MODNAME = "PEC";
    public static final String UPDATE_JSON = "https://download.nodecdn.net/containers/thedragonteam/pec-updater.json";
    public static final String DEPEND = "required-after:forge@[14.22.0.2462,);" +
            "required-after:thedragonlib@[1.12.1-5.0.0,)";
    public static final String GUI_FACTORY = "net.thedragonteam.pec.client.gui.config.ConfigGuiFactory";
    public static final String GUI_HANDLER = "net.thedragonteam.pec.client.gui.config.ConfigGuiFactory";

}
