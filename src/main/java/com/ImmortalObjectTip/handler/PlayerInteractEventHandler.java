package com.ImmortalObjectTip.handler;

import com.ImmortalObjectTip.ImmortalObjectTip;
import com.ImmortalObjectTip.TipInfoBase;

import com.ImmortalObjectTip.network.PacketCreateTipBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PlayerInteractEventHandler {

    @SubscribeEvent
    public void PlayerInteract(PlayerInteractEvent event) {
        if (event.action == Action.LEFT_CLICK_BLOCK && event.getWorld().getBlock(event.x, event.y, event.z) == Blocks.bedrock && !event.getEntityPlayer().capabilities.isCreativeMode) {
            int dimId = event.world.provider.dimensionId;

            PacketCreateTipBlock packet = null;
            EnumFacing face = event.getFace();
            if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH || face == EnumFacing.EAST || face == EnumFacing.WEST) {
                packet = new PacketCreateTipBlock(dimId, event.x, event.y, event.z, event.face);
            }
            else if (face == EnumFacing.DOWN) {
                packet = new PacketCreateTipBlock(dimId, TipInfoBase.Type.BlockBottom, event.x, event.y, event.z);
            }
            else if (face == EnumFacing.UP) {
                packet = new PacketCreateTipBlock(dimId, TipInfoBase.Type.BlockTop, event.x, event.y + 1, event.z);
            }
            ImmortalObjectTip.instance.network.sendToDimension(packet, dimId);

            //DebugMessage(event);
        }
    }
    
    @SuppressWarnings("unused")
    private void DebugMessage(PlayerInteractEvent event) {
        System.out.println(String.format("side = %s, player = %s, pos = %s, face = %s, action = %s",
                event.getWorld().isRemote ? "Client" : "Server",
                event.getEntityPlayer().getDisplayName(),
                event.getPos().toString(),
                event.getFace(),
                event.action)
        );
    }
    
}
