package me.pyr0byte.vapid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.pyr0byte.vapid.modules.ModuleAura;
import me.pyr0byte.vapid.modules.ModuleAutoarmor;
import me.pyr0byte.vapid.modules.ModuleAutobridge;
import me.pyr0byte.vapid.modules.ModuleAutomine;
import me.pyr0byte.vapid.modules.ModuleAutorespawn;
import me.pyr0byte.vapid.modules.ModuleAutowalk;
import me.pyr0byte.vapid.modules.ModuleBase;
import me.pyr0byte.vapid.modules.ModuleBindEditor;
import me.pyr0byte.vapid.modules.ModuleBot;
import me.pyr0byte.vapid.modules.ModuleBreakMask;
import me.pyr0byte.vapid.modules.ModuleBrightness;
import me.pyr0byte.vapid.modules.ModuleDump;
import me.pyr0byte.vapid.modules.ModuleESP;
import me.pyr0byte.vapid.modules.ModuleEncryption;
import me.pyr0byte.vapid.modules.ModuleFastBreak;
import me.pyr0byte.vapid.modules.ModuleFastPlace;
import me.pyr0byte.vapid.modules.ModuleFastbow;
import me.pyr0byte.vapid.modules.ModuleFastbowTristan;
import me.pyr0byte.vapid.modules.ModuleFly;
import me.pyr0byte.vapid.modules.ModuleFreecam;
import me.pyr0byte.vapid.modules.ModuleFriends;
import me.pyr0byte.vapid.modules.ModuleGlide;
import me.pyr0byte.vapid.modules.ModuleGreet;
import me.pyr0byte.vapid.modules.ModuleGui;
import me.pyr0byte.vapid.modules.ModuleHelp;
import me.pyr0byte.vapid.modules.ModuleHit;
import me.pyr0byte.vapid.modules.ModuleIRC;
import me.pyr0byte.vapid.modules.ModuleImp;
import me.pyr0byte.vapid.modules.ModuleInfo;
import me.pyr0byte.vapid.modules.ModuleIntervalThrow;
import me.pyr0byte.vapid.modules.ModuleMarkers;
import me.pyr0byte.vapid.modules.ModuleMarkov;
import me.pyr0byte.vapid.modules.ModuleNoFall;
import me.pyr0byte.vapid.modules.ModuleNoKnockback;
import me.pyr0byte.vapid.modules.ModuleNoSlow;
import me.pyr0byte.vapid.modules.ModuleNotifications;
import me.pyr0byte.vapid.modules.ModuleRegexIgnore;
import me.pyr0byte.vapid.modules.ModuleSounder;
import me.pyr0byte.vapid.modules.ModuleSpam;
import me.pyr0byte.vapid.modules.ModuleSpawn;
import me.pyr0byte.vapid.modules.ModuleSprint;
import me.pyr0byte.vapid.modules.ModuleStep;
import me.pyr0byte.vapid.modules.ModuleTeleport;
import me.pyr0byte.vapid.modules.ModuleTextWidth;
import me.pyr0byte.vapid.modules.ModuleTimer;
import me.pyr0byte.vapid.modules.ModuleTracers;
import me.pyr0byte.vapid.modules.ModuleTrajectories;
import me.pyr0byte.vapid.modules.ModuleUtil;
import me.pyr0byte.vapid.modules.ModuleWaypoints;
import me.pyr0byte.vapid.modules.ModuleXray;
import me.pyr0byte.vapid.modules.ModuleYaw;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ScreenShotHelper;

public class Vapid {

	Minecraft mc;
	public ArrayList<ModuleBase> modules;
	VapidGui vg;
	public Events events;
	public VapidBools vb;
	public Map<String, ModuleBase> moduleCache;
	public Map<String, String> moduleNameCache;
	boolean hudNotifications;
	public final String delimeter;
	
