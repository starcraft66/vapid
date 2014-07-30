package me.pyr0byte.vapid.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.ChatReceivedEvent;
import net.minecraft.client.Minecraft;

public class ModuleRegexIgnore extends ModuleBase 
{
	Map<String, String> regex;
	
	public ModuleRegexIgnore(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
						
		this.command = new Command(this.vapid, this, aliases, "Ignores messages by regular expression; not done");
		this.command.registerArg("add", new Class[] { String.class, String.class });
		this.command.registerArg("del", new Class[] { String.class });

		this.command.registerArg("list", new Class[] {});

		this.defaultArg = "add";
		this.name = "Regex Ignore";
		this.aliases.add("regex");
		this.aliases.add("ignore");

		this.regex = new HashMap<String, String>();
		
		this.read();
	}
	
	public void write()
	{
		ArrayList<String> out = new ArrayList<String>();
		for(String s : this.regex.keySet())
		{
			out.add(s + " " + this.regex.get(s));
		}
		
		this.vapid.writeLines("regexignore.vpd", out);
	}
	
	public void read()
	{
		ArrayList<String> in = this.vapid.readLines("regexignore.vpd");
		String tmp[];
		for(String s : in)
		{
			tmp = s.split(" ");
			this.regex.put(tmp[0], tmp[1]);
		}
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("add"))
		{
			this.regex.put(argv[0].toLowerCase(), argv[1]);
			this.vapid.confirmMessage("Added regex: " + argv[1] + " for key: " + argv[0]);
			this.write();
			
		}
		else if(name.equals("list"))
		{
			for(String key : this.regex.keySet())
			{
				this.vapid.confirmMessage("ID: " + key + " REGEX: " + this.regex.get(key));
				
			}
		}
		else if(name.equals("del"))
		{
			this.regex.remove(argv[0].toLowerCase());
			this.vapid.confirmMessage("Removed by key: " + argv[0].toLowerCase());
			this.write();
		}
	}
	

	@EventHandler
	public boolean onChatReceived(ChatReceivedEvent e)
	{
		if(this.isEnabled)
		{
			String msg = e.getMessage();
			
			msg.replaceAll("§", "[clr]");
			
			for(String key : this.regex.keySet())
			{
				if(msg.matches(this.regex.get(key)))
				{
					return false;
				}
			}
			
		}
		
		return true;

	}
}
