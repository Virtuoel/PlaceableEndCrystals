package net.thedragonteam.pec;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import static net.thedragonteam.pec.Reference.MODID;

@Mod(modid = MODID,
        name = Reference.MODNAME,
        version = Reference.VERSION,
        dependencies = Reference.DEPEND,
        updateJSON = Reference.UPDATE_JSON,
        guiFactory = Reference.GUI_FACTORY
)
public class PEC {

    @Instance(MODID)
    public static PEC instance;

    public static SimpleNetworkWrapper network;
}