package com.ImmortalObjectTip;

import net.minecraftforge.common.MinecraftForge;

import com.ImmortalObjectTip.network.PacketCreateTip;
import com.ImmortalObjectTip.network.PacketCreateTipHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = ModInformation.MOD_ID, name = ModInformation.MOD_NAME)
public class ImmortalObjectTip {

    @Instance
    public static ImmortalObjectTip instance;

    @SidedProxy(serverSide = "com.ImmortalObjectTip.CommonProxy", clientSide = "com.ImmortalObjectTip.ClientProxy")
    public static CommonProxy proxy;
    
    public SimpleNetworkWrapper wrapper;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
        proxy.registerEvent();
        
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModInformation.MOD_ID);
        wrapper.registerMessage(PacketCreateTipHandler.class, PacketCreateTip.class, 1, Side.CLIENT);
    }
    
}
