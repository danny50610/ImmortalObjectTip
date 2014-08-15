package com.ImmortalObjectTip;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{
    @Override
    public void registerEvent() {
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
    }
}
