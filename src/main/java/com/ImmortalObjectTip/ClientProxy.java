package com.ImmortalObjectTip;

import com.ImmortalObjectTip.clinet.RenderWorldLastEventHandler;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{
    @Override
    public void registerEvent() {
        MinecraftForge.EVENT_BUS.register(new RenderWorldLastEventHandler());
    }
}
