package com.ImmortalObjectTip;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = ImmortalObjectTip.MOD_ID, name = ImmortalObjectTip.MOD_NAME)
public class ImmortalObjectTip {
    public static final String MOD_ID = "immortalobjecttip";
    public static final String MOD_NAME = "Immortal Object Tip";
    
    @Instance
    public static ImmortalObjectTip instance;
    @SidedProxy(serverSide = "com.ImmortalObjectTip.CommonProxy", clientSide = "com.ImmortalObjectTip.ClientProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
    }
    
}
