package net.minecraft.client.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.pyr0byte.vapid.StaticVapid;
import me.pyr0byte.vapid.events.BlockRenderedEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.TesselatorVertexState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.lwjgl.opengl.GL11;

public class WorldRenderer
{
    private TesselatorVertexState vertexState;

    /** Reference to the World object. */
    public World worldObj;
    private int glRenderList = -1;
    private static Tessellator tessellator = Tessellator.instance;
    public static int chunksUpdated;
    public int posX;
    public int posY;
    public int posZ;

    /** Pos X minus */
    public int posXMinus;

    /** Pos Y minus */
    public int posYMinus;

    /** Pos Z minus */
    public int posZMinus;

    /** Pos X clipped */
    public int posXClip;

    /** Pos Y clipped */
    public int posYClip;

    /** Pos Z clipped */
    public int posZClip;
    public boolean isInFrustum;

    /** Should this renderer skip this render pass */
    public boolean[] skipRenderPass = new boolean[2];

    /** Pos X plus */
    public int posXPlus;

    /** Pos Y plus */
    public int posYPlus;

    /** Pos Z plus */
    public int posZPlus;

    /** Boolean for whether this renderer needs to be updated or not */
    public boolean needsUpdate;

    /** Axis aligned bounding box */
    public AxisAlignedBB rendererBoundingBox;

    /** Chunk index */
    public int chunkIndex;

    /** Is this renderer visible according to the occlusion query */
    public boolean isVisible = true;

    /** Is this renderer waiting on the result of the occlusion query */
    public boolean isWaitingOnOcclusionQuery;

    /** OpenGL occlusion query */
    public int glOcclusionQuery;

    /** Is the chunk lit */
    public boolean isChunkLit;
    private boolean isInitialized;
    public List tileEntityRenderers = new ArrayList();
    private List tileEntities;

    /** Bytes sent to the GPU */
    private int bytesDrawn;
    private static final String __OBFID = "CL_00000942";

    public WorldRenderer(World par1World, List par2List, int par3, int par4, int par5, int par6)
    {
        this.worldObj = par1World;
        this.vertexState = null;
        this.tileEntities = par2List;
        this.glRenderList = par6;
        this.posX = -999;
        this.setPosition(par3, par4, par5);
        this.needsUpdate = false;
    }

