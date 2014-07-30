package me.pyr0byte.vapid.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.ChatReceivedEvent;
import me.pyr0byte.vapid.events.PlayerLogOffEvent;
import me.pyr0byte.vapid.events.PlayerLogOnEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;

public class ModuleGreet extends ModuleBase 
{
	
	boolean onJoin;
	boolean onLeave;
	ArrayList<String> ignoredPlayers;
	String syncWith;
	boolean syncing;
	
	public ModuleGreet(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.vapid, this, aliases, "Greets");
		this.command.registerArg("join", new Class[] {}, "Welcome");
		this.command.registerArg("leave", new Class[] {}, "Good bye");
		this.command.registerArg("ignore", new Class[] {String.class}, "Adds a player to the ignore list");
		this.command.registerArg("unignore", new Class[] {String.class}, "Removes a player from the ignore list");
		this.command.registerArg("ignored", new Class[] {}, "Lists ignored players");
		this.command.registerArg("unignoreall", new Class[] {}, "Removes all ignored players");
	//	this.command.registerArg("syncing", new Class[] {}, "Experimental; Synchronize messages with another player");
	//	this.command.registerArg("syncwith", new Class[] {String.class}, "Experimental; Synchronize messages with another player");


		this.ignoredPlayers = new ArrayList<String>();
		
		File ignored = new File("greeter_ignored.vpd");
		
		if(ignored.exists())
		{
			this.ignoredPlayers = this.vapid.readLines("greeter_ignored.vpd");
		}
		
		this.syncWith = null;
		
