package com.ImmortalObjectTip.handler;

import com.ImmortalObjectTip.ImmortalObjectTip;
import com.ImmortalObjectTip.TipInfoBase;
import com.ImmortalObjectTip.network.PacketCreateTipBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class ExplosionEventHandler {

    private final static int detectionRadius = 3;

    @SubscribeEvent
    public void explosionEventStart(ExplosionEvent.Start event) {
        World world = event.getWorld();
        Random random = new Random();
        BlockPos explosionPosition = new BlockPos(event.getExplosion().getPosition());

        for (int dz = -detectionRadius; dz <= detectionRadius; dz++) {
            for (int dx = -detectionRadius; dx <= detectionRadius; dx++) {
                for (int dy = -detectionRadius; dy <= detectionRadius; dy++) {
                    BlockPos pos = explosionPosition.add(dx, dy, dz);
                    if (world.getBlockState(pos).getBlock() == Blocks.BEDROCK /*&& random.nextDouble() < 0.75d*/) {
                        EnumFacing face = EnumFacing.getFacingFromVector(-dx, -dy, -dz);

                        PlayerInteractEventHandler.SendPacket(pos, face, event.getWorld().provider.getDimension());
                    }
                }
            }
        }
    }

}