    /**
     * Sets a new position for the renderer and setting it up so it can be reloaded with the new data for that position
     */
    public void setPosition(int par1, int par2, int par3)
    {
        if (par1 != this.posX || par2 != this.posY || par3 != this.posZ)
        {
            this.setDontDraw();
            this.posX = par1;
            this.posY = par2;
            this.posZ = par3;
            this.posXPlus = par1 + 8;
            this.posYPlus = par2 + 8;
            this.posZPlus = par3 + 8;
            this.posXClip = par1 & 1023;
            this.posYClip = par2;
            this.posZClip = par3 & 1023;
            this.posXMinus = par1 - this.posXClip;
            this.posYMinus = par2 - this.posYClip;
            this.posZMinus = par3 - this.posZClip;
            float var4 = 6.0F;
            this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)par1 - var4), (double)((float)par2 - var4), (double)((float)par3 - var4), (double)((float)(par1 + 16) + var4), (double)((float)(par2 + 16) + var4), (double)((float)(par3 + 16) + var4));
            GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
            RenderItem.renderAABB(AxisAlignedBB.getAABBPool().getAABB((double)((float)this.posXClip - var4), (double)((float)this.posYClip - var4), (double)((float)this.posZClip - var4), (double)((float)(this.posXClip + 16) + var4), (double)((float)(this.posYClip + 16) + var4), (double)((float)(this.posZClip + 16) + var4)));
            GL11.glEndList();
            this.markDirty();
        }
    }

    private void setupGLTranslation()
    {
        GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
    }

    /**
     * Will update this chunk renderer
     */
    public void updateRenderer(EntityLivingBase p_147892_1_)
    {
        if (this.needsUpdate)
        {
            this.needsUpdate = false;
            int var2 = this.posX;
            int var3 = this.posY;
            int var4 = this.posZ;
            int var5 = this.posX + 16;
            int var6 = this.posY + 16;
            int var7 = this.posZ + 16;

            for (int var8 = 0; var8 < 2; ++var8)
            {
                this.skipRenderPass[var8] = true;
            }

            Chunk.isLit = false;
            HashSet var26 = new HashSet();
            var26.addAll(this.tileEntityRenderers);
            this.tileEntityRenderers.clear();
            Minecraft var9 = Minecraft.getMinecraft();
            EntityLivingBase var10 = var9.renderViewEntity;
            int var11 = MathHelper.floor_double(var10.posX);
            int var12 = MathHelper.floor_double(var10.posY);
            int var13 = MathHelper.floor_double(var10.posZ);
            byte var14 = 1;
            ChunkCache var15 = new ChunkCache(this.worldObj, var2 - var14, var3 - var14, var4 - var14, var5 + var14, var6 + var14, var7 + var14, var14);

            if (!var15.extendedLevelsInChunkCache())
            {
                ++chunksUpdated;
                RenderBlocks var16 = new RenderBlocks(var15);
                this.bytesDrawn = 0;
                this.vertexState = null;

                for (int var17 = 0; var17 < 2; ++var17)
                {
                    boolean var18 = false;
                    boolean var19 = false;
                    boolean var20 = false;

                    for (int var21 = var3; var21 < var6; ++var21)
                    {
                        for (int var22 = var4; var22 < var7; ++var22)
                        {
                            for (int var23 = var2; var23 < var5; ++var23)
                            {
                                Block var24 = var15.getBlock(var23, var21, var22);

                                if (var24.getMaterial() != Material.air)
                                {
                                    if (!var20)
                                    {
                                        var20 = true;
                                        this.preRenderBlocks(var17);
                                    }

                                    if (var17 == 0 && var24.hasTileEntity())
                                    {
                                        TileEntity var25 = var15.getTileEntity(var23, var21, var22);

                                        if (TileEntityRendererDispatcher.instance.hasSpecialRenderer(var25))
                                        {
                                            this.tileEntityRenderers.add(var25);
                                        }
                                    }

                                    int var28 = var24.getRenderBlockPass();

                                    if (var28 > var17)
                                    {
                                        var18 = true;
                                    }
                                    else if (var28 == var17)
                                    {
                                        var19 |= var16.renderBlockByRenderType(var24, var23, var21, var22);
                                        
                                        //VAPID
                                        StaticVapid.vapid.events.onEvent(new BlockRenderedEvent(StaticVapid.vapid.getBlockId(var24), var15.getBlockMetadata(var23, var21, var22), var23, var21, var22));

                                        if (var24.getRenderType() == 0 && var23 == var11 && var21 == var12 && var22 == var13)
                                        {
                                            var16.setRenderFromInside(true);
                                            var16.setRenderAllFaces(true);
                                            var16.renderBlockByRenderType(var24, var23, var21, var22);
                                            var16.setRenderFromInside(false);
                                            var16.setRenderAllFaces(false);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (var19)
                    {
                        this.skipRenderPass[var17] = false;
                    }

                    if (var20)
                    {
                        this.postRenderBlocks(var17, p_147892_1_);
                    }
                    else
                    {
                        var19 = false;
                    }

                    if (!var18)
                    {
                        break;
                    }
                }
            }

            HashSet var27 = new HashSet();
            var27.addAll(this.tileEntityRenderers);
            var27.removeAll(var26);
            this.tileEntities.addAll(var27);
            var26.removeAll(this.tileEntityRenderers);
            this.tileEntities.removeAll(var26);
            this.isChunkLit = Chunk.isLit;
            this.isInitialized = true;
        }
    }

    private void preRenderBlocks(int p_147890_1_)
    {
        GL11.glNewList(this.glRenderList + p_147890_1_, GL11.GL_COMPILE);
        GL11.glPushMatrix();
        this.setupGLTranslation();
        float var2 = 1.000001F;
        GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
        GL11.glScalef(var2, var2, var2);
        GL11.glTranslatef(8.0F, 8.0F, 8.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));
    }

    private void postRenderBlocks(int p_147891_1_, EntityLivingBase p_147891_2_)
    {
        if (p_147891_1_ == 1 && !this.skipRenderPass[p_147891_1_])
        {
            this.vertexState = tessellator.getVertexState((float)p_147891_2_.posX, (float)p_147891_2_.posY, (float)p_147891_2_.posZ);
        }

        this.bytesDrawn += tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEndList();
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
    }

    public void updateRendererSort(EntityLivingBase p_147889_1_)
    {
        if (this.vertexState != null && !this.skipRenderPass[1])
        {
            this.preRenderBlocks(1);
            tessellator.setVertexState(this.vertexState);
            this.postRenderBlocks(1, p_147889_1_);
        }
    }

    /**
     * Returns the distance of this chunk renderer to the entity without performing the final normalizing square root,
     * for performance reasons.
     */
    public float distanceToEntitySquared(Entity par1Entity)
    {
        float var2 = (float)(par1Entity.posX - (double)this.posXPlus);
        float var3 = (float)(par1Entity.posY - (double)this.posYPlus);
        float var4 = (float)(par1Entity.posZ - (double)this.posZPlus);
        return var2 * var2 + var3 * var3 + var4 * var4;
    }

    /**
     * When called this renderer won't draw anymore until its gets initialized again
     */
    public void setDontDraw()
    {
        for (int var1 = 0; var1 < 2; ++var1)
        {
            this.skipRenderPass[var1] = true;
        }

        this.isInFrustum = false;
        this.isInitialized = false;
        this.vertexState = null;
    }

    public void stopRendering()
    {
        this.setDontDraw();
        this.worldObj = null;
    }

    /**
     * Takes in the pass the call list is being requested for. Args: renderPass
     */
    public int getGLCallListForPass(int par1)
    {
        return !this.isInFrustum ? -1 : (!this.skipRenderPass[par1] ? this.glRenderList + par1 : -1);
    }

    public void updateInFrustum(ICamera par1ICamera)
    {
        this.isInFrustum = par1ICamera.isBoundingBoxInFrustum(this.rendererBoundingBox);
    }

    /**
     * Renders the occlusion query GL List
     */
    public void callOcclusionQueryList()
    {
        GL11.glCallList(this.glRenderList + 2);
    }

    /**
     * Checks if all render passes are to be skipped. Returns false if the renderer is not initialized
     */
    public boolean skipAllRenderPasses()
    {
        return !this.isInitialized ? false : this.skipRenderPass[0] && this.skipRenderPass[1];
    }

    /**
     * Marks the current renderer data as dirty and needing to be updated.
     */
    public void markDirty()
    {
        this.needsUpdate = true;
    }
}
