package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;


public class ModuleNoKnockback extends ModuleBase 
{	
	public ModuleNoKnockback(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		this.aliases.add("nk");	
		this.aliases.add("knock");		

		this.name = "No Knockback";
		
		this.command = new Command(this.vapid, this, aliases, "Prevents knockback from swords, bows, etc.");
	}
}
