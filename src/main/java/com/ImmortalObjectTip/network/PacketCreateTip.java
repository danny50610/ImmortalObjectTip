package com.ImmortalObjectTip.network;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketCreateTip implements IMessage {
    
    public float x;
    public float y;
    public float z;
    public int face;
    public int dim;
    
    public PacketCreateTip() {}

    public PacketCreateTip(float x, float y, float z, int face, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
        this.dim = dim;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readFloat();
        y = buf.readFloat();
        z = buf.readFloat();
        face = buf.readByte();
        dim = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeByte(face);
        buf.writeInt(dim);
    }

}
