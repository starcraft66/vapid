package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

public class ModuleTrajectories extends ModuleBase 
{
	
	public ModuleTrajectories(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsRendererTick = true;
		//this.showEnabled = false;
		this.aliases.add("trajectories");
		
		this.command = new Command(this.vapid, this, aliases, "Draws trajectories for arrows, enderpearls, eggs, potions, snowballs...");
		
	}
	
	@Override
	public void onRendererTick()
	{
		if(this.isEnabled) 
		{
				this.projectiles(mc.thePlayer, false);
		}
		
	}
	
	public double pX = -9000, pY = -9000, pZ = -9000;
	  
    public void projectiles(EntityPlayer e, boolean flag) {
	  
	  	  
	  	  boolean potion = false;
          ItemStack is = e.inventory.getCurrentItem();
          if (is != null) {
                  String id = is.getItem().getUnlocalizedName();
                  boolean drawPrediction = true;
                  float var4 = 0.05F;
                  float var5 = 3.2F;
                  float var6 = 0.0F;
                  boolean var7 = false;
                                   
                  if (id.equals("item.bow")) {
                          var4 = 0.05F;
                          var5 = 3.2F;
                  } else if (id.equals("item.enderPearl")) {
                          var4 = 0.03F;
                          var5 = 1.5F;
                  } else if (id.equals("item.snowball")) {
                          var4 = 0.03F;
                          var5 = 1.5F;
                  } else if (id.equals("item.fishing_rod")) {
                          var5 = 1.5F;
                  } else if (id.equals("item.egg")) {
                          var4 = 0.03F;
                          var5 = 1.5F;
                  } else if ((is.getItemDamage() & 0x4000) > 0) {
                          var4 = 0.05F;
                          var5 = 0.5F;
                          var6 = -20F;
                          potion = true;
                  } else {
                          drawPrediction = false;
                  }

                  if (drawPrediction) {
                              AxisAlignedBB var9 = AxisAlignedBB.getBoundingBox(0.0D, 0.0D,
                                              0.0D, 0.0D, 0.0D, 0.0D);
                              float var10 = (float) e.posX;
                              float var11 = (float) e.posY;
                              float var12 = (float) e.posZ;
                              float var13 = e.rotationYaw;
                              float var14 = e.rotationPitch;
                              float var15 = var13 / 180.0F * (float) Math.PI;
                              float var16 = MathHelper.sin(var15);
                              float var17 = MathHelper.cos(var15);
                              float var18 = var14 / 180.0F * (float) Math.PI;
                              float var19 = MathHelper.cos(var18);
                              float var20 = (var14 + var6) / 180.0F * (float) Math.PI;
                              float var21 = MathHelper.sin(var20);
                              float var22 = var10 - var17 * 0.16F;
                              float var23 = var11 - 0.1F;
                              float var24 = var12 - var16 * 0.16F;
                              float var25 = -var16 * var19 * 0.4F;
                              float var26 = -var21 * 0.4F;
                              float var27 = var17 * var19 * 0.4F;
                              float var28 = MathHelper.sqrt_double(var25 * var25
                                              + var26 * var26 + var27 * var27);
                              var25 /= var28;
                              var26 /= var28;
                              var27 /= var28;
                              var25 *= var5;
                              var26 *= var5;
                              var27 *= var5;
                              float var29 = var22;
                              float var30 = var23;
                              float var31 = var24;
                              GL11.glPushMatrix();
                              //GL11.glColor3f(0.27F, 0.70F, 0.92F);
                              GL11.glColor3f(1.0F, 1.0F, 1.0F);

                              GL11.glBlendFunc(770, 771);
                              GL11.glEnable(3042);
                              GL11.glDisable(3553);
                              GL11.glDisable(2929);
                              GL11.glDepthMask(false);
                              GL11.glBegin(GL11.GL_LINES);
                              int var32 = 0;
                              boolean var33 = true;
                              while (var33) {
                                      ++var32;
                                      var29 += var25;
                                      var30 += var26;
                                      var31 += var27;
                                      Vec3 var34 = Vec3.createVectorHelper(var22,
                                                      var23, var24);
                                      Vec3 var35 = Vec3.createVectorHelper(var29,
                                                      var30, var31);
                                      MovingObjectPosition var8 = mc.theWorld.rayTraceBlocks(var34, var35);
                                      if (var8 != null) {
                                              var22 = (float) var8.hitVec.xCoord;
                                              var23 = (float) var8.hitVec.yCoord;
                                              var24 = (float) var8.hitVec.zCoord;
                                              var33 = false;
                                      } else if (var32 > 200) {
                                              var33 = false;
                                      } else {
                                              this.drawLine3D(var22 - var10, var23 - var11, var24
                                                              - var12, var29 - var10, var30 - var11, var31
                                                              - var12);
                                              if (var7) {
                                                      var9.setBounds(var22 - 0.125F,
                                                                      var23, var24 - 0.125F,
                                                                      var22 + 0.125F,
                                                                      var23 + 0.25F,
                                                                      var24 + 0.125F);
                                                      var4 = 0.0F;
                                                      float var37;
                                                      for (int var36 = 0; var36 < 5; ++var36) {
                                                              var37 = (float) (var9.minY + (var9.maxY - var9.minY)
                                                                              * (var36) / 5.0D);
                                                              float var38 = (float) (var9.minY + (var9.maxY - var9.minY)
                                                                              * (var36 + 1) / 5.0D);
                                                              AxisAlignedBB var39 = AxisAlignedBB
                                                                              .getBoundingBox(var9.minX,
                                                                                              var37, var9.minZ,
                                                                                              var9.maxX, var38,
                                                                                              var9.maxZ);
                                                              // water
                                                              if (mc.theWorld.isAABBInMaterial(var39, Material.water)) {
                                                                      var4 += 0.2F;
                                                              }
                                                              
                                                      }
                                                      float var40 = var4 * 2.0F - 1.0F;
                                                      var26 += 0.04F * var40;
                                                      var37 = 0.92F;

                                                      if (var4 > 0.0F) {
                                                              var37 *= 0.9F;
                                                              var26 *= 0.8F;
                                                      }

                                                      var25 *= var37;
                                                      var26 *= var37;
                                                      var27 *= var37;
                                              } else {
                                                      var25 *= 0.99F;
                                                      var26 *= 0.99F;
                                                      var27 *= 0.99F;
                                                      var26 -= var4;
                                              }
                                              var22 = var29;
                                              var23 = var30;
                                              var24 = var31;
                                      }

                                      if (!var33) {
                                              pX = var22;
                                              pY = var23;
                                              pZ = var24;
                                              
                                              if(potion && Math.floor(e.getDistanceSq(pX, pY, pZ)) < 17.0) {
                                            	  GL11.glColor3f(1.0F, 0F, 0F);
                                              }
                                              
                                              
                                              
                                              GL11.glVertex3d(pX - var10 - 0.5F, pY - var11, pZ - var12 - 0.5F);
                                              GL11.glVertex3d(pX - var10 - 0.5F, pY - var11, pZ - var12 + 0.5F);
                                              GL11.glVertex3d(pX - var10 + 0.5F, pY - var11, pZ - var12 + 0.5F);
                                              GL11.glVertex3d(pX - var10 + 0.5F, pY - var11, pZ - var12 - 0.5F);
                                              GL11.glVertex3d(pX - var10 + 0.5F, pY - var11, pZ - var12 + 0.5F);
                                              GL11.glVertex3d(pX - var10 - 0.5F, pY - var11, pZ - var12 + 0.5F);
                                              GL11.glVertex3d(pX - var10 - 0.5F, pY - var11, pZ - var12 - 0.5F);
                                              GL11.glVertex3d(pX - var10 + 0.5F, pY - var11, pZ - var12 - 0.5F);
                                              GL11.glVertex3d(pX - var10 - 0.5F, pY - var11, pZ - var12 - 0.5F);
                                              GL11.glVertex3d(pX - var10 + 0.5F, pY - var11, pZ - var12 + 0.5F);
                                              GL11.glVertex3d(pX - var10 - 0.5F, pY - var11, pZ - var12 + 0.5F);
                                              GL11.glVertex3d(pX - var10 + 0.5F, pY - var11, pZ - var12 - 0.5F);
                                              
                                      } else {
                                      }
                              }
                              GL11.glEnd();
                              GL11.glDisable(3042);
                          GL11.glEnable(3553);
                          GL11.glEnable(2929);
                          GL11.glDisable(2848);
                          GL11.glDisable(2881);
                          GL11.glEnable(2896);
                          GL11.glDisable(32925);
                          GL11.glDisable(32926);
                          GL11.glDepthMask(true);
                              GL11.glPopMatrix();
                      }
              }
              
      }

      public void drawLine3D(float var1, float var2, float var3, float var4,
                      float var5, float var6) {
              GL11.glVertex3d(var1, var2, var3);
              GL11.glVertex3d(var4, var5, var6);
      }
      
      public void drawCircle(double x, double z, double y, double radius)
      {

          	  GL11.glColor3f(0.27F, 0.70F, 0.92F);
              GL11.glBegin(GL11.GL_POLYGON);
              for (int i = 0; i <= 360; i++) {
                  GL11.glVertex3d(x + (Math.sin((i * 3.141526D / 180)) * radius), y, z + (Math.cos((i * 3.141526D / 180)) * radius));
              }
              
      }

}


