package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleFly extends ModuleBase 
{
	float speed;
	
	public ModuleFly(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
			
		this.speed = 0.05F;
		
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "flies");
		this.command.registerArg("speed", new Class[] { Float.class }, "how fast, default 0.05F, highest speed on ice is 0.065, with speed II is 0.09");
		this.defaultArg = "speed";

	}
	
	@Override
	public void onEnable()
	{
		mc.thePlayer.capabilities.isFlying = true;
		this.isEnabled = true;
	}
	
	@Override
	public void onDisable()
	{
		mc.thePlayer.capabilities.isFlying = false;
		this.isEnabled = false;
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.thePlayer.capabilities.isFlying = true;
			mc.thePlayer.capabilities.setFlySpeed(speed);
		}
		
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("speed"))
		{
			this.speed = Float.parseFloat(argv[0]);
		}
	}
	
	@Override
	public String getMetadata()
	{
		return this.speed == 0.05F ? "" :  "(" + Float.toString(speed) + ")";
	}
}
