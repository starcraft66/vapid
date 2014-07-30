package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.ChatSentEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class ModuleTextWidth extends ModuleBase 
{

	int add;
	public ModuleTextWidth(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		
		aliases.add("width");
		this.add = 0xFFEE0;
		
		this.command = new Command(this.vapid, this, aliases, "Converts chats to full-width unicode");
		this.command.registerArg("val", new Class[] {Integer.class});
		this.defaultArg = "val";
	}
	
	public String toFull(String s)
	{
		String ret = "";
		char[] c = s.toCharArray();
		
		for(char h : c)
		{
			
			if(h != ' ')
				ret += (char)((h | 0x10000) + this.add);
			else
				ret += ' ';
		}
		
		System.out.print(ret);
		
		return ret;
	}
	
	@EventHandler
	public boolean onPlayerChat(ChatSentEvent e)
	{
		
		if(this.isEnabled)
		{
			String message = "";
			if(e.getMessage().startsWith(">"))
			{
				message = ">" + this.toFull(e.getMessage().substring(1));
			}
			else if(e.getMessage().startsWith("/r "))
			{
				message = "/r " + this.toFull(e.getMessage().substring(3));
			}
			else if(e.getMessage().startsWith("/p "))
			{
				message = "/p " + this.toFull(e.getMessage().substring(3));
			}
			else if(e.getMessage().startsWith("/w "))
			{
				message = e.getMessage().substring(3);
				int split = message.indexOf(" ");
				String user = message.substring(0, split);
				message = "/w " + user + " " + this.toFull(message.substring(split));
			}
			else
				message = this.toFull(e.getMessage());
			
			mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
			return false;
		}
		else
			return true;
	}
	
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("val"))
		{
			this.add = Integer.parseInt(argv[0]);
		}
	}
	
	@Override
	public String getMetadata()
	{
		return this.add == 0xFFEE0 ? "" : ("(" + Integer.toString(this.add) + ")");
	}
	
}
