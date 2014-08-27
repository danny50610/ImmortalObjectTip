package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfo;
import com.ImmortalObjectTip.clinet.RenderTipHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketCreateTipHandler implements IMessageHandler<PacketCreateTip, IMessage> {

    @Override
    public IMessage onMessage(PacketCreateTip message, MessageContext ctx) {
        RenderTipHandler.addTip(new TipInfo(message));
        return null;
    }

}
