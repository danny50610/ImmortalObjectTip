package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketCreateTipEntity extends PacketCreateTipBase  {

    public int entityId;

    public PacketCreateTipEntity() {
    }

    public PacketCreateTipEntity(EntityPlayer player) {
        super(player.dimension, TipInfoBase.Type.Player);

        this.entityId = player.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeInt(this.entityId);
    }
}
