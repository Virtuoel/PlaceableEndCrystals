package net.thedragonteam.pec;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.thedragonteam.pec.Reference.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@Config(modid = MODID, category = "pec")
public class PECConfig {

    @Comment({"Enable/Disable the base from the Ender Crystal"})
    public static boolean hasEnderCrystalBasePlate = false;

    @Comment({"Enable/Disable debugMode"})
    public static boolean debugMode = false;

    public static void sync() {
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            sync();
        }
    }
}
