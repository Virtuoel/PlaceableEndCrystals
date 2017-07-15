package net.thedragonteam.pec;

public class Reference {
    /**
     * Updates every time the mod updates minecraft version,
     * Updates MAJOR with 1 after each version upgrade
     */
    public static final String MCVERSION = "1.12";
    /**
     * Updates every MAJOR change,
     * never resets
     */
    public static final int MAJOR = 1;
    /**
     * Updates every time a new block, item or features is added or change,
     * resets on MAJOR changes
     */
    public static final int MINOR = 0;
    /**
     * Updates every time a bug is fixed or issue solved or very minor code changes,
     * resets on MINOR changes
     */
    public static final int PATCH = 0;
    /**
     * The MobInspect Version
     */
    public static final String VERSION = MCVERSION + "-" + MAJOR + "." + MINOR + "." + PATCH;
    public static final String MODID = "pec";
    public static final String MODNAME = "PEC";
    public static final String UPDATE_JSON = "https://download.nodecdn.net/containers/thedragonteam/pec-updater.json";
    public static final String DEPEND = "required-after:forge@[14.21.1.2412,);" + "required-after:thedragonlib@[1.12-4.1.0,)";
    public static final String GUI_FACTORY = "net.thedragonteam.pec.client.gui.ConfigGuiFactory";

}
