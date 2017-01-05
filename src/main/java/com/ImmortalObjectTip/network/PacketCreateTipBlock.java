package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PacketCreateTipBlock extends PacketCreateTipBase {

    public BlockPos pos;
    public EnumFacing face;

    public PacketCreateTipBlock() {
    }

    public PacketCreateTipBlock(int dim, TipInfoBase.Type type, BlockPos pos) {
        super(dim, type);

        this.pos = pos;
    }

    public PacketCreateTipBlock(int dim, BlockPos pos, EnumFacing face) {
        super(dim, TipInfoBase.Type.BlockSide);

        this.pos = pos;
        this.face = face;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        this.pos = new BlockPos(buf.readFloat(), buf.readFloat(), buf.readFloat());
        if (type == TipInfoBase.Type.BlockSide) {
            face = EnumFacing.getFront(buf.readByte());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeFloat(this.pos.getX());
        buf.writeFloat(this.pos.getY());
        buf.writeFloat(this.pos.getZ());
        if (type == TipInfoBase.Type.BlockSide) {
            buf.writeByte(face.getIndex());
        }
    }
}
