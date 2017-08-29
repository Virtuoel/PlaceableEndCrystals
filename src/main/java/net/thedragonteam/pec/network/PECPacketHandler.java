package net.thedragonteam.pec.network;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.thedragonteam.pec.Reference;

public class PECPacketHandler implements IMessageHandler<EnderCrystalData, IMessage> {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    @Override
    public IMessage onMessage(EnderCrystalData packet, MessageContext ctx) {
        PlayerInteractionManager interactionManager = ctx.getServerHandler().player.interactionManager;
        World world = interactionManager.world;
        if (world.getEntityByID(packet.getEntityID()) instanceof EntityEnderCrystal) {
            EntityEnderCrystal enderCrystal = (EntityEnderCrystal) world.getEntityByID(packet.getEntityID());
            if (enderCrystal != null) {
                enderCrystal.setBeamTarget(new BlockPos(packet.xPos, packet.yPos, packet.zPos));
                world.updateEntity(enderCrystal);
            }
        }
        // No response packet
        return null;
    }
}