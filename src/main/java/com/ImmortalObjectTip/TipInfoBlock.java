package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTipBlock;

public class TipInfoBlock extends TipInfoBase {

    public final float x;
    public final float y;
    public final float z;

    // for BlockSide
    public final int face;

    public TipInfoBlock(PacketCreateTipBlock message) {
        super(message);

        this.x = message.x;
        this.y = message.y;
        this.z = message.z;

        this.face = message.face;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TipInfoBlock that = (TipInfoBlock) o;

        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;
        if (Float.compare(that.z, z) != 0) return false;
        return face == that.face;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        result = 31 * result + face;
        return result;
    }
}
