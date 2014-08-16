package com.ImmortalObjectTip.clinet;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.ImmortalObjectTip.ImmortalObjectTip;
import com.ImmortalObjectTip.TipInfo;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWorldLastEventHandler {
    private Minecraft mc = Minecraft.getMinecraft();
    
    private static final ResourceLocation texture = new ResourceLocation(ImmortalObjectTip.MOD_ID, "textures/immortal_object_tip.png");
    
    private static List<TipInfo> tipList = Lists.newArrayList();
    
    public static void addTip(TipInfo info) {
        tipList.add(info);
        System.out.println(info);
    }
    
    float time = 0f;
    
    //Knowledge: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1433242-solved-forge-rendering-lines-in-the-world
    @SubscribeEvent
    public void RenderWorldLast(RenderWorldLastEvent event) {        
        time += event.partialTicks * 0.025f;

        mc.renderEngine.bindTexture(texture);
        
        EntityClientPlayerMP player = mc.thePlayer;
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;
        
        GL11.glPushMatrix();
        GL11.glTranslated(-x, -y, -z);
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        tessellator.startDrawingQuads();
        for (TipInfo tip : tipList) {
            x = tip.x + 0.5d; y = tip.y + 0.5d; z = tip.z + 0.5d;
            if (tip.face == 2) {
                tessellator.addVertexWithUV(x + 0.5d, y + 0.5d, z - 0.51d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x + 0.5d, y - 0.5d, z - 0.51d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.5d, y - 0.5d, z - 0.51d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.5d, y + 0.5d, z - 0.51d, 1.0f, 0.0f);
            }
            else if (tip.face == 3) {
                tessellator.addVertexWithUV(x - 0.5d, y + 0.5d, z + 0.51d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x - 0.5d, y - 0.5d, z + 0.51d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.5d, y - 0.5d, z + 0.51d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.5d, y + 0.5d, z + 0.51d, 1.0f, 0.0f);
            }
            else if (tip.face == 4) {
                tessellator.addVertexWithUV(x - 0.51d, y + 0.5d, z - 0.5d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x - 0.51d, y - 0.5d, z - 0.5d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.51d, y - 0.5d, z + 0.5d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.51d, y + 0.5d, z + 0.5d, 1.0f, 0.0f);
            }
            else if (tip.face == 5) {
                tessellator.addVertexWithUV(x + 0.51d, y + 0.5d, z + 0.5d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x + 0.51d, y - 0.5d, z + 0.5d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.51d, y - 0.5d, z - 0.5d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.51d, y + 0.5d, z - 0.5d, 1.0f, 0.0f);
            }

            
        }
        tessellator.draw();
        GL11.glPopMatrix();
    }
}
