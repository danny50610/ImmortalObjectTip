package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTipBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TipInfoBlock extends TipInfoBase {

    public BlockPos pos;

    public EnumFacing face;

    public TipInfoBlock(PacketCreateTipBlock message) {
        super(message);

        this.pos = message.pos;

        this.face = message.face;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TipInfoBlock that = (TipInfoBlock) o;

        if (!this.pos.equals(that.pos)) return false;
        return face == that.face;

    }

    @Override
    public int hashCode() {
        int result = pos != null ? pos.hashCode() : 0;
        result = 31 * result + (face != null ? face.hashCode() : 0);
        return result;
    }
}
