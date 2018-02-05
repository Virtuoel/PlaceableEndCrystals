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
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.thedragonteam.pec.Reference;
import net.thedragonteam.pec.obj.BeamPos;
import net.thedragonteam.thedragonlib.util.LogHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.thedragonteam.pec.PECConfig.debugMode;
import static net.thedragonteam.pec.PECConfig.hasEnderCrystalBasePlate;
import static net.thedragonteam.pec.utils.Utils.*;

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
            BlockPos posA = new BlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            BlockPos posB = new BlockPos(blockpos.getX() + 1.0D, blockpos.getY() + 2.0D, blockpos.getZ() + 1.0D);

            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(posA, posB));

            if (!list.isEmpty()) return;
            if (world.isRemote) return;
            EntityEnderCrystal enderCrystal = new EntityEnderCrystal(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.0D, (double) pos.getZ() + 0.5D);
            enderCrystal.setCustomNameTag(itemStackIn.getDisplayName());
            enderCrystal.setAlwaysRenderNameTag(false);
            enderCrystal.setShowBottom(hasEnderCrystalBasePlate);
            world.spawnEntity(enderCrystal);

            if (world.provider instanceof WorldProviderEnd) {
                DragonFightManager dragonfightmanager = ((WorldProviderEnd) world.provider).getDragonFightManager();
                if (isNotNull(dragonfightmanager)) dragonfightmanager.respawnDragon();
            }

            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(AttackEntityEvent e) {
        if (debugMode) {
            if (isNull(e.getTarget()) || isNull(e.getEntityPlayer()) || e.getTarget().world.isRemote) return;
            if (e.getTarget() instanceof EntityEnderCrystal) {
                e.getTarget().setDead();
                if (e.getTarget().isDead) {
                    ItemStack endCrystal = new ItemStack(Items.END_CRYSTAL);
                    endCrystal.setStackDisplayName(e.getTarget().getCustomNameTag());
                    e.getTarget().entityDropItem(endCrystal, 0F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityUpdate(EntityEvent e) {
        if (e.getEntity() == null || e.getEntity().world.isRemote) return;
        if (e.getEntity() instanceof EntityEnderCrystal) {
            EntityEnderCrystal eC = (EntityEnderCrystal) e.getEntity();
            String customNameTag = eC.getCustomNameTag();
            if (Objects.equals(customNameTag, "End Crystal")) return;
            String[] coords = customNameTag.split(" ");
            LogHelper.info(customNameTag);
            if (coords.length > 2) {
                if (is(coords, "~")) {
                    eC.setBeamTarget(new BeamPos(eC.posX, eC.posY, eC.posZ).getBlockPos());
                } else if (is(coords[0], coords[1], "~")) {
                    eC.setBeamTarget(new BeamPos(eC.posX, eC.posY, coords).getBlockPos());
                } else if (is(coords[0], coords[2], "~")) {
                    eC.setBeamTarget(new BeamPos(eC.posX, coords, eC.posZ).getBlockPos());
                } else if (is(coords[1], coords[2], "~")) {
                    eC.setBeamTarget(new BeamPos(coords, eC.posY, eC.posZ).getBlockPos());
                } else if (is(coords[0], "~")) {
                    eC.setBeamTarget(new BeamPos(eC.posX, coords).getBlockPos());
                } else if (is(coords[1], "~")) {
                    eC.setBeamTarget(new BeamPos(coords, eC.posY, coords).getBlockPos());
                } else if (is(coords[2], "~")) {
                    eC.setBeamTarget(new BeamPos(coords, eC.posZ).getBlockPos());
                } else {
                    eC.setBeamTarget(new BeamPos(coords).getBlockPos());
                }
                if (debugMode) {
                    LogHelper.info("Coords:" + Arrays.toString(coords));
                }
            } else if (debugMode) {
                LogHelper.info("The Coords you specified are invalid:" + Arrays.toString(coords));
                LogHelper.info("The length of the array is:" + coords.length);
            }
        }
    }
}
