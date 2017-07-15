package net.thedragonteam.pec.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.thedragonteam.pec.Reference;
import net.thedragonteam.thedragonlib.util.LogHelper;

import java.util.List;

import static net.thedragonteam.pec.PEC.configuration;
import static net.thedragonteam.pec.PECConfig.hasEnderCrystalBasePlate;

@EventBusSubscriber(modid = Reference.MODID)
public class WorldEventHandler {

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickBlock e) {
        World world = e.getWorld();
        BlockPos pos = e.getPos();
        ItemStack itemStackIn = e.getItemStack();
        Item itemIn = itemStackIn.getItem();
        EntityPlayer player = e.getEntityPlayer();
        if (itemIn == Items.END_CRYSTAL) {
            BlockPos blockpos = pos.up();
            ItemStack itemstack = player.getHeldItem(e.getHand());

            if (!player.canPlayerEdit(blockpos, e.getFace(), itemstack)) return;
            BlockPos blockpos1 = blockpos.up();
            boolean flag = !world.isAirBlock(blockpos) && !world.getBlockState(blockpos).getBlock().isReplaceable(world, blockpos);
            flag = flag | (!world.isAirBlock(blockpos1) && !world.getBlockState(blockpos1).getBlock().isReplaceable(world, blockpos1));

            if (flag) return;
            double d0 = (double) blockpos.getX();
            double d1 = (double) blockpos.getY();
            double d2 = (double) blockpos.getZ();
            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

            if (!list.isEmpty()) return;
            if (!world.isRemote) {
                EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(world, (double) ((float) pos.getX() + 0.5F), (double) (pos.getY() + 1), (double) ((float) pos.getZ() + 0.5F));
                entityendercrystal.setShowBottom(hasEnderCrystalBasePlate);
                world.spawnEntity(entityendercrystal);

                if (world.provider instanceof WorldProviderEnd) {
                    DragonFightManager dragonfightmanager = ((WorldProviderEnd) world.provider).getDragonFightManager();
                    if (dragonfightmanager != null) dragonfightmanager.respawnDragon();
                }
            }

            itemstack.shrink(1);
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        syncConfig();
        LogHelper.info("Refreshing configuration file");
    }

    private static void syncConfig() {
        if (configuration.hasChanged())
            configuration.save();
    }
}
