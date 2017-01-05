package com.ImmortalObjectTip.handler.clinet;

import com.ImmortalObjectTip.ModInformation;
import com.ImmortalObjectTip.TipInfoBase;
import com.ImmortalObjectTip.TipInfoBlock;
import com.ImmortalObjectTip.TipInfoEntity;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;

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

    private static Vec3d aYaw = new Vec3d(0d, 0d, 1d);
    private static Vec3d aPitch = new Vec3d(0d, 1d, 0d);

    public static void addTipBlock(TipInfoBlock tip) {
        if (!tipBlockList.contains(tip)) {
            tipBlockList.add(tip);
            playTipSound(tip.pos.getX(), tip.pos.getY(), tip.pos.getZ());
        }
    }

    public static void addTipPlayer(TipInfoEntity tip) {
        if (!tipPlayerList.contains(tip) && tip.entity != null) {
            tipPlayerList.add(tip);

            playTipSound(tip.entity.posX, tip.entity.posY, tip.entity.posZ);
        }
    }

    private double playerX, playerY, playerZ;
    private double eyeHeight;

    // Knowledge: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1433242-solved-forge-rendering-lines-in-the-world
    @SubscribeEvent
    public void renderAllTip(RenderWorldLastEvent event) {
        mc.renderEngine.bindTexture(TipTexture);

        EntityPlayerSP player = mc.thePlayer;
        playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
        playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
        playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
        eyeHeight = player.eyeHeight;

        GL11.glPushMatrix();
        GL11.glTranslated(-playerX, -playerY, -playerZ);

        renderTipBlock(player, event);

        renderTipEntity(player, event);

        GL11.glPopMatrix();
    }

    // Knowledge: http://www.minecraftforge.net/forum/index.php?topic=41475.0
    private void renderTipBlock(EntityPlayer player, RenderWorldLastEvent event) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = Tessellator.getInstance().getBuffer();
        // vertexBuffer.setTranslation(1.0F, 1.0F, 1.0F);

        for (Iterator<TipInfoBlock> it = tipBlockList.iterator(); it.hasNext(); ) {
            TipInfoBlock tip = it.next();
            double x = tip.pos.getX() + 0.5d;
            double y = tip.pos.getY() + 0.5d;
            double z = tip.pos.getZ() + 0.5d;

            tip.update(event.getPartialTicks());
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

                vertexBuffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);

                double halfHeight = getHalfHeight(tip.getHeightRatio());

                GL11.glTranslated(x, y, z);

                updateBillboardYawMatrix(x, y, z);
                GL11.glMultMatrix(billboardYawMatrix);

                updateBillboardPitchMatrix(x, y, z);
                GL11.glMultMatrix(billboardPitchMatrix);

//                setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.UP, getAlpha(tip));
                vertexBuffer.pos(-0.5d, +halfHeight, 0.0d).tex(0.0d, 0.0d).endVertex();
                vertexBuffer.pos(-0.5d, -halfHeight, 0.0d).tex(0.0d, 1.0d).endVertex();
                vertexBuffer.pos(+0.5d, -halfHeight, 0.0d).tex(1.0d, 1.0d).endVertex();
                vertexBuffer.pos(+0.5d, +halfHeight, 0.0d).tex(1.0d, 0.0d).endVertex();

                GL11.glEnable(GL11.GL_BLEND);
                tessellator.draw();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
            else if (tip.type == TipInfoBase.Type.BlockSide) {
                vertexBuffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                if (tip.face == EnumFacing.NORTH) {
//                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.NORTH, getAlpha(tip));
                    vertexBuffer.pos(x + 0.5d, y + Reduce_Height, z - 0.51d).tex(0.0f, 0.0f).endVertex();
                    vertexBuffer.pos(x + 0.5d, y - Reduce_Height, z - 0.51d).tex(0.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x - 0.5d, y - Reduce_Height, z - 0.51d).tex(1.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x - 0.5d, y + Reduce_Height, z - 0.51d).tex(1.0f, 0.0f).endVertex();
                }
                else if (tip.face == EnumFacing.SOUTH) {
//                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.SOUTH, getAlpha(tip));
                    vertexBuffer.pos(x - 0.5d, y + Reduce_Height, z + 0.51d).tex(0.0f, 0.0f).endVertex();
                    vertexBuffer.pos(x - 0.5d, y - Reduce_Height, z + 0.51d).tex(0.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x + 0.5d, y - Reduce_Height, z + 0.51d).tex(1.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x + 0.5d, y + Reduce_Height, z + 0.51d).tex(1.0f, 0.0f).endVertex();
                }
                else if (tip.face == EnumFacing.WEST) {
//                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.WEST, getAlpha(tip));
                    vertexBuffer.pos(x - 0.51d, y + Reduce_Height, z - 0.5d).tex(0.0f, 0.0f).endVertex();
                    vertexBuffer.pos(x - 0.51d, y - Reduce_Height, z - 0.5d).tex(0.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x - 0.51d, y - Reduce_Height, z + 0.5d).tex(1.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x - 0.51d, y + Reduce_Height, z + 0.5d).tex(1.0f, 0.0f).endVertex();
                }
                else if (tip.face == EnumFacing.EAST) {
//                    setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.EAST, getAlpha(tip));
                    vertexBuffer.pos(x + 0.51d, y + Reduce_Height, z + 0.5d).tex(0.0f, 0.0f).endVertex();
                    vertexBuffer.pos(x + 0.51d, y - Reduce_Height, z + 0.5d).tex(0.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x + 0.51d, y - Reduce_Height, z - 0.5d).tex(1.0f, 1.0f).endVertex();
                    vertexBuffer.pos(x + 0.51d, y + Reduce_Height, z - 0.5d).tex(1.0f, 0.0f).endVertex();
                }
                GL11.glEnable(GL11.GL_BLEND);
                tessellator.draw();
                GL11.glDisable(GL11.GL_BLEND);
            }
            else if (tip.type == TipInfoBase.Type.BlockBottom) {
                GL11.glPushMatrix();

                vertexBuffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);

                double halfHeight = getHalfHeight(tip.getHeightRatio());

                GL11.glTranslated(x, y, z);

                updateBillboardYawMatrix(x, y, z);
                GL11.glMultMatrix(billboardYawMatrix);

