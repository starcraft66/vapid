package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleChunkMarker extends ModuleBase 
{
	
	public float yaw;
	
	public ModuleChunkMarker(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("yaw");
		this.yaw = 0F;
		
		this.command = new Command(this.vapid, this, aliases);
		

	}
	
	@Override
	public void onEnable()
	{
		this.isEnabled = true;
		this.yaw = Math.round((mc.thePlayer.rotationYaw + 1F) / 45F) * 45F;
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.thePlayer.rotationYaw = this.yaw;
		}
		
	}
	
	@Override
	public void processArguments(String name, String[] argv)
	{
		if(name.equals("yaw"))
		{
			this.yaw = Float.parseFloat(argv[0]);
		}
	}
	
	@Override
	public String getMetadata()
	{
		return "(" + Float.toString(this.yaw) + ")";
	}

}
