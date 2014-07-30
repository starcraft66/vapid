package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;


public class ModuleNoFall extends ModuleBase 
{
	float tolerance;
	
	public ModuleNoFall(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.name = "No Fall";
		this.command = new Command(this.vapid, this, aliases, "Catches you after you've fallen a certain distance by turning on fly");
		this.command.registerArg("tolerance", new Class[] { Float.class }, "How far you can fall, in meters, before fly gets turned on");
		this.defaultArg = "tolerance";
		this.tolerance = 5.0F;
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) 
		{
			if(mc.thePlayer.fallDistance > tolerance)
			{
				vapid.getModule("fly").isEnabled = true;
			}

		}
		
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("tolerance"))
		{
			this.tolerance = Float.parseFloat(argv[0]);
		}
	}

	@Override
	public String getMetadata()
	{
		return this.tolerance == 5.0F ? "" : ("(" + Float.toString(this.tolerance) + ")");
	}
}
