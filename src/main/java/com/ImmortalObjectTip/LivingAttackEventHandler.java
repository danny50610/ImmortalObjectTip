package com.ImmortalObjectTip;

import com.ImmortalObjectTip.network.PacketCreateTip;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class LivingAttackEventHandler {

    @SubscribeEvent
    public void livingAttack(LivingAttackEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entityLiving;

        if (!player.capabilities.isCreativeMode) return;

        PacketCreateTip packet = new PacketCreateTip(
                (float) player.posX,
                (float) player.posY + 1,
                (float) player.posZ,
                1,
                player.dimension
        );
        ImmortalObjectTip.instance.network.sendToDimension(packet, packet.dim);
    }

}
