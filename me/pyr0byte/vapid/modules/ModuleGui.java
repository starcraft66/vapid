package me.pyr0byte.vapid.modules;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.lwjgl.Sys;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Util;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;

public class ModuleGui extends ModuleBase {

	public boolean isHidden;
	int guiColor;
	FontRenderer fr;
	int width, height;
	ScaledResolution scaledResolution;
	public ArrayList<HashMap<String, Integer>> note;
	int maxDisplayedNotifications;
	int notificationTimeout;
	int ticks;
	String infoFormat;
	boolean showArmor;
	String fps = "";
	
	public ModuleGui(Vapid vapid, Minecraft mc)
	{
		super(vapid, mc);
		
		this.fr = this.mc.fontRenderer;
		this.isHidden = false;
		this.guiColor = 0xFFFFFF;
		this.needsTick = false;
		this.note = new ArrayList<HashMap<String, Integer>>();
		this.maxDisplayedNotifications = 5;
		this.notificationTimeout = 256;
		this.ticks = 0;
		this.showArmor = true;
		this.command = new Command(this.vapid, this, aliases, "Toggles the GUI");
		this.command.registerArg("format", new Class[] {String.class}, "Wrap your argument in quotes! Changes format of the coord/info text; {x} parses to the x-coord, {z}, {y} do the same; {d} is your direction, {D} is it's single character representation; {v} is your velocity in km/h, {fps}, & is the formatting char");
		this.command.registerArg("armor", new Class[] {}, "Toggles armor display");

		try
		{
			File file = new File("gui_format.vpd");
			if(file.exists())
			{
				this.infoFormat = vapid.read("gui_format.vpd");				
			}
			else
			{
				file.createNewFile();
				this.infoFormat = "[{x}, {z}] {v}km/h";
				vapid.write("gui_format.vpd", this.infoFormat);
			}
			
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void toggleState()
	{
		this.isHidden = !this.isHidden;
	}
	
	@Override
	public void onTick()
	{

		scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        width = scaledResolution.getScaledWidth();
        height = scaledResolution.getScaledHeight();
        
		if(!mc.gameSettings.showDebugInfo && !isHidden) {

			String x = Integer.toString((int)Math.floor(this.mc.thePlayer.posX));
			String y = Integer.toString((int)Math.floor(this.mc.thePlayer.posY));
			String z = Integer.toString((int)Math.floor(this.mc.thePlayer.posZ));
			String v = Double.toString(this.getPlayerVelocity());
			String d = Direction.directions[MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3];
			String D = Character.toString(d.charAt(0));
			if(Sys.getTime() * 1000L / Sys.getTimerResolution() >= mc.debugUpdateTime + 1000L)
				fps = Integer.toString(this.mc.fpsCounter);
			
			String info = this.infoFormat.replaceAll("&", "§").replaceAll("\\{x\\}", x).replaceAll("\\{z\\}", z).replaceAll("\\{y\\}", y).replaceAll("\\{v\\}", v).replaceAll("\\{d\\}", d).replaceAll("\\{D\\}", D).replaceAll("\\{fps\\}", fps);
					
			
			fr.drawStringWithShadow(info, 2, 2, this.guiColor);
		
			this.drawModuleData();
			this.drawTime();
			this.drawStats();
			this.drawNotifications();
			
			this.decrementNotes();
			
		}

	}
	

	private void drawModuleData()
	{
		int position = 12;
		for(ModuleBase m : vapid.modules) {
			
			if(m.isEnabled && m.showEnabled) {
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
    	
    	if(this.showArmor)
    	{
    		fr.drawStringWithShadow(helmet, width - fr.getStringWidth(helmet) - 2, 2, this.guiColor);
    		fr.drawStringWithShadow(chestplate, width - fr.getStringWidth(chestplate) - 2, 12, this.guiColor);
    		fr.drawStringWithShadow(leggings, width - fr.getStringWidth(leggings) - 2, 22, this.guiColor);
    		fr.drawStringWithShadow(boots, width - fr.getStringWidth(boots) - 2, 32, this.guiColor);
    	}
    	
    	Iterator potions = mc.thePlayer.getActivePotionEffects().iterator();
    	PotionEffect p;
    	String name, duration, toDraw = "";
    	int position = this.showArmor ? 42 : 2;
    	
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
	
	private void drawNotifications()
	{
		int x = 0, y = 0, pos = 0;
		String note = "";
		
		for(int i = this.note.size() - 1; i >= 0; i--) 
		{
			if(i == this.maxDisplayedNotifications)
				break;
			
			note = (String) this.note.get(i).keySet().toArray()[0];
			

			x = (width - fr.getStringWidth(note)) / 2;
			y = (height - 8) / 2;
			
			fr.drawStringWithShadow(note, x, y - pos, this.guiColor);

			
			pos += 10;
		}

	}
	
	public void addToQueue(String msg)
	{		
		this.note.add(new HashMap<String, Integer>());
		this.note.get(this.note.size() - 1).put(msg, 60);
	}
	
	public void decrementNotes()
	{
		String key = "";
		for(int i = 0; i < this.note.size(); i++)
		{
			key = (String) this.note.get(i).keySet().toArray()[0];
			
			if(this.note.get(i).get(key) <= 0)
			{
				this.note.remove(i);
				continue;
			}
			
			this.note.get(i).put(key, this.note.get(i).get(key) - 1);
			
		}
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("format"))
		{
			this.infoFormat = argv[0];
			vapid.write("gui_format.vpd", this.infoFormat);
		}
		else if(name.equals("armor"))
		{
			this.showArmor = !this.showArmor;
		}
	}

}
