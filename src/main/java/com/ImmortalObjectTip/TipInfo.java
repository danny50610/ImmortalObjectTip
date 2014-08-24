package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTip;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class TipInfo {
    private static final int tickTime = 1000 / 20; //1 tick 經過的毫秒數
    
    private static final int MaxTime = 5000; 
    
    public final int x;
    public final int y;
    public final int z;
    public final int face;
    public final int dim;
    private int time = 0;
    
    public TipInfo(PlayerInteractEvent event) {
        x = event.x;
        y = event.y;
        z = event.z;
        face = event.face;
        dim = event.world.provider.dimensionId;
    }
    
    public TipInfo(PacketCreateTip message) {
        x = message.x;
        y = message.y;
        z = message.z;
        face = message.face;
        dim = message.dim;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof TipInfo && equals((TipInfo)obj);
    }
    
    public boolean equals(TipInfo obj) {
        return this.x == obj.x &&
                this.y == obj.y &&
                this.z == obj.z &&
                this.face == obj.face &&
                this.dim == obj.dim;
                
    }
    
    @Override
    public String toString() {
        return String.format("x = %d, y = %d, z = %d, face = %d, dim = %d", x, y, z, face, dim);
    }
    
    public void updata(float partialTicks) {
        time += tickTime * partialTicks;
    }
    
    public boolean isDisappear() {
        return time > MaxTime;
    }
    
    public float getHeightRatio() {
        return time / (float)MaxTime;
    }
    
}
