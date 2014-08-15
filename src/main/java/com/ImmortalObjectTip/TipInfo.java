package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTip;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class TipInfo {
    //private static final MaxTime = 
    
    public final int x;
    public final int y;
    public final int z;
    public final int face;
    public final int dim;
    
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
    public String toString() {
        return String.format("x = %d, y = %d, z = %d, face = %d, dim = %d", x, y, z, face, dim);
    }
    
}
