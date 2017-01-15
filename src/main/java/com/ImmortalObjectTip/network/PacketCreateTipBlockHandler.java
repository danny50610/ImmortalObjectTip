package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoBlock;
import com.ImmortalObjectTip.TipInfoEntity;
import com.ImmortalObjectTip.handler.clinet.RenderTipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCreateTipBlockHandler implements IMessageHandler<PacketCreateTipBlock, IMessage> {

    @Override
    public IMessage onMessage(PacketCreateTipBlock message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                RenderTipHandler.addTipBlock(new TipInfoBlock(message));
            }
        });

        return null;
    }

}