//                setColorOpaque((int) tip.x, (int) tip.y, (int) tip.z, ForgeDirection.DOWN, getAlpha(tip));
                vertexBuffer.pos(-0.5d, -0.51d, +halfHeight).tex(0.0d, 0.0d).endVertex();
                vertexBuffer.pos(-0.5d, -0.51d, -halfHeight).tex(0.0d, 1.0d).endVertex();
                vertexBuffer.pos(+0.5d, -0.51d, -halfHeight).tex(1.0d, 1.0d).endVertex();
                vertexBuffer.pos(+0.5d, -0.51d, +halfHeight).tex(1.0d, 0.0d).endVertex();

                GL11.glEnable(GL11.GL_BLEND);
                tessellator.draw();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    private void renderTipEntity(EntityPlayer player, RenderWorldLastEvent event) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = Tessellator.getInstance().getBuffer();
//        vertexBuffer.putColorRGBA(1,1,1)

        for (Iterator<TipInfoEntity> it = tipPlayerList.iterator(); it.hasNext(); ) {
            TipInfoEntity tip = it.next();
            double x = tip.entity.posX;
            double y = tip.entity.posY + tip.entity.height + 1;
            double z = tip.entity.posZ;

            tip.update(event.getPartialTicks());
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

            vertexBuffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            double halfHeight = getHalfHeight(tip.getHeightRatio());

            GL11.glTranslated(x, y, z);

            updateBillboardYawMatrix(x, y, z);
            GL11.glMultMatrix(billboardYawMatrix);

            updateBillboardPitchMatrix(x, y, z);
            GL11.glMultMatrix(billboardPitchMatrix);

//            setColorOpaque((int) x, (int) y, (int) z, ForgeDirection.UP, getAlpha(tip));
            vertexBuffer.pos(-0.5d, +halfHeight, 0.0d).tex(0.0d, 0.0d).endVertex();
            vertexBuffer.pos(-0.5d, -halfHeight, 0.0d).tex(0.0d, 1.0d).endVertex();
            vertexBuffer.pos(+0.5d, -halfHeight, 0.0d).tex(1.0d, 1.0d).endVertex();
            vertexBuffer.pos(+0.5d, +halfHeight, 0.0d).tex(1.0d, 0.0d).endVertex();

            GL11.glEnable(GL11.GL_BLEND);
            tessellator.draw();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    private void updateBillboardYawMatrix(double x, double y, double z) {
        Vec3d b = new Vec3d(playerX - x, 0d, playerZ - z).normalize();

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
        Vec3d b = new Vec3d(playerX - x, playerY + eyeHeight - y, playerZ - z).normalize();

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
        mc.theWorld.playSound(x + 0.5d, y + 0.5d, z + 0.5d, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 3.0f, 1.414f, false);
    }

    private boolean canRender(double x, double y, double z) {
        return (playerX - x) * (playerX - x) + (playerY - y) * (playerY - y) + (playerZ - z) * (playerZ - z) <= 5 * 5;
    }

//    /**
//     * 調整明暗
//     * (雖然名稱像是調顏色，但執行卻像是調明暗)
//     * FIXME: 突然不管用了
//     */
//    private void setColorOpaque(int x, int y, int z, ForgeDirection dir, int alpha) {
//        IBlockAccess blockAccess = mc.thePlayer.worldObj;
//        Block block = blockAccess.getBlock(x, y, z);
//        int light = block.getMixedBrightnessForBlock(blockAccess, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
//        int l1 = light >> 20;
//        int l2 = (light >> 4) & 0xF;
//        int l3 = (l1 > l2 ? l1 : l2) * 17;
//        Tessellator.instance.setColorRGBA(l3, l3, l3, alpha);
//    }

}
