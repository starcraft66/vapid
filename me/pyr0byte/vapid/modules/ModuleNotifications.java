package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.PlayerEnterVisualRangeEvent;
import me.pyr0byte.vapid.events.PlayerLeaveVisualRangeEvent;
import me.pyr0byte.vapid.events.PlayerLogOffEvent;
import me.pyr0byte.vapid.events.PlayerLogOnEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ModuleNotifications extends ModuleBase 
{
	// True is chat box, false is Vapid GUI
	boolean useHud;
	boolean visualRange;
	boolean onLog;
	boolean logOffLoc;
	
	public ModuleNotifications(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = false;
		
		this.aliases.add("note");
		this.aliases.add("notify");
		
		this.name = "Notifications";
		
		this.command = new Command(this.vapid, this, aliases, "Notifies you of server events");
		this.command.registerArg("see", new Class[] {}, "Toggles if you get a notification when a player enters or leaves your visual range");
		this.command.registerArg("log", new Class[] {}, "Toggles if you see a notification when a player logs in or out");
		this.command.registerArg("hud", new Class[] {}, "Toggles if the notifications for \"see\" are displayed on the HUD/GUI");
		this.command.registerArg("loc", new Class[] {}, "Say where a player logged off");

		this.useHud = true;
		this.visualRange = true;
		this.onLog = true;
		this.logOffLoc = true;

	}
	
	@EventHandler
	public void onPlayerLogIn(PlayerLogOnEvent e)
	{
		// TODO: Greet
		if(this.isEnabled && this.onLog)
		{
			if(!e.getUsername().equals(mc.thePlayer.getCommandSenderName()))
				vapid.yellowMessage(e.getUsername() + " joined the game");
		}
	}
	
	@EventHandler
	public void onPlayerLogOff(PlayerLogOffEvent e)
	{
		if(this.isEnabled)
		{
			if(!e.getUsername().equals(mc.thePlayer.getCommandSenderName()))
			{
				if(this.onLog)
					vapid.yellowMessage(e.getUsername() + " left the game");

				
				if(this.logOffLoc)
				{
					EntityPlayer p = this.mc.theWorld.getPlayerEntityByName(e.getUsername());
					if(p != null)
						this.vapid.message(p.getCommandSenderName() + " logged off at (" + p.posX + ", " + p.posY + ", " + p.posZ + ")");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerEnterVisualRange(PlayerEnterVisualRangeEvent e)
	{
		if(this.isEnabled && this.visualRange)
		{
			if(!e.getPlayer().getCommandSenderName().equals(mc.thePlayer.getCommandSenderName()))
				vapid.notificationMessage("§4§l!!!§r " + e.getPlayer().getCommandSenderName() + " entered visual range", this.useHud);
		}
	}
	
	@EventHandler
	public void onPlayerLeaveVisualRange(PlayerLeaveVisualRangeEvent e)
	{
		if(this.isEnabled && this.visualRange)
		{
			if(!e.getPlayer().getCommandSenderName().equals(mc.thePlayer.getCommandSenderName()))
				vapid.notificationMessage("§b§l...§r " + e.getPlayer().getCommandSenderName() + " left visual range", this.useHud);
		}
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("see"))
		{
			this.visualRange = !this.visualRange;
			this.vapid.confirmMessage("Visual range notifications " + (this.visualRange ? "enabled" : "disabled"));
		}
		else if(name.equals("log"))
		{
			this.onLog = !this.onLog;
			this.vapid.confirmMessage("Log on/off notifications " + (this.visualRange ? "enabled" : "disabled"));

		}
		else if(name.equals("hud"))
		{
			this.useHud = !this.useHud;
			this.vapid.confirmMessage("HUD notifcations " + (this.useHud ? "enabled" : "disabled"));
		}
		else if(name.equals("loc"))
		{
			this.logOffLoc = !this.logOffLoc;
			this.vapid.confirmMessage("Log off locations " + (this.logOffLoc ? "enabled" : "disabled"));
		}
	}

	@Override
	public String getMetadata()
	{
		if(!this.visualRange && !this.useHud && !this.onLog && !this.logOffLoc)
			return "";
		
		String ret = "(";
		ret += this.visualRange ? "See, " : "";
		ret += this.onLog ? "Log, " : "";
		ret += this.useHud ? "HUD, " : "";
		ret += this.logOffLoc ? "Loc, " : "";
		
		if(ret.endsWith(", "))
			ret = ret.substring(0, ret.length() - 2);
		
		return ret + ")";
	}
}
