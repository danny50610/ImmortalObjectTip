package com.ImmortalObjectTip;

import com.ImmortalObjectTip.handler.ExplosionEventHandler;
import com.ImmortalObjectTip.handler.LivingAttackEventHandler;
import com.ImmortalObjectTip.handler.PlayerInteractEventHandler;
import com.ImmortalObjectTip.network.PacketCreateTipBlock;
import com.ImmortalObjectTip.network.PacketCreateTipBlockHandler;
import com.ImmortalObjectTip.network.PacketCreateTipEntity;
import com.ImmortalObjectTip.network.PacketCreateTipEntityHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = ModInformation.MOD_ID, name = ModInformation.MOD_NAME, dependencies = ModInformation.DEPENDENCIES)
public class ImmortalObjectTip {

    @Instance
    public static ImmortalObjectTip instance;

    @SidedProxy(serverSide = "com.ImmortalObjectTip.CommonProxy", clientSide = "com.ImmortalObjectTip.ClientProxy")
    public static CommonProxy proxy;
    
    public SimpleNetworkWrapper network;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
        MinecraftForge.EVENT_BUS.register(new LivingAttackEventHandler());
        MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
        proxy.registerEvent();
        
        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInformation.MOD_ID);
        network.registerMessage(PacketCreateTipBlockHandler.class, PacketCreateTipBlock.class, 1, Side.CLIENT);
        network.registerMessage(PacketCreateTipEntityHandler.class, PacketCreateTipEntity.class, 2, Side.CLIENT);
    }
    
}
