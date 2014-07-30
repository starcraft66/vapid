package me.pyr0byte.vapid.modules;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleGlide extends ModuleBase 
{
	double speed;
	
	public ModuleGlide(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
			
		this.speed = -0.17D;
		
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "Slows fall speed; NCP patched");
		this.command.registerArg("speed", new Class[] { Double.class });
		this.defaultArg = "speed";

	}
	
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			if(!mc.thePlayer.onGround && mc.thePlayer.motionY < 0.0D && mc.thePlayer.isAirBorne && !mc.thePlayer.isInWater())
			{
				mc.thePlayer.motionY = speed;
			}
		}
		
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("speed"))
		{
			this.speed = Double.parseDouble(argv[0]);
		}
	}
	
	@Override
	public String getMetadata()
	{
		return this.speed == 0.05F ? "" :  "(" + Double.toString(speed) + ")";
	}
}
