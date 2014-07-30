package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ModuleSneak extends ModuleBase 
{
	
	int tick = 0;
	
	public ModuleSneak(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "you're always sneaking");

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled && tick % 2 == 0) 
		{	
			mc.thePlayer.movementInput.sneak = true;
		} else {
			mc.thePlayer.movementInput.sneak = false;
		}
		
		tick++;
		
	}

}


