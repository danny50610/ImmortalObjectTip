package com.ImmortalObjectTip.network;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketCreateTip implements IMessage {
    
    public int x;
    public int y;
    public int z;
    public int face;
    public int dim;
    
    public PacketCreateTip() {}
    
    public PacketCreateTip(PlayerInteractEvent event) {
        x = event.x;
        y = event.y;
        z = event.z;
        face = event.face;
        dim = event.world.provider.dimensionId;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        face = buf.readByte();
        dim = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(face);
        buf.writeInt(dim);
    }

}
