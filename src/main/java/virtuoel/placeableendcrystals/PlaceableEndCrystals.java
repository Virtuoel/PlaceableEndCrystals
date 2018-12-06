package virtuoel.placeableendcrystals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import virtuoel.placeableendcrystals.reference.PlaceableEndCrystalsConfig;

@Mod(modid = PlaceableEndCrystals.MOD_ID, version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class PlaceableEndCrystals
{
	public static final String MOD_ID = "placeableendcrystals";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Mod.Instance(MOD_ID)
	public static PlaceableEndCrystals instance;
	
	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event)
	{
		LOGGER.error("Expecting signature {}, however there is no signature matching that description. The file {} may have been tampered with. This version will NOT be supported by the author!", event.getExpectedFingerprint(), event.getSource().getName());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		PlaceableEndCrystalsConfig.updateValidity();
	}
}
