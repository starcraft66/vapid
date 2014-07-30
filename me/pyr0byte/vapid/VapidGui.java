package me.pyr0byte.vapid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import me.pyr0byte.vapid.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class VapidGui {

	Vapid vapid;
	Minecraft mc;
	FontRenderer fr;
	boolean isHidden;
	int width, height;
	ScaledResolution scaledResolution;
	int guiColor;
	
	public VapidGui(Minecraft mc, Vapid vapid) {
		this.mc = mc;
		this.vapid = vapid;
		this.fr = this.mc.fontRenderer;
		this.isHidden = false;
		this.guiColor = 14737632;

	}
	
	public void update()
	{
		scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        width = scaledResolution.getScaledWidth();
        height = scaledResolution.getScaledHeight();
        
		if(!isHidden) {

			fr.drawStringWithShadow("[" + Double.toString(Math.floor(this.mc.thePlayer.posX)) + ", " + Double.toString(Math.floor(this.mc.thePlayer.posZ)) + "] " + Double.toString(this.getPlayerVelocity()) + "km/h", 2, 2, this.guiColor);
		
			this.drawModuleData();
			this.drawTime();
			this.drawStats();
		}
		
	}
	
	private void drawModuleData()
	{
		int position = 12;
		for(ModuleBase m : vapid.modules) {
			
			if(m.isEnabled) {
				fr.drawStringWithShadow("> " + Util.capitalize(m.name) + " " + m.getMetadata(), 2, position, this.guiColor);
				position += 10;
			}
		}
	}
	
	private void drawTime()
	{
    	String date = new SimpleDateFormat("hh:mm a").format(new Date());
    	fr.drawStringWithShadow(date, width - fr.getStringWidth(date) - 4, height - 12, this.guiColor);
	}
	
	private void drawStats()
	{
		ItemStack armor[] = mc.thePlayer.inventory.armorInventory;
    	String helmet, chestplate, leggings, boots = "";
		
    	helmet = "h: " + (armor[3] != null ? Util.formatArmorDurability( ( ((double)armor[3].getMaxDamage() - (double)armor[3].getItemDamage() ) / (double)armor[3].getMaxDamage()) * 100.0D ) + "%" : "none");
    	chestplate = "c: " + (armor[2] != null ? Util.formatArmorDurability((((double)armor[2].getMaxDamage() - (double)armor[2].getItemDamage()) / (double)armor[2].getMaxDamage()) * 100.0D) + "%" : "none");
    	leggings = "l: " + (armor[1] != null ? Util.formatArmorDurability((((double)armor[1].getMaxDamage() - (double)armor[1].getItemDamage()) / (double)armor[1].getMaxDamage()) * 100.0D) + "%" : "none");
    	boots = "b: " + (armor[0] != null ? Util.formatArmorDurability( (((double)armor[0].getMaxDamage() - (double)armor[0].getItemDamage()) / (double)armor[0].getMaxDamage()) * 100.0D)  + "%" : "none");
    	
    	fr.drawStringWithShadow(helmet, width - fr.getStringWidth(helmet) - 2, 2, this.guiColor);
    	fr.drawStringWithShadow(chestplate, width - fr.getStringWidth(chestplate) - 2, 12, this.guiColor);
    	fr.drawStringWithShadow(leggings, width - fr.getStringWidth(leggings) - 2, 22, this.guiColor);
    	fr.drawStringWithShadow(boots, width - fr.getStringWidth(boots) - 2, 32, this.guiColor);
    	
    	Iterator potions = mc.thePlayer.getActivePotionEffects().iterator();
    	PotionEffect p;
    	String name, duration, toDraw = "";
    	int position = 42;
    	
    	while(potions.hasNext()) {
    		p = (PotionEffect)potions.next();
    		duration = Potion.getDurationString(p);
    		name = p.getEffectName().toLowerCase().substring(7, p.getEffectName().length() < 12 ? p.getEffectName().length() : 12);
    		
    		toDraw = name + " " + duration;
    		
    		if( (p.getDuration() / 20) <= 5)
    			toDraw = "§4" + toDraw;
    		
    		if( (p.getDuration() / 20) <= 10)
    			toDraw = "§c" + toDraw;
    		
    		
        	fr.drawStringWithShadow(toDraw, width - fr.getStringWidth(toDraw) - 2, position, this.guiColor);
    		position += 10;
    	}
	}
	private double getPlayerVelocity() 
	{
		
    	double velocity = Math.floor(Math.sqrt(Math.pow(this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX, 2) + Math.pow(this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ, 2)) * 20 * 60 * 60);
    	velocity /= 100;
    	velocity = Math.round(velocity);
    	velocity /= 10;
    	return velocity;
	}
}
