package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleAutobridge extends ModuleBase 
{
	
	public ModuleAutobridge(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("bridge");
		
		this.command = new Command(this.vapid, this, aliases, "as if you were holding S, Shift, and Right Click");

	}

   
	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		mc.gameSettings.keyBindBack.pressed = false;
		mc.gameSettings.keyBindUseItem.pressed = false;
		mc.gameSettings.keyBindSneak.pressed = false;
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.gameSettings.keyBindBack.pressed = true;
			mc.gameSettings.keyBindUseItem.pressed = true;
			mc.gameSettings.keyBindSneak.pressed = true;

		}
		
	}

}
