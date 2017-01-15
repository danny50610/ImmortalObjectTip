package com.ImmortalObjectTip.handler;

import com.ImmortalObjectTip.ImmortalObjectTip;
import com.ImmortalObjectTip.TipInfoBase;

import com.ImmortalObjectTip.network.PacketCreateTipBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractEventHandler {

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.BEDROCK && !event.getEntityPlayer().capabilities.isCreativeMode) {
            SendPacket(event.getPos(), event.getFace(), event.getWorld().provider.getDimension());
            //DebugMessage(event);
        }
    }

    public static void SendPacket(BlockPos pos, EnumFacing face, int dimId) {
        PacketCreateTipBlock packet = null;

        if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH || face == EnumFacing.EAST || face == EnumFacing.WEST) {
            packet = new PacketCreateTipBlock(dimId, pos, face);
        }
        else if (face == EnumFacing.DOWN) {
            packet = new PacketCreateTipBlock(dimId, TipInfoBase.Type.BlockBottom, pos);
        }
        else if (face == EnumFacing.UP) {
            packet = new PacketCreateTipBlock(dimId, TipInfoBase.Type.BlockTop, pos.add(0, 1, 0));
        }
        ImmortalObjectTip.instance.network.sendToDimension(packet, dimId);
    }

    @SuppressWarnings("unused")
    private void DebugMessage(PlayerInteractEvent event) {
        System.out.println(String.format("side = %s, player = %s, pos = %s, face = %s",
                event.getWorld().isRemote ? "Client" : "Server",
                event.getEntityPlayer().getDisplayName(),
                event.getPos().toString(),
                event.getFace()
                )
        );
    }

}
