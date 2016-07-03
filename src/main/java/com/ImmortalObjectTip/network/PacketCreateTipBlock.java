package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoBase;
import io.netty.buffer.ByteBuf;

public class PacketCreateTipBlock extends PacketCreateTipBase {

    public float x;
    public float y;
    public float z;
    public int face = -1;

    public PacketCreateTipBlock() {
    }

    public PacketCreateTipBlock(int dim, TipInfoBase.Type type, float x, float y, float z) {
        super(dim, type);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketCreateTipBlock(int dim, float x, float y, float z, int face) {
        super(dim, TipInfoBase.Type.BlockSide);

        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        x = buf.readFloat();
        y = buf.readFloat();
        z = buf.readFloat();
        if (type == TipInfoBase.Type.BlockSide) {
            face = buf.readByte();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        if (type == TipInfoBase.Type.BlockSide) {
            buf.writeByte(face);
        }
    }
}
