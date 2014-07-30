package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleAutorespawn extends ModuleBase 
{
	
	public ModuleAutorespawn(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("respawn");
		
		this.command = new Command(this.vapid, this, aliases, "Automatically respawns upon death");

	}

   
	@Override
	public void onTick()
	{
		if(this.isEnabled && mc.thePlayer.isDead)
			mc.thePlayer.respawnPlayer();
	}

}
