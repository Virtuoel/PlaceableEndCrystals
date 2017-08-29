package net.thedragonteam.pec;


import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.thedragonteam.pec.client.gui.GuiHandler;
import net.thedragonteam.pec.network.EnderCrystalData;
import net.thedragonteam.pec.network.PECPacketHandler;
import net.thedragonteam.thedragonlib.config.ModConfigProcessor;

import static net.thedragonteam.pec.Reference.MODID;

@Mod(modid = Reference.MODID,
        name = Reference.MODNAME,
        version = Reference.VERSION,
        dependencies = Reference.DEPEND,
        updateJSON = Reference.UPDATE_JSON,
        guiFactory = Reference.GUI_FACTORY
)
public class PEC {

    public static ModConfigProcessor configProcessor = new ModConfigProcessor();
    public static Configuration configuration;

    @Instance(MODID)
    public static PEC instance;
    public static GuiHandler guiHandler = new GuiHandler();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PECPacketHandler.INSTANCE.registerMessage(PECPacketHandler.class, EnderCrystalData.class, 0, Side.SERVER);
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        configProcessor.processConfig(PECConfig.class, configuration);
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
    }
}