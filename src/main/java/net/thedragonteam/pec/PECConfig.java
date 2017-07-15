package net.thedragonteam.pec;

import net.thedragonteam.thedragonlib.config.ModConfigProperty;

public class PECConfig {

    @ModConfigProperty(category = "end_crystal", name = "hasEnderCrystalBasePlate", comment = "Enable/Disable the base from the Ender Crystal")
    public static boolean hasEnderCrystalBasePlate = false;
}
