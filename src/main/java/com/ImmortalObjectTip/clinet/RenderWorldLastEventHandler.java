package com.ImmortalObjectTip.clinet;

import java.nio.DoubleBuffer;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import com.ImmortalObjectTip.ImmortalObjectTip;
import com.ImmortalObjectTip.TipInfo;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWorldLastEventHandler {
    private static Minecraft mc = Minecraft.getMinecraft();
    
    private static final ResourceLocation texture = new ResourceLocation(ImmortalObjectTip.MOD_ID, "textures/immortal_object_tip.png");
    
    private static List<TipInfo> tipList = Lists.newLinkedList();
    
    private static List<TipInfo> billboardTipList = Lists.newLinkedList();
    
    private static DoubleBuffer billboardYawMatrix = BufferUtils.createDoubleBuffer(16);
    
    private static DoubleBuffer billboardPitchMatrix = BufferUtils.createDoubleBuffer(16);
    
    static {
        billboardYawMatrix.put(5, 1d); billboardYawMatrix.put(15, 1d);
        billboardPitchMatrix.put(0, 1d); billboardPitchMatrix.put(15, 1d);
    }
    
    private static Vec3 aYaw = Vec3.createVectorHelper(0d, 0d, 1d);
    private static Vec3 aPitch = Vec3.createVectorHelper(0d, 1d, 0d);
    
    public static void addTip(TipInfo info) {
        if (info.face >= 2) {
            if(!tipList.contains(info)) {
                tipList.add(info);
                mc.theWorld.playSound(info.x + 0.5d, info.y + 0.5d, info.z + 0.5d, "note.harp", 3.0f, 1.414f, false);
            }
        }
        else {
            if(!billboardTipList.contains(info)) {
                billboardTipList.add(info);
                mc.theWorld.playSound(info.x + 0.5d, info.y + 0.5d, info.z + 0.5d, "note.harp", 3.0f, 1.414f, false);
            }
        }
    }
    
    //Knowledge: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1433242-solved-forge-rendering-lines-in-the-world
    @SubscribeEvent
    public void RenderWorldLast(RenderWorldLastEvent event) {        

        mc.renderEngine.bindTexture(texture);
        
        EntityClientPlayerMP player = mc.thePlayer;
        IBlockAccess blockAccess = player.worldObj; 
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;
        
        GL11.glPushMatrix();
        GL11.glTranslated(-playerX, -playerY, -playerZ);
        
        TipInfo tip;
        double x,y,z;
        int light;
        Block block;
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        tessellator.startDrawingQuads();
        //tessellator.setNormal(0, 1, 0);
        for (Iterator<TipInfo> it = tipList.iterator(); it.hasNext();) {
            tip = it.next();
            x = tip.x + 0.5d; y = tip.y + 0.5d; z = tip.z + 0.5d;
            
            tip.updata(event.partialTicks);
            if (tip.isDisappear()) {
                it.remove();
                mc.theWorld.playSound(x, y, z, "note.harp", 3.0f, 1.414f, false);
            }
            
            if (tip.dim != player.dimension) continue;
            if ((playerX - x) * (playerX - x) + (playerY - y) * (playerY - y) + (playerZ - z) * (playerZ - z) > 25.0d) continue;
            
            double Reduce_Height = getHalfHeight(tip.getHeightRatio());
            
            block = blockAccess.getBlock(tip.x, tip.y, tip.z);
            light = block.getMixedBrightnessForBlock(blockAccess, tip.x, tip.y, tip.z);
            
            tessellator.setBrightness(light);
            
            System.out.println(light);
            
            if (tip.face == 2) {
                tessellator.addVertexWithUV(x + 0.5d, y + Reduce_Height, z - 0.51d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x + 0.5d, y - Reduce_Height, z - 0.51d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.5d, y - Reduce_Height, z - 0.51d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.5d, y + Reduce_Height, z - 0.51d, 1.0f, 0.0f);
            }
            else if (tip.face == 3) {
                tessellator.addVertexWithUV(x - 0.5d, y + Reduce_Height, z + 0.51d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x - 0.5d, y - Reduce_Height, z + 0.51d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.5d, y - Reduce_Height, z + 0.51d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.5d, y + Reduce_Height, z + 0.51d, 1.0f, 0.0f);
            }
            else if (tip.face == 4) {
                tessellator.addVertexWithUV(x - 0.51d, y + Reduce_Height, z - 0.5d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x - 0.51d, y - Reduce_Height, z - 0.5d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.51d, y - Reduce_Height, z + 0.5d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.51d, y + Reduce_Height, z + 0.5d, 1.0f, 0.0f);
            }
            else if (tip.face == 5) {
                tessellator.addVertexWithUV(x + 0.51d, y + Reduce_Height, z + 0.5d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x + 0.51d, y - Reduce_Height, z + 0.5d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.51d, y - Reduce_Height, z - 0.5d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.51d, y + Reduce_Height, z - 0.5d, 1.0f, 0.0f);
            }
            
        }
        tessellator.draw();
        

        for (Iterator<TipInfo> it = billboardTipList.iterator(); it.hasNext();) {
            tip = it.next();
            x = tip.x + 0.5d; y = tip.y + 0.5d; z = tip.z + 0.5d;
            
            tip.updata(event.partialTicks);
            if (tip.isDisappear()) {
                it.remove();
                mc.theWorld.playSound(x, y, z, "note.harp", 3.0f, 1.414f, false);
            }
            
            if (tip.dim != player.dimension) continue;
            if ((playerX - x) * (playerX - x) + (playerY - y) * (playerY - y) + (playerZ - z) * (playerZ - z) > 25.0d) continue;
                        
            GL11.glPushMatrix();
            
            tessellator.startDrawingQuads();
            
            
            double halfHeight = getHalfHeight(tip.getHeightRatio());
            
            block = blockAccess.getBlock(tip.x, tip.y, tip.z);
//            light = block.getMixedBrightnessForBlock(blockAccess, tip.x, tip.y + 1, tip.z);
//            
//            tessellator.setBrightness(light);
//            //tessellator.setColorOpaque_F(0.5F, 0.5F, 0.5F);
            
            //System.out.println(light);
            
            //float f = block.getLightOpacity(blockAccess, tip.x, tip.y, tip.z);
            light = block.getMixedBrightnessForBlock(blockAccess, tip.x, tip.y + 1, tip.z);
            int l1 = light >> 20;
            int l2 = (light & 0xF0) >> 4;
            int l3 = (l1 > l2 ? l1 : l2) * 17;
            tessellator.setColorOpaque(l3, l3, l3);
            System.out.println(l3);
            //float l3 = (l1 * 16 + l2 * 16) / 512.0f;
            //tessellator.setColorOpaque_F(l3, l3, l3);
            //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)l1, (float)l2); 
            //tessellator.setBrightness(light);
            
            GL11.glTranslated(x, y + tip.face, z);

            updataBillboardYawMatrix(playerX, playerY, playerZ, x, y, z);
            GL11.glMultMatrix(billboardYawMatrix);
            
            if (tip.face == 1) {
                updataBillboardPitchMatrix(playerX, playerY, playerZ, x, y + 1, z);
                GL11.glMultMatrix(billboardPitchMatrix);
                tessellator.addVertexWithUV(-0.5d, +halfHeight, 0.0d, 0.0d, 0.0d);
                tessellator.addVertexWithUV(-0.5d, -halfHeight, 0.0d, 0.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, -halfHeight, 0.0d, 1.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, +halfHeight, 0.0d, 1.0d, 0.0d);
            }
            else if (tip.face == 0) {
                tessellator.addVertexWithUV(-0.5d, -0.51d,  halfHeight, 0.0d, 0.0d);
                tessellator.addVertexWithUV(-0.5d, -0.51d, -halfHeight, 0.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, -0.51d, -halfHeight, 1.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, -0.51d,  halfHeight, 1.0d, 0.0d);
            }
            tessellator.draw();
            
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }
    
    public void updataBillboardYawMatrix(double playerX, double playerY, double playerZ, double x, double y, double z) {
        Vec3 b = Vec3.createVectorHelper(playerX - x, 0d, playerZ - z).normalize();
        
        double c_len = aYaw.subtract(b).lengthVector();
        
        double cos = (c_len * c_len - 2) / -2d;
        double sin = aYaw.crossProduct(b).lengthVector();
        
        if (playerX < x) sin = -sin;
        
        billboardYawMatrix.put( 0, cos); billboardYawMatrix.put( 2, -sin);
        billboardYawMatrix.put( 8, sin); billboardYawMatrix.put(10,  cos);
    }
    
    public void updataBillboardPitchMatrix(double playerX, double playerY, double playerZ, double x, double y, double z) {
        Vec3 b = Vec3.createVectorHelper(playerX - x, playerY - y, playerZ - z).normalize();
        
        double c_len = aPitch.subtract(b).lengthVector();
        
        double cos = (c_len * c_len - 2) / -2d;
        double sin = aPitch.crossProduct(b).lengthVector();
        
        //billboardPitchMatrix.put( 5,  cos); billboardPitchMatrix.put( 6, sin);
        //billboardPitchMatrix.put( 9, -sin); billboardPitchMatrix.put(10, cos);
        billboardPitchMatrix.put( 5,  sin); billboardPitchMatrix.put( 6, -cos);
        billboardPitchMatrix.put( 9,  cos); billboardPitchMatrix.put(10,  sin);
    }
    
    private static final float Interval = 0.05f; 
    
    public float getHalfHeight(float ratio) {
        return ratio < Interval ? MathHelper.sin(ratio * ((float)Math.PI / 2 / Interval)) * 0.5f :
            ratio > 1 - Interval ? MathHelper.sin((1 - ratio) * ((float)Math.PI / 2 / Interval)) * 0.5f:
                0.5f;
    }
    
}
