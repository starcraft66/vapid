package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Argument;
import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleHelp extends ModuleBase 
{	
	public ModuleHelp(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		
		this.command = new Command(this.vapid, this, aliases, "Sending cached base coords to Pyrobyte...");
		this.command.registerArg("module", new Class[] { String.class }, "Gives help for modules");

		this.defaultArg = "module";
	}

	@Override
	public void toggleState()
	{
		this.processArguments("module", null);
	}

	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("module"))
		{
			
			if(argv == null || !this.vapid.moduleNameCache.values().contains(argv[0]))
			{

				this.vapid.message("An insipid and banal client by Pyrobyte; simple and reliable and §mprobably§r definitely has no backdoors.");
				this.vapid.message("help (module); all modules:");
				
				String modules = "";
				
				for(ModuleBase m : this.vapid.modules)
				{
					
					modules += m.aliases.get(0) + ", ";
				
				}
				
				this.vapid.message(modules);
			}
			else
			{
				ModuleBase m = this.vapid.moduleCache.get(argv[0]);
				Command c = m.command;
				int i = 0;
				String aliases = "§n§l";
				
				for(String s : c.aliases)
				{
					i++;
					if(i > c.aliases.size())
						break;
					
					if(i > c.aliases.size() - 1)
						aliases += s;
					else
						aliases += s + ", ";
				}
				
			   aliases += ":§r " + c.getDescription();
			   
			   this.vapid.message(aliases);
				
			   String argument = "";
			   String arg = "";
			   String usage[];
			   
			   for(Argument a : c.args.values())
			   {
				   arg = a.getId();
				   argument = "§l" + (m.defaultArg.equals(arg) ? ("§l§d" + arg) : arg) + ":§r ";
				   
				   usage = a.getUsage().split("\n");
				   
				   argument += usage[0];
				   
				   this.vapid.message(argument);
				   				   
				   if(usage.length > 1)
				   {
					   boolean fuckOffPopbob = true;
					   
					   for(String str : usage)
					   {
						   if(fuckOffPopbob)
						   {
							   fuckOffPopbob = false;
							   continue;
						   }
						   
						   this.vapid.message("  " + str);
					   }
				   }
			   }
			}
		}
	}
	
}
