package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleTimer extends ModuleBase 
{
	
	float speed;
	
	public ModuleTimer(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = false;
		
		this.command = new Command(this.vapid, this, aliases, "Broken by update; changes tick speed");
		this.command.registerArg("interval", new Class[] { Float.class }, "Tick speed");
		this.defaultArg = "interval";
		this.isToggleable = false;
		this.speed = 1.0F;
	}
	
	
	@Override
	public void processArguments(String name, String argv[])
	{
		try
		{
			this.speed = Float.parseFloat(argv[0]);
		}
		catch (NumberFormatException e)
		{
			vapid.errorMessage("That isn't a number, you imbecile.");
		}

		mc.timer.timerSpeed = speed;
	}
	
	@Override
	public String getMetadata()
	{
		return this.speed == 1.0F ? "" :  "(" + Float.toString(this.speed) + ")";
	}

}
