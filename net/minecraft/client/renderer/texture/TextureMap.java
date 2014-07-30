package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject, IIconRegister
{
    private static final Logger logger = LogManager.getLogger();
    public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    public static final ResourceLocation locationItemsTexture = new ResourceLocation("textures/atlas/items.png");
    private final List listAnimatedSprites = Lists.newArrayList();
    private final Map mapRegisteredSprites = Maps.newHashMap();
    private final Map mapUploadedSprites = Maps.newHashMap();

    /** 0 = terrain.png, 1 = items.png */
    private final int textureType;
    private final String basePath;
    private int field_147636_j;
    private int field_147637_k = 1;
    private final TextureAtlasSprite missingImage = new TextureAtlasSprite("missingno");
    private static final String __OBFID = "CL_00001058";

    public TextureMap(int par1, String par2Str)
    {
        this.textureType = par1;
        this.basePath = par2Str;
        this.registerIcons();
    }

    private void initMissingImage()
    {
        int[] var1;

        if ((float)this.field_147637_k > 1.0F)
        {
            boolean var2 = true;
            boolean var3 = true;
            boolean var4 = true;
            this.missingImage.setIconWidth(32);
            this.missingImage.setIconHeight(32);
            var1 = new int[1024];
            System.arraycopy(TextureUtil.missingTextureData, 0, var1, 0, TextureUtil.missingTextureData.length);
            TextureUtil.func_147948_a(var1, 16, 16, 8);
        }
        else
        {
            var1 = TextureUtil.missingTextureData;
            this.missingImage.setIconWidth(16);
            this.missingImage.setIconHeight(16);
        }

        int[][] var5 = new int[this.field_147636_j + 1][];
        var5[0] = var1;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] {var5}));
    }

    public void loadTexture(IResourceManager par1ResourceManager) throws IOException
    {
        this.initMissingImage();
        this.func_147631_c();
        this.loadTextureAtlas(par1ResourceManager);
    }

    public void loadTextureAtlas(IResourceManager par1ResourceManager)
    {
        int var2 = Minecraft.getGLMaximumTextureSize();
        Stitcher var3 = new Stitcher(var2, var2, true, 0, this.field_147636_j);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int var4 = Integer.MAX_VALUE;
        Iterator var5 = this.mapRegisteredSprites.entrySet().iterator();
        TextureAtlasSprite var8;

        while (var5.hasNext())
        {
            Entry var6 = (Entry)var5.next();
            ResourceLocation var7 = new ResourceLocation((String)var6.getKey());
            var8 = (TextureAtlasSprite)var6.getValue();
            ResourceLocation var9 = this.func_147634_a(var7, 0);

            try
            {
                IResource var10 = par1ResourceManager.getResource(var9);
                BufferedImage[] var11 = new BufferedImage[1 + this.field_147636_j];
                var11[0] = ImageIO.read(var10.getInputStream());
                TextureMetadataSection var12 = (TextureMetadataSection)var10.getMetadata("texture");

                if (var12 != null)
                {
                    List var13 = var12.func_148535_c();
                    int var15;

                    if (!var13.isEmpty())
                    {
                        int var14 = var11[0].getWidth();
                        var15 = var11[0].getHeight();

                        if (MathHelper.roundUpToPowerOfTwo(var14) != var14 || MathHelper.roundUpToPowerOfTwo(var15) != var15)
                        {
                            throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                        }
                    }

                    Iterator var35 = var13.iterator();

                    while (var35.hasNext())
                    {
                        var15 = ((Integer)var35.next()).intValue();

                        if (var15 > 0 && var15 < var11.length - 1 && var11[var15] == null)
                        {
                            ResourceLocation var16 = this.func_147634_a(var7, var15);

                            try
                            {
                                var11[var15] = ImageIO.read(par1ResourceManager.getResource(var16).getInputStream());
                            }
                            catch (IOException var21)
                            {
                                logger.error("Unable to load miplevel {} from: {}", new Object[] {Integer.valueOf(var15), var16, var21});
                            }
                        }
                    }
                }

                AnimationMetadataSection var34 = (AnimationMetadataSection)var10.getMetadata("animation");
                var8.func_147964_a(var11, var34, (float)this.field_147637_k > 1.0F);
            }
            catch (RuntimeException var22)
            {
                logger.error("Unable to parse metadata from " + var9, var22);
                continue;
            }
            catch (IOException var23)
            {
                logger.error("Using missing texture, unable to load " + var9, var23);
                continue;
            }

            var4 = Math.min(var4, Math.min(var8.getIconWidth(), var8.getIconHeight()));
            var3.addSprite(var8);
        }

        int var24 = MathHelper.calculateLogBaseTwo(var4);

        if (var24 < this.field_147636_j)
        {
            logger.debug("{}: dropping miplevel from {} to {}, because of minTexel: {}", new Object[] {this.basePath, Integer.valueOf(this.field_147636_j), Integer.valueOf(var24), Integer.valueOf(var4)});
            this.field_147636_j = var24;
        }

        Iterator var26 = this.mapRegisteredSprites.values().iterator();

        while (var26.hasNext())
        {
            final TextureAtlasSprite var27 = (TextureAtlasSprite)var26.next();

            try
            {
                var27.func_147963_d(this.field_147636_j);
            }
            catch (Throwable var20)
            {
                CrashReport var29 = CrashReport.makeCrashReport(var20, "Applying mipmap");
                CrashReportCategory var32 = var29.makeCategory("Sprite being mipmapped");
                var32.addCrashSectionCallable("Sprite name", new Callable()
                {
                    private static final String __OBFID = "CL_00001059";
                    public String call()
                    {
                        return var27.getIconName();
                    }
                });
                var32.addCrashSectionCallable("Sprite size", new Callable()
                {
                    private static final String __OBFID = "CL_00001060";
                    public String call()
                    {
                        return var27.getIconWidth() + " x " + var27.getIconHeight();
                    }
                });
                var32.addCrashSectionCallable("Sprite frames", new Callable()
                {
                    private static final String __OBFID = "CL_00001061";
                    public String call()
                    {
                        return var27.getFrameCount() + " frames";
                    }
                });
                var32.addCrashSection("Mipmap levels", Integer.valueOf(this.field_147636_j));
                throw new ReportedException(var29);
            }
        }

        this.missingImage.func_147963_d(this.field_147636_j);
        var3.addSprite(this.missingImage);

        try
        {
            var3.doStitch();
        }
        catch (StitcherException var19)
        {
            throw var19;
        }

        logger.info("Created: {}x{} {}-atlas", new Object[] {Integer.valueOf(var3.getCurrentWidth()), Integer.valueOf(var3.getCurrentHeight()), this.basePath});
        TextureUtil.func_147946_a(this.getGlTextureId(), this.field_147636_j, var3.getCurrentWidth(), var3.getCurrentHeight(), (float)this.field_147637_k);
        HashMap var25 = Maps.newHashMap(this.mapRegisteredSprites);
        Iterator var28 = var3.getStichSlots().iterator();

        while (var28.hasNext())
        {
            var8 = (TextureAtlasSprite)var28.next();
            String var30 = var8.getIconName();
            var25.remove(var30);
            this.mapUploadedSprites.put(var30, var8);

            try
            {
                TextureUtil.func_147955_a(var8.func_147965_a(0), var8.getIconWidth(), var8.getIconHeight(), var8.getOriginX(), var8.getOriginY(), false, false);
            }
            catch (Throwable var18)
            {
                CrashReport var31 = CrashReport.makeCrashReport(var18, "Stitching texture atlas");
                CrashReportCategory var33 = var31.makeCategory("Texture being stitched together");
                var33.addCrashSection("Atlas path", this.basePath);
                var33.addCrashSection("Sprite", var8);
                throw new ReportedException(var31);
            }

            if (var8.hasAnimationMetadata())
            {
                this.listAnimatedSprites.add(var8);
            }
            else
            {
                var8.clearFramesTextureData();
            }
        }

        var28 = var25.values().iterator();

        while (var28.hasNext())
        {
            var8 = (TextureAtlasSprite)var28.next();
            var8.copyFrom(this.missingImage);
        }
    }

    private ResourceLocation func_147634_a(ResourceLocation p_147634_1_, int p_147634_2_)
    {
        return p_147634_2_ == 0 ? new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/%s%s", new Object[] {this.basePath, p_147634_1_.getResourcePath(), ".png"})): new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] {this.basePath, p_147634_1_.getResourcePath(), Integer.valueOf(p_147634_2_), ".png"}));
    }

    private void registerIcons()
    {
        this.mapRegisteredSprites.clear();
        Iterator var1;

        if (this.textureType == 0)
        {
            var1 = Block.blockRegistry.iterator();

            while (var1.hasNext())
            {
                Block var2 = (Block)var1.next();

                if (var2.getMaterial() != Material.air)
                {
                    var2.registerBlockIcons(this);
                }
            }

            Minecraft.getMinecraft().renderGlobal.registerDestroyBlockIcons(this);
            RenderManager.instance.updateIcons(this);
        }

        var1 = Item.itemRegistry.iterator();

        while (var1.hasNext())
        {
            Item var3 = (Item)var1.next();

            if (var3 != null && var3.getSpriteNumber() == this.textureType)
            {
                var3.registerIcons(this);
            }
        }
    }

    public TextureAtlasSprite getAtlasSprite(String par1Str)
    {
        TextureAtlasSprite var2 = (TextureAtlasSprite)this.mapUploadedSprites.get(par1Str);

        if (var2 == null)
        {
            var2 = this.missingImage;
        }

        return var2;
    }

    public void updateAnimations()
    {
        TextureUtil.bindTexture(this.getGlTextureId());
        Iterator var1 = this.listAnimatedSprites.iterator();

        while (var1.hasNext())
        {
            TextureAtlasSprite var2 = (TextureAtlasSprite)var1.next();
            var2.updateAnimation();
        }
    }

    public IIcon registerIcon(String par1Str)
    {
        if (par1Str == null)
        {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        else if (par1Str.indexOf(47) == -1 && par1Str.indexOf(92) == -1)
        {
            Object var2 = (TextureAtlasSprite)this.mapRegisteredSprites.get(par1Str);

            if (var2 == null)
            {
                if (this.textureType == 1)
                {
                    if ("clock".equals(par1Str))
                    {
                        var2 = new TextureClock(par1Str);
                    }
                    else if ("compass".equals(par1Str))
                    {
                        var2 = new TextureCompass(par1Str);
                    }
                    else
                    {
                        var2 = new TextureAtlasSprite(par1Str);
                    }
                }
                else
                {
                    var2 = new TextureAtlasSprite(par1Str);
                }

                this.mapRegisteredSprites.put(par1Str, var2);
            }

            return (IIcon)var2;
        }
        else
        {
            throw new IllegalArgumentException("Name cannot contain slashes!");
        }
    }

    public int getTextureType()
    {
        return this.textureType;
    }

    public void tick()
    {
        this.updateAnimations();
    }

    public void func_147633_a(int p_147633_1_)
    {
        this.field_147636_j = p_147633_1_;
    }

    public void func_147632_b(int p_147632_1_)
    {
        this.field_147637_k = p_147632_1_;
    }
}
