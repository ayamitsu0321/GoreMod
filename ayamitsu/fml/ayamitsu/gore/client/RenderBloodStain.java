package ayamitsu.gore.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ayamitsu.gore.common.EntityBloodStain;

public class RenderBloodStain extends Render
{
	public RenderBloodStain() {}

	public void doRenderNode(Entity entity, double d, double d1, double d2, float f, float f1)
    {
    	EntityBloodStain stain = (EntityBloodStain)entity;

    	if (stain.type != 1)
        {
        	String texture = stain.getEntity() != null && stain.getEntity().getBloodTexture() != null ? stain.getEntity().getBloodTexture() : "/ayamitsu/gore/misc/blood_red.png";
            this.renderImage(entity, d, d1, d2, f, f1, texture);
        }
    }

	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		this.shadowSize = 1.0F;
        GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		int var10 = entity.getBrightnessForRender(f1);
		int var11 = var10 % 65536;
        int var12 = var10 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 / 1.0F, var12 / 1.0F);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.doRenderNode(entity, d, d1, d2, f, f1);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		this.shadowSize = 0.0F;
		GL11.glPopMatrix();
	}

	private void renderImage(Entity entity, double d, double d1, double d2, float f, float f1, String s)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	RenderEngine renderengine = this.renderManager.renderEngine;
    	renderengine.bindTexture(renderengine.getTexture(s));//this.loadTexture(s);
        World world = this.getWorldFromRenderManager();
        GL11.glDepthMask(false);
        //RenderHelper.disableStandardItemLighting();//
    	RenderHelper.enableStandardItemLighting();
    	float f2 = this.shadowSize;

        double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f1;
        double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f1 + (double)entity.getShadowSize();
        double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f1;
        int i = MathHelper.floor_double(d3 - f2);
        int j = MathHelper.floor_double(d3 + f2);
        int k = MathHelper.floor_double(d4 - f2);
        int l = MathHelper.floor_double(d4);
        int i1 = MathHelper.floor_double(d5 - f2);
        int j1 = MathHelper.floor_double(d5 + f2);
        double d6 = d - d3;
        double d7 = d1 - d4;
        double d8 = d2 - d5;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        double d9 = ((EntityBloodStain)entity).strength / 100D;

        for (int k1 = i; k1 <= j; k1++)
        {
            for (int l1 = k; l1 <= l; l1++)
            {
                for (int i2 = i1; i2 <= j1; i2++)
                {
                    int j2 = world.getBlockId(k1, l1 - 1, i2);

                    if (j2 > 0 && world.getBlockLightValue(k1, l1, i2) > 3)
                    {
                        renderImageOnBlock(Block.blocksList[j2], d, d1 + (double)entity.getShadowSize(), d2, k1, l1, i2, f, f2, d6, d7 + (double)entity.getShadowSize(), d8, d9);
                    }
                }
            }
        }

        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	//RenderHelper.enableStandardItemLighting();//
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

	private World getWorldFromRenderManager()
    {
        return renderManager.worldObj;
    }

	private void renderImageOnBlock(Block block, double d, double d1, double d2, int i, int j, int k, float f, float f1, double d3, double d4, double d5, double d6)
    {
        Tessellator tessellator = Tessellator.instance;

    	if (!block.renderAsNormalBlock())
        {
        	return;
        }

        double d7 = d6;
    	//double d7 = ((double)f - (d1 - ((double)j + d5)) / 2.0D) * 0.5D * (double)this.getWorldFromRenderManager().getLightBrightness(i, j, k);

        if (d7 > 1.0D)
        {
            d7 = 1.0D;
        }

        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d7);
        double d8 = (double)i + block.getBlockBoundsMinX() + d3;
        double d9 = (double)i + block.getBlockBoundsMaxX() + d3;
        double d10 = (double)j + block.getBlockBoundsMinY() + d4 + 0.015625D;
        double d11 = (double)k + block.getBlockBoundsMinZ() + d5;
        double d12 = (double)k + block.getBlockBoundsMaxZ() + d5;
        float f2 = (float)((d - d8) / 2D / f1 + 0.5D);
        float f3 = (float)((d - d9) / 2D / f1 + 0.5D);
        float f4 = (float)((d2 - d11) / 2D / f1 + 0.5D);
        float f5 = (float)((d2 - d12) / 2D / f1 + 0.5D);
        tessellator.addVertexWithUV(d8, d10, d11, f2, f4);
        tessellator.addVertexWithUV(d8, d10, d12, f2, f5);
        tessellator.addVertexWithUV(d9, d10, d12, f3, f5);
        tessellator.addVertexWithUV(d9, d10, d11, f3, f4);
    }

    public static void renderOffsetAABB(AxisAlignedBB axisalignedbb, double d, double d1, double d2, boolean flag)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.instance;

        if (flag)
        {
            GL11.glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
        }
        else
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        tessellator.startDrawingQuads();
        tessellator.setTranslation(d, d1, d2);
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.setNormal(0.0F, -1F, 0.0F);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}