package com.ImmortalObjectTip;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractEventHandler {

    @SubscribeEvent
    public void PlayerInteract(PlayerInteractEvent event) {
        
    }
    
    private void DebugMessage(PlayerInteractEvent event) {
        System.out.println(String.format("side = %s, player = %s, x = %d, y = %d, z = %d, face = %d, action = %s",
                event.world.isRemote ? "Client" : "Server",
                event.entityPlayer.getDisplayName(),
                event.x,
                event.y,
                event.z,
                event.face,
                event.action)
        );
    }
    
}
