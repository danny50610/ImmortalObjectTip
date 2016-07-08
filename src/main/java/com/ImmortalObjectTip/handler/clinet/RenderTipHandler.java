package com.ImmortalObjectTip.handler.clinet;

import com.ImmortalObjectTip.ModInformation;
import com.ImmortalObjectTip.TipInfoBase;
import com.ImmortalObjectTip.TipInfoBlock;
import com.ImmortalObjectTip.TipInfoEntity;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderTipHandler {
    private static Minecraft mc = Minecraft.getMinecraft();

    private static final ResourceLocation TipTexture = new ResourceLocation(ModInformation.MOD_ID, "textures/immortal_object_tip.png");

    private static List<TipInfoBlock> tipBlockList = Lists.newLinkedList();

    private static List<TipInfoEntity> tipPlayerList = Lists.newLinkedList();

    // Y軸旋轉矩陣
    private static DoubleBuffer billboardYawMatrix = BufferUtils.createDoubleBuffer(16);
    // X軸旋轉矩陣
    private static DoubleBuffer billboardPitchMatrix = BufferUtils.createDoubleBuffer(16);

    static {
        billboardYawMatrix.put(5, 1d);
        billboardYawMatrix.put(15, 1d);
        billboardPitchMatrix.put(0, 1d);
        billboardPitchMatrix.put(15, 1d);
    }

    private static Vec3 aYaw = Vec3.createVectorHelper(0d, 0d, 1d);
    private static Vec3 aPitch = Vec3.createVectorHelper(0d, 1d, 0d);

    public static void addTipBlock(TipInfoBlock tip) {
        if (!tipBlockList.contains(tip)) {
            tipBlockList.add(tip);
            playTipSound(tip.x, tip.y, tip.z);
        }
    }

    public static void addTipPlayer(TipInfoEntity tip) {
        if (!tipPlayerList.contains(tip) && tip.entity != null) {
            tipPlayerList.add(tip);

            playTipSound(tip.entity.posX, tip.entity.posY, tip.entity.posZ);
        }
    }

    private double playerX, playerY, playerZ;

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

        renderTipBlock(player, event);

        renderTipEntity(player, event);

        GL11.glPopMatrix();
    }

    private void renderTipBlock(EntityPlayer player, RenderWorldLastEvent event) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        for (Iterator<TipInfoBlock> it = tipBlockList.iterator(); it.hasNext(); ) {
            TipInfoBlock tip = it.next();
            double x = tip.x + 0.5d, y = tip.y + 0.5d, z = tip.z + 0.5d;

            tip.update(event.partialTicks);
            if (tip.isDisappear()) {
                it.remove();
                playTipSound(x, y, z);
            }

            if (tip.dim != player.dimension) continue;
            if (!canRender(x, y, z))
                continue;

            double Reduce_Height = getHalfHeight(tip.getHeightRatio());

            if (tip.type == TipInfoBase.Type.BlockTop) {
                GL11.glPushMatrix();

                tessellator.startDrawingQuads();

                double halfHeight = getHalfHeight(tip.getHeightRatio());

                GL11.glTranslated(x, y, z);

                updateBillboardYawMatrix(x, y, z);
                GL11.glMultMatrix(billboardYawMatrix);

                updateBillboardPitchMatrix(x, y, z);
                GL11.glMultMatrix(billboardPitchMatrix);

                setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.UP, getAlpha(tip));
                tessellator.addVertexWithUV(-0.5d, +halfHeight, 0.0d, 0.0d, 0.0d);
                tessellator.addVertexWithUV(-0.5d, -halfHeight, 0.0d, 0.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, -halfHeight, 0.0d, 1.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, +halfHeight, 0.0d, 1.0d, 0.0d);

                GL11.glEnable(GL11.GL_BLEND);
                tessellator.draw();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
            else if (tip.type == TipInfoBase.Type.BlockSide) {
                tessellator.startDrawingQuads();
                if (tip.face == 2) {
                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.NORTH, getAlpha(tip));
                    tessellator.addVertexWithUV(x + 0.5d, y + Reduce_Height, z - 0.51d, 0.0f, 0.0f);
                    tessellator.addVertexWithUV(x + 0.5d, y - Reduce_Height, z - 0.51d, 0.0f, 1.0f);
                    tessellator.addVertexWithUV(x - 0.5d, y - Reduce_Height, z - 0.51d, 1.0f, 1.0f);
                    tessellator.addVertexWithUV(x - 0.5d, y + Reduce_Height, z - 0.51d, 1.0f, 0.0f);
                }
                else if (tip.face == 3) {
                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.SOUTH, getAlpha(tip));
                    tessellator.addVertexWithUV(x - 0.5d, y + Reduce_Height, z + 0.51d, 0.0f, 0.0f);
                    tessellator.addVertexWithUV(x - 0.5d, y - Reduce_Height, z + 0.51d, 0.0f, 1.0f);
                    tessellator.addVertexWithUV(x + 0.5d, y - Reduce_Height, z + 0.51d, 1.0f, 1.0f);
                    tessellator.addVertexWithUV(x + 0.5d, y + Reduce_Height, z + 0.51d, 1.0f, 0.0f);
                }
                else if (tip.face == 4) {
                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.WEST, getAlpha(tip));
                    tessellator.addVertexWithUV(x - 0.51d, y + Reduce_Height, z - 0.5d, 0.0f, 0.0f);
                    tessellator.addVertexWithUV(x - 0.51d, y - Reduce_Height, z - 0.5d, 0.0f, 1.0f);
                    tessellator.addVertexWithUV(x - 0.51d, y - Reduce_Height, z + 0.5d, 1.0f, 1.0f);
                    tessellator.addVertexWithUV(x - 0.51d, y + Reduce_Height, z + 0.5d, 1.0f, 0.0f);
                }
                else if (tip.face == 5) {
                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.EAST, getAlpha(tip));
                    tessellator.addVertexWithUV(x + 0.51d, y + Reduce_Height, z + 0.5d, 0.0f, 0.0f);
                    tessellator.addVertexWithUV(x + 0.51d, y - Reduce_Height, z + 0.5d, 0.0f, 1.0f);
                    tessellator.addVertexWithUV(x + 0.51d, y - Reduce_Height, z - 0.5d, 1.0f, 1.0f);
                    tessellator.addVertexWithUV(x + 0.51d, y + Reduce_Height, z - 0.5d, 1.0f, 0.0f);
                }
                GL11.glEnable(GL11.GL_BLEND);
                tessellator.draw();
                GL11.glDisable(GL11.GL_BLEND);
            }
            else if (tip.type == TipInfoBase.Type.BlockBottom) {
                GL11.glPushMatrix();

                tessellator.startDrawingQuads();

                double halfHeight = getHalfHeight(tip.getHeightRatio());

                GL11.glTranslated(x, y, z);

                updateBillboardYawMatrix(x, y, z);
                GL11.glMultMatrix(billboardYawMatrix);

                setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.DOWN, getAlpha(tip));
                tessellator.addVertexWithUV(-0.5d, -0.51d, halfHeight, 0.0d, 0.0d);
                tessellator.addVertexWithUV(-0.5d, -0.51d, -halfHeight, 0.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, -0.51d, -halfHeight, 1.0d, 1.0d);
                tessellator.addVertexWithUV(+0.5d, -0.51d, halfHeight, 1.0d, 0.0d);

                GL11.glEnable(GL11.GL_BLEND);
                tessellator.draw();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    private void renderTipEntity(EntityPlayer player, RenderWorldLastEvent event) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        for (Iterator<TipInfoEntity> it = tipPlayerList.iterator(); it.hasNext(); ) {
            TipInfoEntity tip = it.next();
            double x = tip.entity.posX;
            double y = tip.entity.posY + tip.entity.height + 1;
            double z = tip.entity.posZ;

            tip.update(event.partialTicks);
            if (tip.isDisappear()) {
                it.remove();
                playTipSound(x, y, z);
            }

            if (tip.dim != player.dimension) continue;
            if (!canRender(x, y, z))
                continue;
            if (tip.entity.equals(player))
                continue;

            GL11.glPushMatrix();

            tessellator.startDrawingQuads();

            double halfHeight = getHalfHeight(tip.getHeightRatio());

            GL11.glTranslated(x, y, z);

            updateBillboardYawMatrix(x, y, z);
            GL11.glMultMatrix(billboardYawMatrix);

            updateBillboardPitchMatrix(x, y, z);
            GL11.glMultMatrix(billboardPitchMatrix);

            setColorOpaque((int)x, (int)y, (int)z, ForgeDirection.UP, getAlpha(tip));
            tessellator.addVertexWithUV(-0.5d, +halfHeight, 0.0d, 0.0d, 0.0d);
            tessellator.addVertexWithUV(-0.5d, -halfHeight, 0.0d, 0.0d, 1.0d);
            tessellator.addVertexWithUV(+0.5d, -halfHeight, 0.0d, 1.0d, 1.0d);
            tessellator.addVertexWithUV(+0.5d, +halfHeight, 0.0d, 1.0d, 0.0d);

            GL11.glEnable(GL11.GL_BLEND);
            tessellator.draw();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    private void updateBillboardYawMatrix(double x, double y, double z) {
        Vec3 b = Vec3.createVectorHelper(playerX - x, 0d, playerZ - z).normalize();

        //              b - aYaw
        double c_len = aYaw.subtract(b).lengthVector();

        double cos = (c_len * c_len - 2) / -2d;
        double sin = aYaw.crossProduct(b).lengthVector();

        if (playerX < x) sin = -sin;

        billboardYawMatrix.put(0, cos);
        billboardYawMatrix.put(2, -sin);
        billboardYawMatrix.put(8, sin);
        billboardYawMatrix.put(10, cos);
    }

    private void updateBillboardPitchMatrix(double x, double y, double z) {
        Vec3 b = Vec3.createVectorHelper(playerX - x, playerY - y, playerZ - z).normalize();

        //              b - aPitch
        double c_len = aPitch.subtract(b).lengthVector();

        //Complementary angles(餘角)
        double cos_C = (c_len * c_len - 2) / -2d;
        double sin_C = aPitch.crossProduct(b).lengthVector();

        //原矩陣，負號是因為角度要乘負號
        //billboardPitchMatrix.put( 5,  cos); billboardPitchMatrix.put( 6, sin);
        //billboardPitchMatrix.put( 9, -sin); billboardPitchMatrix.put(10, cos);
        billboardPitchMatrix.put(5, sin_C);
        billboardPitchMatrix.put(6, -cos_C);
        billboardPitchMatrix.put(9, cos_C);
        billboardPitchMatrix.put(10, sin_C);
    }

    private static final float Interval = 0.05f;

    private float getHalfHeight(float ratio) {
        return ratio < Interval ? MathHelper.sin(ratio * ((float) Math.PI / 2 / Interval)) * 0.5f : // 0 ~ Interval
                ratio > 1 - Interval ? MathHelper.sin((1 - ratio) * ((float) Math.PI / 2 / Interval)) * 0.5f : // Interval ~ 1
                        0.5f;
    }

    private int getAlpha(TipInfoBase tip) {
        return getAlpha(tip.getHeightRatio());
    }

    private int getAlpha(float ratio) {
        return (int) (ratio < Interval ? MathHelper.sin(ratio * ((float) Math.PI / 2 / Interval)) * 224 : // 0 ~ Interval
                ratio > 1 - Interval ? MathHelper.sin((1 - ratio) * ((float) Math.PI / 2 / Interval)) * 224 : // Interval ~ 1
                        224);
    }

    private static void playTipSound(double x, double y, double z) {
        mc.theWorld.playSound(x + 0.5d, y + 0.5d, z + 0.5d, "note.harp", 3.0f, 1.414f, false);
    }

    private boolean canRender(double x, double y, double z) {
        return (playerX - x) * (playerX - x) + (playerY - y) * (playerY - y) + (playerZ - z) * (playerZ - z) <= 5 * 5;
    }

    /**
     * 調整明暗
     * (雖然名稱像是調顏色，但執行卻像是調明暗)
     * FIXME: 突然不管用了
     */
    private void setColorOpaque(int x, int y, int z, ForgeDirection dir, int alpha) {
        IBlockAccess blockAccess = mc.thePlayer.worldObj;
        Block block = blockAccess.getBlock(x, y, z);
        int light = block.getMixedBrightnessForBlock(blockAccess, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
        int l1 = light >> 20;
        int l2 = (light >> 4) & 0xF;
        int l3 = (l1 > l2 ? l1 : l2) * 17;
        Tessellator.instance.setColorRGBA(l3, l3, l3, alpha);
    }

}
