package com.ImmortalObjectTip.handler;

import com.ImmortalObjectTip.ImmortalObjectTip;
import com.ImmortalObjectTip.network.PacketCreateTipEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class LivingAttackEventHandler {

    @SubscribeEvent
    public void livingAttack(LivingAttackEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entityLiving;

        if (!player.capabilities.isCreativeMode) return;

        PacketCreateTipEntity packet = new PacketCreateTipEntity(player);
        ImmortalObjectTip.instance.network.sendToDimension(packet, packet.dim);
    }

}
