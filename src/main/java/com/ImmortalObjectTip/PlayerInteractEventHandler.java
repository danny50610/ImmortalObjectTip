package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTip;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PlayerInteractEventHandler {

    @SubscribeEvent
    public void PlayerInteract(PlayerInteractEvent event) {
        if (event.action == Action.LEFT_CLICK_BLOCK && event.world.getBlock(event.x, event.y, event.z) == Blocks.bedrock && !event.entityPlayer.capabilities.isCreativeMode) {
            PacketCreateTip packet = new PacketCreateTip(event);
            ImmortalObjectTip.instance.wrapper.sendToDimension(packet, packet.dim);
            //DebugMessage(event);
        }
    }
    
    @SuppressWarnings("unused")
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
