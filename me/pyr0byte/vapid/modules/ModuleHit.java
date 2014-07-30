package me.pyr0byte.vapid.modules;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleHit extends ModuleBase 
{
	
	public ModuleHit(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.vapid, this, aliases, "Hits you");
	}
	
	@Override
	public void onEnable() {}
	
	@Override
	public void onDisable() {}
	
	@Override
	public void toggleState()
	{
		
		  mc.playerController.attackEntity(mc.thePlayer, mc.thePlayer);
		
	}

}
