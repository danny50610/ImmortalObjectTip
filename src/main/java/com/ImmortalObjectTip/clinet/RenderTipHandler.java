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
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTipHandler {
    private static Minecraft mc = Minecraft.getMinecraft();
    
    private static final ResourceLocation TipTexture = new ResourceLocation(ImmortalObjectTip.MOD_ID, "textures/immortal_object_tip.png");
    /** 儲存側邊的Tip*/
    private static List<TipInfo> tipList = Lists.newLinkedList();
    /** 儲存上方和底部的Tip*/
    private static List<TipInfo> billboardTipList = Lists.newLinkedList();
    /** Y軸旋轉矩陣 */
    private static DoubleBuffer billboardYawMatrix = BufferUtils.createDoubleBuffer(16);
    /** X軸旋轉矩陣 */
    private static DoubleBuffer billboardPitchMatrix = BufferUtils.createDoubleBuffer(16);
    
    static {
        billboardYawMatrix.put(5, 1d); billboardYawMatrix.put(15, 1d);
        billboardPitchMatrix.put(0, 1d); billboardPitchMatrix.put(15, 1d);
    }
    
    private static Vec3 aYaw = Vec3.createVectorHelper(0d, 0d, 1d);
    private static Vec3 aPitch = Vec3.createVectorHelper(0d, 1d, 0d);
    
    public static void addTip(TipInfo tip) {
        if (tip.face >= 2) { if(!tipList.contains(tip)) { tipList.add(tip); playerSound(tip); } }
        else { if(!billboardTipList.contains(tip)) { billboardTipList.add(tip); playerSound(tip); } }
    }
    
    double playerX, playerY, playerZ;
    
    //Knowledge: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1433242-solved-forge-rendering-lines-in-the-world
    @SubscribeEvent
    public void renderAllTip(RenderWorldLastEvent event) {        
        mc.renderEngine.bindTexture(TipTexture);
        
        EntityClientPlayerMP player = mc.thePlayer;
        playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;
        
        GL11.glPushMatrix();
        GL11.glTranslated(-playerX, -playerY, -playerZ);
        
        TipInfo tip;
        double x,y,z;
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        tessellator.startDrawingQuads();
        for (Iterator<TipInfo> it = tipList.iterator(); it.hasNext();) {
            tip = it.next();
            x = tip.x + 0.5d; y = tip.y + 0.5d; z = tip.z + 0.5d;
            
            tip.updata(event.partialTicks);
            if (tip.isDisappear()) {
                it.remove();
                playerSound(tip);
            }
            
            if (tip.dim != player.dimension) continue;
            if ((playerX - x) * (playerX - x) + (playerY - y) * (playerY - y) + (playerZ - z) * (playerZ - z) > 25.0d) continue;
            
            double Reduce_Height = getHalfHeight(tip.getHeightRatio());
            
            if (tip.face == 2) {
                setColorOpaque(tip, ForgeDirection.NORTH);
                tessellator.addVertexWithUV(x + 0.5d, y + Reduce_Height, z - 0.51d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x + 0.5d, y - Reduce_Height, z - 0.51d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.5d, y - Reduce_Height, z - 0.51d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.5d, y + Reduce_Height, z - 0.51d, 1.0f, 0.0f);
            }
            else if (tip.face == 3) {
                setColorOpaque(tip, ForgeDirection.SOUTH);
                tessellator.addVertexWithUV(x - 0.5d, y + Reduce_Height, z + 0.51d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x - 0.5d, y - Reduce_Height, z + 0.51d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.5d, y - Reduce_Height, z + 0.51d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x + 0.5d, y + Reduce_Height, z + 0.51d, 1.0f, 0.0f);
            }
            else if (tip.face == 4) {
                setColorOpaque(tip, ForgeDirection.WEST);
                tessellator.addVertexWithUV(x - 0.51d, y + Reduce_Height, z - 0.5d, 0.0f, 0.0f);
                tessellator.addVertexWithUV(x - 0.51d, y - Reduce_Height, z - 0.5d, 0.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.51d, y - Reduce_Height, z + 0.5d, 1.0f, 1.0f);
                tessellator.addVertexWithUV(x - 0.51d, y + Reduce_Height, z + 0.5d, 1.0f, 0.0f);
            }
            else if (tip.face == 5) {
                setColorOpaque(tip, ForgeDirection.EAST);
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
                playerSound(tip);
            }
            
            if (tip.dim != player.dimension) continue;
            if ((playerX - x) * (playerX - x) + (playerY - y) * (playerY - y) + (playerZ - z) * (playerZ - z) > 25.0d) continue;
                        
            GL11.glPushMatrix();
            
            tessellator.startDrawingQuads();
            
            double halfHeight = getHalfHeight(tip.getHeightRatio());
            
            GL11.glTranslated(x, y + tip.face, z);

            updataBillboardYawMatrix(x, y, z);
            GL11.glMultMatrix(billboardYawMatrix);
            
            if (tip.face == 1) {
                updataBillboardPitchMatrix(x, y + 1, z);
                GL11.glMultMatrix(billboardPitchMatrix);
                setColorOpaque(tip, ForgeDirection.UP);
                tessellator.addVertexWithUV(-0.5d, +halfHeight, 0.0d, 0.0d, 0.0d);
                tessellator.addVertexWithUV(-0.5d, -halfHeight, 0.0d, 0.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, -halfHeight, 0.0d, 1.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, +halfHeight, 0.0d, 1.0d, 0.0d);
            }
            else if (tip.face == 0) {
                setColorOpaque(tip, ForgeDirection.DOWN);
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
    
    public void updataBillboardYawMatrix(double x, double y, double z) {
        Vec3 b = Vec3.createVectorHelper(playerX - x, 0d, playerZ - z).normalize();
        
        //              b - aYaw
        double c_len = aYaw.subtract(b).lengthVector();
        
        double cos = (c_len * c_len - 2) / -2d;
        double sin = aYaw.crossProduct(b).lengthVector();
        
        if (playerX < x) sin = -sin;
        
        billboardYawMatrix.put( 0, cos); billboardYawMatrix.put( 2, -sin);
        billboardYawMatrix.put( 8, sin); billboardYawMatrix.put(10,  cos);
    }
    
    public void updataBillboardPitchMatrix(double x, double y, double z) {
        Vec3 b = Vec3.createVectorHelper(playerX - x, playerY - y, playerZ - z).normalize();
        
        //              b - aPitch
        double c_len = aPitch.subtract(b).lengthVector();
        
        //Complementary angles(餘角)
        double cos_C = (c_len * c_len - 2) / -2d;
        double sin_C = aPitch.crossProduct(b).lengthVector();
        
        //原矩陣，負號是因為角度要乘負號
        //billboardPitchMatrix.put( 5,  cos); billboardPitchMatrix.put( 6, sin);
        //billboardPitchMatrix.put( 9, -sin); billboardPitchMatrix.put(10, cos);
        billboardPitchMatrix.put( 5,  sin_C); billboardPitchMatrix.put( 6, -cos_C);
        billboardPitchMatrix.put( 9,  cos_C); billboardPitchMatrix.put(10,  sin_C);
    }
    
    private static final float Interval = 0.05f; 
    
    public float getHalfHeight(float ratio) {
        return ratio < Interval ? MathHelper.sin(ratio * ((float)Math.PI / 2 / Interval)) * 0.5f : // 0 ~ Interval
            ratio > 1 - Interval ? MathHelper.sin((1 - ratio) * ((float)Math.PI / 2 / Interval)) * 0.5f: // Interval ~ 1
                0.5f;
    }
    
    public static void playerSound(TipInfo tip) {
        mc.theWorld.playSound(tip.x + 0.5d, tip.y + 0.5d, tip.z + 0.5d, "note.harp", 3.0f, 1.414f, false);
    }
    /** 調整明暗
     *  (雖然名稱像是調顏色，但執行卻像是調明暗)
     */
    public void setColorOpaque(TipInfo tip, ForgeDirection dir) {
        IBlockAccess blockAccess = mc.thePlayer.worldObj;
        Block block = blockAccess.getBlock(tip.x, tip.y, tip.z);
        int light = block.getMixedBrightnessForBlock(blockAccess, tip.x + dir.offsetX, tip.y + dir.offsetY, tip.z + dir.offsetZ);
        int l1 = light >> 20;
        int l2 = (light >> 4) & 0xF;
        int l3 = (l1 > l2 ? l1 : l2) * 17;
        Tessellator.instance.setColorOpaque(l3, l3, l3);
    }
    
}
