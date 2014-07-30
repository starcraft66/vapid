package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleStep extends ModuleBase 
{
	
	public ModuleStep(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub		
		
		this.needsTick = true;
		this.command = new Command(this.vapid, this, aliases, "Partially broken, very shitty. Automatically jumps up blocks.");
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled)
		{
			if(mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isInWater())
			{
				mc.thePlayer.boundingBox.offset(0.0D, 1.0628D, 0.0D);
				mc.thePlayer.isCollidedHorizontally = false;
				
			}
		
		}
	}

}
