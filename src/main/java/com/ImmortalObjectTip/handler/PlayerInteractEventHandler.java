package com.ImmortalObjectTip.handler;

import com.ImmortalObjectTip.ImmortalObjectTip;
import com.ImmortalObjectTip.TipInfoBase;

import com.ImmortalObjectTip.network.PacketCreateTipBlock;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PlayerInteractEventHandler {

    @SubscribeEvent
    public void PlayerInteract(PlayerInteractEvent event) {
        if (event.action == Action.LEFT_CLICK_BLOCK && event.world.getBlock(event.x, event.y, event.z) == Blocks.bedrock && !event.entityPlayer.capabilities.isCreativeMode) {
            int dimId = event.world.provider.dimensionId;

            PacketCreateTipBlock packet = null;
            if (event.face == 2 || event.face == 3 || event.face == 4 || event.face == 5) {
                packet = new PacketCreateTipBlock(dimId, event.x, event.y, event.z, event.face);
            }
            else if (event.face == 0) {
                packet = new PacketCreateTipBlock(dimId, TipInfoBase.Type.BlockBottom, event.x, event.y, event.z);
            }
            else if (event.face == 1) {
                packet = new PacketCreateTipBlock(dimId, TipInfoBase.Type.BlockTop, event.x, event.y + 1, event.z);
            }
            ImmortalObjectTip.instance.network.sendToDimension(packet, dimId);

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
