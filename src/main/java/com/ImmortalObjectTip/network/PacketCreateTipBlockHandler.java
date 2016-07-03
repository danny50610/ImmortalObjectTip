package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoBlock;
import com.ImmortalObjectTip.handler.clinet.RenderTipHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketCreateTipBlockHandler implements IMessageHandler<PacketCreateTipBlock, IMessage> {

    @Override
    public IMessage onMessage(PacketCreateTipBlock message, MessageContext ctx) {
        RenderTipHandler.addTipBlock(new TipInfoBlock(message));
        return null;
    }

}
