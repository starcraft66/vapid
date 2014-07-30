package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.EntityLabelRenderedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.lwjgl.opengl.GL11;

public class ModuleESP extends ModuleBase 
{
	
	boolean itemEsp;
	boolean armorEsp;
	
	public ModuleESP(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
	
		this.name = "ESP";
		this.command = new Command(this.vapid, this, aliases, "Hilights a player's name and shows their held item and distance; friend's names are green.");
		this.command.registerArg("items", new Class[] {}, "Show held items?");
		this.command.registerArg("armor", new Class[] {}, "Show armor durability?");

		this.itemEsp = true;
	}
	
	@EventHandler
	public boolean onEntityLabelRenderedEvent(EntityLabelRenderedEvent event)
	{
		
		if(this.isEnabled)
		{
			FontRenderer fr = mc.fontRenderer;
	        float var13 = 1.6F;
	        float var14 = 0.016666668F * var13;
	        
	        Entity e = event.entity;
	        double i = event.interpolationX;
	        double j = event.interpolationY;
	        double k = event.interpolationZ;
	        
	        String name = e.getCommandSenderName();
	        double distance = Math.round(mc.thePlayer.getDistanceSqToEntity(e));
	        
	        double dx = (e.posX * 1 - RenderManager.renderPosX + 0.5D);
	        double dy = (e.posY - RenderManager.renderPosY + 0.5D);
	        double dz = (e.posZ * 1 - RenderManager.renderPosZ + 0.5D);
	        
	        
	        double dl = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	        var14 = (float)(dl * 0.1F + 1.0F) * 0.02666666666666667F;
	        
	        int j2 = 225 % 0x10000;
			int k2 = 225 / 0x10000;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j2 / 1.0F, (float)k2 / 1.0F);
			
			
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float)i + 0.0F, (float)j + e.height + 0.5F, (float)k);
	        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	        GL11.glRotatef(-event.rm.playerViewY, 0.0F, 1.0F, 0.0F);
	        GL11.glRotatef(event.rm.playerViewX, 1.0F, 0.0F, 0.0F);
	        GL11.glScalef(-var14, -var14, var14);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDepthMask(false);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_BLEND);
	    
	        // ||| - Disable fog
	        GL11.glDisable(GL11.GL_FOG);
	        
	        
	       // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        
	        Tessellator var15 = Tessellator.instance;
	        byte var16 = 0;
	
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        var15.startDrawingQuads();
	        
		        name = name + " (" + Integer.toString((int)Math.floor(Math.sqrt(distance))) + ")";
		        
		        String item = "";
		        if(e instanceof EntityOtherPlayerMP) {
		        	item = this.getItemNameAndEnchantments(((EntityOtherPlayerMP)e).inventory.getCurrentItem());
		        }
		        
		        int var17 = fr.getStringWidth(name) / 2;
	        
		     // ||| - Colors
		        if(e instanceof EntityOtherPlayerMP && vapid.getModule(ModuleFriends.class).isFriend(e.getCommandSenderName()))
		        	var15.setColorRGBA_F(0.2F, 0.7F, 0.2F, 1.0F);
		        else
		        	var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
		        
		        var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
		        var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
		        var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
		        var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
		        var15.draw();
		        GL11.glEnable(GL11.GL_TEXTURE_2D);
		        
		        // |||
		        if(e instanceof EntityOtherPlayerMP && this.itemEsp) 
		        {
		            fr.drawString(item, -fr.getStringWidth(item) / 2, -10, 553648127);
		
		            fr.drawString(item, -fr.getStringWidth(item) / 2, -10, -1);
		        }
		        
		        if(e instanceof EntityOtherPlayerMP && this.armorEsp) 
		        {
		        	String armor = this.vapid.getModule(ModuleInfo.class).getArmorDurability(e.getCommandSenderName());
		        	int position = this.itemEsp ? -20 : -10;
		        	
		            fr.drawString(armor, -fr.getStringWidth(armor) / 2, position, 553648127);
		
		            fr.drawString(armor, -fr.getStringWidth(armor) / 2, position, -1);
		        }
	
	        
	        fr.drawString(name, -fr.getStringWidth(name) / 2, var16, 553648127);
	        fr.drawString(name, -fr.getStringWidth(name) / 2, var16, -1);
	
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glDepthMask(true);
	        
	        
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
	        
	        
	        // |||
	        GL11.glEnable(GL11.GL_FOG);
	        
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glPopMatrix();
	        
	        return false;
		}
		
		return true;
 	}

	  
	  public String getItemNameAndEnchantments(ItemStack is) {
          	if(is != null) {
          		if(is.isItemEnchanted()) {
          			NBTTagList e = is.getEnchantmentTagList();
          			String enchants = "";
          			int i, i1, i2;
          			for(i = 0; i < e.tagCount(); i++) {
          				// ??????
          				i1 = ((NBTTagCompound)e.getCompoundTagAt(i)).getShort("id");
          				i2 = ((NBTTagCompound)e.getCompoundTagAt(i)).getShort("lvl");
          				enchants = enchants + (i == 0 ? "" : ", ") + Enchantment.enchantmentsList[i1].getTranslatedName(i2);
          			}
          			return  is.getDisplayName() + " (" + Integer.toString(is.stackSize) + "/" + Integer.toString(is.getMaxStackSize()) + ")" +  " [" + enchants +"]";
          		} else
          			return is.getDisplayName() + " (" + Integer.toString(is.stackSize) + "/" + Integer.toString(is.getMaxStackSize()) + ")";
          			
          	} else
          		return "Nothing";
	  }
	  
	  @Override
	  public void processArguments(String name, String argv[])
	  {
		  if(name.equals("items"))
		  {
			  this.itemEsp = !this.itemEsp;
		  }
		  
		  if(name.equals("armor"))
		  {
			  this.armorEsp = !this.armorEsp;
		  }
	  }
	  
	  @Override
	  public String getMetadata()
	  {
		  String ret = "";
		  if(this.itemEsp)
		  {
			  ret += "Items, ";
		  }
		  
		  if(this.armorEsp)
		  {
			  ret += "Armor";
		  }
		  
		  if(!this.armorEsp && !this.itemEsp)
			  return "";
		  
		  if(ret.endsWith(", "))
		  {
			  ret = ret.substring(0, ret.length() - 2);
		  }
		  
		  return "(" + ret + ")";
	  }
}
