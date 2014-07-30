package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleNoSlow extends ModuleBase 
{
	
	public ModuleNoSlow(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		aliases.add("ns");

		this.name = "No Slow";
		
		this.command = new Command(this.vapid, this, aliases, "Prevents you from slowing down while eating, shooting a bow, walking in soul sand, etc.");
	}

}
