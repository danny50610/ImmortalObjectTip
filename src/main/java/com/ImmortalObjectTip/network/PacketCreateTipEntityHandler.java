package com.ImmortalObjectTip.network;

import com.ImmortalObjectTip.TipInfoEntity;
import com.ImmortalObjectTip.handler.clinet.RenderTipHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCreateTipEntityHandler implements IMessageHandler<PacketCreateTipEntity, IMessage> {

    @Override
    public IMessage onMessage(PacketCreateTipEntity message, MessageContext ctx) {
        RenderTipHandler.addTipPlayer(new TipInfoEntity(message));
        return null;
    }
}
