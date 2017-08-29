package net.thedragonteam.pec.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class EnderCrystalData implements IMessage {

    public EntityEnderCrystal enderCrystal;
    public int xPos, yPos, zPos;
    public int entityID;

    // A default constructor is always required
    public EnderCrystalData() {
    }

    public EnderCrystalData(ByteBuf buf) {
        xPos = buf.readInt();
        yPos = buf.readInt();
        zPos = buf.readInt();
        entityID = buf.readInt();
    }

    public EnderCrystalData(World world, int entityID) {
        this.entityID = entityID;
        this.enderCrystal = (EntityEnderCrystal) world.getEntityByID(entityID);
        if (this.enderCrystal != null) {
            this.xPos = (int) enderCrystal.posX;
            this.yPos = (int) enderCrystal.posY;
            this.zPos = (int) enderCrystal.posZ;
        }
    }

    public EnderCrystalData(int xPos, int yPos, int zPos, int entityID) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.entityID = entityID;
    }

    public EnderCrystalData(int xPos, int yPos, int zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }


    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(xPos);
        buf.writeInt(yPos);
        buf.writeInt(zPos);
        buf.writeInt(entityID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        xPos = buf.readInt();
        yPos = buf.readInt();
        zPos = buf.readInt();
        entityID = buf.readInt();
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public int getEntityID() {
        return entityID;
    }
}