		this.syncing = false;
		this.onJoin = true;
		this.onLeave = true;
	}
	
	String greetings[] = {"Hey", "Welcome", "Hello", "Hi", "Greetings", "Salutations", "Good to see you", "%"};
	String goodbyes[] = {"Later", "So long", "See you later", "Good bye", "Bye", "Farewell"};

	public void writeIgnoredPlayers()
	{
		String filename = "greeter_ignored.vpd";
		File ignored = new File(filename);
		
		this.vapid.writeLines(filename, this.ignoredPlayers);
	}
	

	public String isPlayerOnline(String username)
	{
		String s;
		
		for(Object o : mc.thePlayer.sendQueue.playerInfoList)
		{
			s = ((GuiPlayerInfo)o).name;
			if(s.equalsIgnoreCase(username))
				return s;
		}
		
		return null;
	}
	
	@EventHandler
	public void onChatReceived(ChatReceivedEvent e)
	{

		// only works on servers without modified chat; not gonna mess with that.
		String player = e.getMessage().substring(0, e.getMessage().indexOf(' ')+1);
		player = player.substring(1, player.length() - 2);
		String message = e.getMessage().substring(e.getMessage().indexOf(' ')+1);
		
		if(this.syncWith != null && this.syncWith.equalsIgnoreCase(player))
		{
			if(message.matches(">\\s(!{3}|\\.{3})\\s.*,\\s.*(!|\\.)"))
			{
								
				String[] aaa = message.split(" ");
				String to = aaa[aaa.length-1];
				to = to.substring(0, to.length()-1);
				
				// 1 is greet 0 is farewell
				boolean type = aaa[1].equals("!!!") ? true : false;
				
				if(type)
					this.greeting(to);
				else
					this.farewell(to);
				
			}
		}

	}
	
	public String toFull(String s)
	{
		String ret = "";
		char[] c = s.toCharArray();
		
		for(char h : c)
		{
			
			if(h != ' ')
				ret += (char)((h | 0x10000) + 0xFEE0);
			else
				ret += ' ';
		}
		
		System.out.print(ret);
		
		return ret;
	}
	
	public void greeting(String user)
	{
		if(!(this.isEnabled && this.onJoin && !this.ignoredPlayers.contains(user.toLowerCase())))
			return;
		
		Random rand = new Random();
		int i = rand.nextInt(greetings.length);
		String period = rand.nextInt(2) > 1 ? "." : "!";
		String greet = greetings[i];
		
		if(greet.equals("%"))
		{
			Date d = new Date();
			int t = d.getHours();
			
			if(t < 11 && t > 2)
			{
				greet = "Morning";
			}
			else if(t > 11 && t < 18)
			{
				greet = "Afternoon";
			}
			else
			{
				greet = "Evening";
			}
		}
		
		mc.thePlayer.sendChatMessage("> !!! " + greet + ", " + user + period);
	}
	
	public void farewell(String user)
	{
		
		if(!(this.isEnabled && this.onLeave && !this.ignoredPlayers.contains(user.toLowerCase())))
			return;
		
		Random rand = new Random();
		int i = rand.nextInt(goodbyes.length);
		String period = rand.nextInt(2) > 1 ? "." : "!";
		
		mc.thePlayer.sendChatMessage("> ... " + goodbyes[i] + ", " + user + period);	
	}
	
	@EventHandler
	public void onPlayerLogIn(PlayerLogOnEvent e)
	{		
		if(e.getUsername().equals("BibleBot")) {
			mc.thePlayer.sendChatMessage("!verse Leviticus 18:32");
		}
		
		this.greeting(e.getUsername());
	}
	
	@EventHandler
	public void onPlayerLogOff(PlayerLogOffEvent e)
	{
		this.farewell(e.getUsername());
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("join"))
		{
			this.onJoin = !this.onJoin;
		}
		else if(name.equals("leave"))
		{
			this.onLeave = !this.onLeave;
		}
		else if(name.equals("ignore"))
		{
			String player = argv[0].toLowerCase();
			if(!this.ignoredPlayers.contains(player))
			{
				this.ignoredPlayers.add(player);
				this.vapid.confirmMessage("Ignored " + player);
				this.writeIgnoredPlayers();

			}
			else
				this.vapid.errorMessage("As much as you hate " + argv[0] + ", you can only ignore him once");

		}
		else if(name.equals("unignore"))
		{
			String player = argv[0].toLowerCase();
			if(this.ignoredPlayers.contains(player))
			{
				this.ignoredPlayers.remove(player);
				this.vapid.confirmMessage("Unignored " + player);

				this.writeIgnoredPlayers();
			}
			else
				this.vapid.errorMessage("You never ignored " + argv[0]);

		}
		else if(name.equals("syncing"))
		{
			this.syncing = !this.syncing;
		}
		else if(name.equals("syncwith"))
		{
			if(this.isPlayerOnline(argv[0]) != null)
			{
				this.syncWith = this.isPlayerOnline(argv[0]);
			}
			else
				this.vapid.errorMessage("That player isn't online :^(");
		}
		else if(name.equals("ignored"))
		{
			if(this.ignoredPlayers.isEmpty())	
			{
				this.vapid.errorMessage("Nobody ignored");
				return;
			}
			
			String ret = "";
			for(String s : this.ignoredPlayers)
			{
				ret += s + ", ";
			}
			
			ret = ret.substring(0, ret.length() - 2);
			this.vapid.confirmMessage(ret);
		}
		else if(name.equals("unignoreall"))
		{
			this.ignoredPlayers.clear();
			this.writeIgnoredPlayers();
			this.vapid.confirmMessage("Deleted all players from ignore list");
		}
	}
	
	@Override
	public String getMetadata()
	{
		String ret = "";
		if(this.onJoin && this.onLeave)
		{
			ret = "(Join, Leave)";
		}
		else if(this.onJoin)
		{
			ret = "(Join)";
		}
		else if(this.onLeave)
		{
			ret = "(Leave)";
		}
		else 
		{
			ret = "(Nothing!)";
		}
		
		if(this.syncing)
		{
			ret += " [Synced: " + this.syncWith + "]";
		}
		
		return ret;
			
	}
}
