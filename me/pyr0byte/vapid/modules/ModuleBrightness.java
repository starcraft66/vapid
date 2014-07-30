package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleBrightness extends ModuleBase 
{
	float brightness;
	float defaultBrightness;
	
	public ModuleBrightness(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
			
		this.brightness = 10024F;
		this.defaultBrightness = 0F;
		
		this.needsTick = true;
		aliases.add("bright");
		aliases.add("fullbright");
		
		this.command = new Command(this.vapid, this, aliases, "Changes brightness");
		this.command.registerArg("int", new Class[] { Float.class }, "how bright");
		this.defaultArg = "int";

	}
	
	@Override
	public void onEnable()
	{
		this.defaultBrightness = mc.gameSettings.gammaSetting;
		this.isEnabled = true;
	}
	
	@Override
	public void onDisable()
	{
		mc.gameSettings.gammaSetting = this.defaultBrightness > 1.0F ? 1.0F : this.defaultBrightness;
		this.isEnabled = false;
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled)
			mc.gameSettings.gammaSetting = this.brightness;	
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("int"))
		{
			this.brightness = Float.parseFloat(argv[0]);
		}
	}
	
	@Override
	public String getMetadata()
	{
		return this.brightness == 1024F ? "" :  "(" + Float.toString(brightness) + ")";
	}
	
}
