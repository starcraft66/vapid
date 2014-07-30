package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleAutowalk extends ModuleBase 
{
	
	public ModuleAutowalk(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("walk");
		
		this.command = new Command(this.vapid, this, aliases, "as if you were holding \"W\"");

	}

	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		mc.gameSettings.keyBindForward.pressed = false;

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.gameSettings.keyBindForward.pressed = true;
		}
		
	}

}
