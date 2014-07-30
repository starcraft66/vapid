package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;


public class ModuleBreakMask extends ModuleBase 
{
	public int id;
	
	public ModuleBreakMask(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.vapid, this, aliases, "Only allows you to break blocks of the given type.");
		this.command.registerArg("id", new Class[] { Integer.class }, "The only ID you want to break.");
		this.defaultArg = "id";
		
		this.id = 4;
	}

	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("id"))
		{
			this.id = Integer.parseInt(argv[0]);
			this.vapid.confirmMessage("Now masking block: " + Integer.toString(id));
		}
	}

	@Override
	public String getMetadata()
	{
		return  ("(" + Integer.toString(this.id) + ")");
	}
}
