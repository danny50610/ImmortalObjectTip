package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTipBase;

public class TipInfoBase {

    public enum Type {
        BlockTop,
        BlockSide,
        BlockBottom,
        Player
    }

    private static final int tickTime = 1000 / 20; // 1 tick 經過的毫秒數
    
    private static final int MaxTime = 5000; 

    public final int dim;
    public final Type type;

    private int time = 0;
    
    public TipInfoBase(PacketCreateTipBase message) {
        this.dim = message.dim;
        this.type = message.type;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof TipInfoBase && equals((TipInfoBase)obj);
    }
    
    public boolean equals(TipInfoBase obj) {
        return this.dim == obj.dim && this.type == obj.type;
    }
    
    public void update(float partialTicks) {
        time += tickTime * partialTicks;
    }
    
    public boolean isDisappear() {
        return time > MaxTime;
    }
    
    public float getHeightRatio() {
        return time / (float)MaxTime;
    }
    
}
