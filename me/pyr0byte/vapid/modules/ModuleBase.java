package me.pyr0byte.vapid.modules;

import java.util.ArrayList;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.IModule;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleBase implements IModule {

	Vapid vapid;
	public ArrayList<String> aliases;
	public String name;
	public String defaultArg;
	public Command command;
	public boolean isEnabled;
	public boolean needsTick;
	public boolean needsRendererTick;
	public boolean needsBotTick;
	public boolean showEnabled;
	
	// If false, this will essentially just act as a command.
	public boolean isToggleable;
	
	Minecraft mc;
	
	public ModuleBase(Vapid vapid, Minecraft mc) 
	{
		this.vapid = vapid;
		this.isEnabled = false;
		this.mc = mc;
		this.isToggleable = true;
		this.needsRendererTick = false;
		this.needsBotTick = false;
		this.defaultArg = "";
		
		aliases = new ArrayList<String>();
		
		this.command = new Command(this.vapid, this, aliases);
		needsTick = false;
		showEnabled = true;
		
		this.name = this.getClass().getSimpleName().replaceFirst("Module", "").toLowerCase();
		aliases.add(name);
		
		this.vapid.moduleCache.put(this.name, this);
	}
	
	public void onEnable()
	{
		this.isEnabled = true;
	}
	
	public void onDisable()
	{
		this.isEnabled = false;
	}
	
	public void toggleState() {
		if(this.isEnabled)
			this.onDisable();
		else
			this.onEnable();
	}
	
	
	public void onTick()
	{
		
	}
	
	public void onRendererTick()
	{
		
	}
	
	public void onBotTick()
	{
		
	}
	
	public void processArguments(String name, String argv[])
	{
	}
	
	public String getMetadata()
	{
		return "";
	}
	
	public String getName()
	{
		return this.name;
	}

}
