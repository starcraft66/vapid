package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;


public class ModuleFastBreak extends ModuleBase 
{
	public float tolerance;
	
	public ModuleFastBreak(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.vapid, this, aliases, "Speeds up block breaking. Only works well with Efficiency V on soft blocks like stone.");
		this.command.registerArg("tolerance", new Class[] { Float.class }, "percent of original break speed, default 0.5 (0 through 1)");
		this.defaultArg = "tolerance";
		this.tolerance = 0.5F;
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
		return this.tolerance == 0.5F ? "" : ("(" + Float.toString(this.tolerance) + ")");
	}
}
