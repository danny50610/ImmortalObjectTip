package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketCreateTipBase implements IMessage {

    public int dim;
    public TipInfoBase.Type type;

    public PacketCreateTipBase() {
    }

    public PacketCreateTipBase(int dim, TipInfoBase.Type type) {
        this.dim = dim;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        type = TipInfoBase.Type.values()[buf.readByte()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeByte(type.ordinal());
    }

}