	public Vapid(Minecraft mc) 
	{
		this.mc = mc;
		
		delimeter = "-";
		
		modules = new ArrayList<ModuleBase>();
		
		moduleCache = new HashMap<String, ModuleBase>();
		moduleNameCache = new HashMap<String, String>();
		
		// Sorted by supposed priority
		modules.add(new ModuleAura(this, this.mc));
		modules.add(new ModuleAutoarmor(this, this.mc));
		modules.add(new ModuleDump(this, this.mc));
		modules.add(new ModuleBot(this, this.mc));

		
		modules.add(new ModuleNoFall(this, this.mc));
		modules.add(new ModuleBindEditor(this, this.mc));
		//modules.add(new ModuleRefill(this, this.mc));
		// crashes
		
		modules.add(new ModuleTrajectories(this, this.mc));
		modules.add(new ModuleMarkers(this, this.mc));

		modules.add(new ModuleESP(this, this.mc));

		
		modules.add(new ModuleFly(this, this.mc));
		modules.add(new ModuleFreecam(this, this.mc));
		modules.add(new ModuleGlide(this, this.mc));
		modules.add(new ModuleAutowalk(this, this.mc));
		modules.add(new ModuleYaw(this, this.mc));
		modules.add(new ModuleIntervalThrow(this, this.mc));

		modules.add(new ModuleSprint(this, this.mc));
		modules.add(new ModuleStep(this, this.mc));

		modules.add(new ModuleTimer(this, this.mc));
		modules.add(new ModuleNoSlow(this, this.mc));
		modules.add(new ModuleBrightness(this, this.mc));
		modules.add(new ModuleGui(this, this.mc));
		modules.add(new ModuleNotifications(this, this.mc));
		modules.add(new ModuleFriends(this, this.mc));
		modules.add(new ModuleNoKnockback(this, this.mc));
		modules.add(new ModuleFastBreak(this, this.mc));
		modules.add(new ModuleBreakMask(this, this.mc));
		modules.add(new ModuleFastbow(this, this.mc));
		modules.add(new ModuleFastbowTristan(this, this.mc));

		modules.add(new ModuleRegexIgnore(this, this.mc));

		modules.add(new ModuleSpawn(this, this.mc));
		modules.add(new ModuleUtil(this, this.mc));
		modules.add(new ModuleEncryption(this, this.mc));
		modules.add(new ModuleFastPlace(this, this.mc));
		modules.add(new ModuleAutobridge(this, this.mc));

		modules.add(new ModuleHelp(this, this.mc));
		modules.add(new ModuleInfo(this, this.mc));
		modules.add(new ModuleGreet(this, this.mc));
		modules.add(new ModuleTextWidth(this, this.mc));
		modules.add(new ModuleAutomine(this, this.mc));
		modules.add(new ModuleWaypoints(this, this.mc));
		modules.add(new ModuleXray(this, this.mc));
		modules.add(new ModuleTracers(this, this.mc));
		modules.add(new ModuleSpam(this, this.mc));
		modules.add(new ModuleAutorespawn(this, this.mc));
		modules.add(new ModuleTeleport(this, this.mc));
		modules.add(new ModuleSounder(this, this.mc));
		modules.add(new ModuleIRC(this, this.mc));
		modules.add(new ModuleHit(this, this.mc));
		modules.add(new ModuleMarkov(this, this.mc));
		modules.add(new ModuleImp(this, this.mc));

		// Troublesome 
		
		for(ModuleBase m : modules)
		{
			moduleCache.put(m.getName().toLowerCase(), m);
			moduleNameCache.put(m.getClass().getSimpleName(), m.getName().toLowerCase());
		}
		
		events = new Events(this.mc, this);
		
		hudNotifications = true;
		//vb = new VapidBools(this);
		
		new StaticVapid(this, this.mc);
	
	}
	

	/*
	public void loadGui()
	{
		vg = new VapidGui(this.mc, this);	
	}
	*/
	
	public void onTick() 
	{

		if(mc.thePlayer != null) {
			
			for(ModuleBase m : this.modules) {
				if(m.needsTick)
					m.onTick();
			}
		}
	
	}
	
	public void onRendererTick()
	{
		for(ModuleBase m : this.modules) {
			if(m.needsRendererTick)
				m.onRendererTick();
		}
	}
	
