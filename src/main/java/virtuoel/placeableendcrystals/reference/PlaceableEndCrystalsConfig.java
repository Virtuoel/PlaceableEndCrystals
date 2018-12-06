package virtuoel.placeableendcrystals.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.InvalidBlockStateException;
import net.minecraft.command.NumberInvalidException;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import virtuoel.placeableendcrystals.PlaceableEndCrystals;

@Config(modid = PlaceableEndCrystals.MOD_ID)
public class PlaceableEndCrystalsConfig
{
	@Comment("List of block states you can/cannot place End Crystals on")
	public static String[] placementStates = { };
	
	@Comment("If false, placementStates will be a placement blacklist instead of a whitelist")
	public static boolean placementStateWhitelist = false;
	
	@Comment("If false, the base plate cannot be shown. Does not apply to already placed End Crystals.")
	public static boolean allowBasePlateToggle = true;
	
	@Comment("If false, the beam target will be ignored. Does not apply to already placed End Crystals.")
	public static boolean allowSettingBeamTarget = true;
	
	@Config.Ignore
	public static List<Pair<Block, Predicate<IBlockState>>> placementStateList = new ArrayList<>();

	public static boolean canPlaceOnState(IBlockState state)
	{
		for(Pair<Block, Predicate<IBlockState>> value : placementStateList)
		{
			if(state.getBlock() == value.getLeft() && value.getRight().test(state))
			{
				return placementStateWhitelist;
			}
		}
		return !placementStateWhitelist;
	}
	
	public static void updateValidity()
	{
		placementStateList.clear();
		placementStateList = Arrays.stream(placementStates)
			.map(PlaceableEndCrystalsConfig::parseStateString)
			.filter(Objects::nonNull)
			.map(s ->
			{
				try
				{
					return Pair.<Block, Predicate<IBlockState>>of(s.getLeft(), CommandBase.convertArgToBlockStatePredicate(s.getLeft(), s.getRight())::test);
				}
				catch(InvalidBlockStateException e)
				{
					PlaceableEndCrystals.LOGGER.fatal("Could not parse block state properties from string: \"{}\", (Block: \"{}\").", s.getRight(), s.getLeft().getRegistryName().toString());
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
	
	public static Pair<Block, String> parseStateString(String stateStr)
	{
		String variantStr = "*";
		String blockStr = stateStr;
		int variantBegin = stateStr.indexOf('[');
		
		if(variantBegin == -1)
		{
			variantBegin = stateStr.indexOf(' ');
		}
		
		if(variantBegin != -1)
		{
			int variantEnd = stateStr.indexOf(']');
			int len = variantEnd == -1 ? stateStr.length() : variantEnd;
			
			variantStr = stateStr.substring(variantBegin + 1, len);
			blockStr = stateStr.substring(0, variantBegin);
		}
		
		try
		{
			return Pair.of(CommandBase.getBlockByText(null, blockStr), variantStr);
		}
		catch(NumberInvalidException e)
		{
			PlaceableEndCrystals.LOGGER.fatal("Could not parse block from string: \"{}\", (Block: \"{}\", Properties: \"{}\").", stateStr, blockStr, variantStr);
			return null;
		}
	}
	
	@EventBusSubscriber(modid = PlaceableEndCrystals.MOD_ID)
	private static class Handler
	{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if(event.getModID().equals(PlaceableEndCrystals.MOD_ID))
			{
				ConfigManager.sync(PlaceableEndCrystals.MOD_ID, Config.Type.INSTANCE);
				updateValidity();
			}
		}
	}
}
