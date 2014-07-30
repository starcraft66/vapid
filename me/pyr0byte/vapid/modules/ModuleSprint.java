package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleSprint extends ModuleBase 
{
	
	public ModuleSprint(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "you're always sprinting");
		
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) 
		{
			if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking())
				mc.thePlayer.setSprinting(true);

		}
		
	}

}