	public void onBotTick()
	{
		for(ModuleBase m : this.modules) {
			if(m.needsBotTick)
				m.onBotTick();
		}
	}
	
	public void confirmMessage(String msg)
	{
		this.message("§l[V]§r " + msg);
	}
	
	public void notificationMessage(String msg)
	{
		this.notificationMessage(msg, this.hudNotifications);
	}
	
	
	public void notificationMessage(String msg, boolean hud)
	{
		if(!hud)
			this.message("§l>>§r " + msg);
		else
			getModule(ModuleGui.class).addToQueue(msg);
	}
	
	public void errorMessage(String msg)
	{
		this.message("§l§m[V]§r " + msg);
	}
	
	public void message(String msg) {
		this.mc.ingameGUI.getChatGUI().func_146227_a(new ChatComponentText(msg));
	}
	
	public void yellowMessage(String msg) {
		this.message("§e" + msg);
	}
	
	public void italicMessage(String msg) {
		this.message("§o" + msg);
	}

	public ModuleBase getModule(String name)
	{
		return moduleCache.get(name);
	}
	
	public <T extends ModuleBase> T getModule(String name, Class<T> type) {
	    return type.cast( moduleCache.get(name) );
	}
	
	public <T extends ModuleBase> T getModule(Class<T> type) {
	    return type.cast( moduleCache.get(moduleNameCache.get(type.getSimpleName())));
	}
	
	public int getBlockId(Block b)
	{
		return Block.blockRegistry.getIDForObject(b);
	}
	

	public String read(String filename) 
	{
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {

				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new FileReader(filename));
	     
	     String line;
	     String res = ""; 
	     while ((line = br.readLine()) != null)
	     {
	        res += line;
	     }
	      
	     	br.close();
	    
	     	return res;
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
		return null;
	}
	
	public ArrayList<String> readLines(String filename) 
	{
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {

				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new FileReader(filename));
	     
	     String line;
	     ArrayList<String> lines = new ArrayList<String>(); 
	     while ((line = br.readLine()) != null)
	     {
	        lines.add(line);
	     }
	      
	     	br.close();
	    
	     	return lines;
	     	
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
		return null;
	}
	
	public ArrayList<String> readUTF8Lines(String filename) 
	{
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {

				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
	     
	     String line;
	     ArrayList<String> lines = new ArrayList<String>(); 
	     while ((line = br.readLine()) != null)
	     {
	        lines.add(line);
	     }
	      
	     	br.close();
	    
	     	return lines;
	     	
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
		return null;
	}
	
	public void write(String filename, String data)
	{
		try 
		  {
		        File f = new File(filename);
		
		        if (f.exists()) 
		        {
		        	f.delete();
		        	f.createNewFile();
		        }
		
				
		        FileWriter fw = new FileWriter(filename);
		        BufferedWriter bw = new BufferedWriter(fw);
		
		        
		        bw.write(data);
		
		        bw.close();
		        
		     
	    }
	    catch (IOException localIOException)
	    {
	    	localIOException.printStackTrace();
	    }
	}
	
	public void writeLines(String filename, ArrayList<String> data)
	{
		try 
		  {
		        File f = new File(filename);
		
		        if (f.exists()) 
		        {
		        	f.delete();
		        	f.createNewFile();
		        }
		
				
		        FileWriter fw = new FileWriter(filename);
		        BufferedWriter bw = new BufferedWriter(fw);
		
		        for(String s : data)
		        {
		        	bw.write(s + "\n");
		        }
		        
		        bw.close();
		        
		     
	    }
	    catch (IOException localIOException)
	    {
	    	localIOException.printStackTrace();
	    }
	}

	public void appendToFile(String filename, String str)
	{
		File f = new File(filename);
		String in = "";
		if(f.exists())
			in = this.read(filename) + str + "\n";
		else
			in = str + "\n";
		
		this.write(filename, in);
	}
	
	public void takeScreenshot()
	{
        mc.ingameGUI.getChatGUI().func_146227_a(ScreenShotHelper.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.mcFramebuffer));
	}

}
