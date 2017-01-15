package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoEntity;
import com.ImmortalObjectTip.handler.clinet.RenderTipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCreateTipEntityHandler implements IMessageHandler<PacketCreateTipEntity, IMessage> {

    @Override
    public IMessage onMessage(PacketCreateTipEntity message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                RenderTipHandler.addTipPlayer(new TipInfoEntity(message));
            }
        });

        return null;
    }
}
