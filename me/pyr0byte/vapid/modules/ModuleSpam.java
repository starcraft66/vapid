package me.pyr0byte.vapid.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Util;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class ModuleSpam extends ModuleBase {
	
	ArrayList<String> messages;
	long currentMs, lastMs;
	long intervalMs = 1000;
	int index = 0;
	int whisperIndex = 0;
	String file = "";
	int charsPerLine = 100;
	Boolean fileLoaded = false;
	boolean whisperMode = false;
	ArrayList<String> whisperRecipients;
	
	/*
	 * By iTristan
	 */
	
	public ModuleSpam(Vapid vapid, Minecraft mc) {
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		
		this.command = new Command(this.vapid, this, aliases, "By iTristan. Lets you spam chat using a predefined text file.");
		this.command.registerArg("load", new Class[] {String.class}, "Loads a text file in your mc directory.");
		this.command.registerArg("list", new Class[] {}, "Lists the currently loaded text file.");
		this.command.registerArg("interval", new Class[] {Long.class}, "Set the interval between sent messages.");
		this.command.registerArg("resetindex", new Class[] {}, "Resets the index of the spam file.");
		this.command.registerArg("whisper", new Class[] {}, "Toggles whisper mode");
		this.command.registerArg("wadd", new Class[] {String.class}, "Adds a recipient for whisper mode");
		this.command.registerArg("wdelall", new Class[] {}, "Deletes all the players on the whisper list");
		this.command.registerArg("wlist", new Class[] {}, "Lists whisper mode recipients");
		this.command.registerArg("wdel", new Class[] {String.class}, "Removes a whisper mode recipient");

		this.whisperRecipients = new ArrayList<String>();
		
		this.defaultArg = "load";
		this.needsTick = true;
				
	}
	
	@Override
	public void processArguments(String name, String[] argv)
	{
		if(name.equals("interval"))
		{
			this.intervalMs = Long.parseLong(argv[0]);
		}
		else if(name.equals("load"))
		{
			File f = new File(argv[0]);
			if (f.exists()) {
				this.file = argv[0];
				vapid.notificationMessage("Loaded spam file " + file);
				fileLoaded = true;
			} else {
				vapid.errorMessage("Failed to load file " + argv[0]);
				fileLoaded = false;
			}
			
		}
		else if(name.equals("list"))
		{
			vapid.notificationMessage("Currently loaded spam file: " + file); 
		}
		else if(name.equals("resetindex"))
		{
			index = 0;
			vapid.notificationMessage("Index reset."); 
		}
		else if(name.equals("whisper"))
		{
			this.whisperMode = !this.whisperMode;
			
			this.vapid.confirmMessage(this.whisperMode ? "Whisper mode ON" : "Whisper mode OFF");
		}
		else if(name.equals("wadd"))
		{
			String player = this.isPlayerOnline(argv[0]);
			if(player != null)
			{
				this.whisperRecipients.add(player.toLowerCase());
				this.vapid.confirmMessage("Added " + player);
			} else
				this.vapid.errorMessage(argv[0] + " is not online");
		}
		else if(name.equals("wdel"))
		{
			String player = argv[0];
			if(this.whisperRecipients.contains(player.toLowerCase()))
			{
				this.whisperRecipients.remove(player.toLowerCase());
				this.vapid.confirmMessage(player + " was removed");
			}
			else
				this.vapid.errorMessage("That player is not on the list");

		}
		else if(name.equals("wlist"))
		{
			String list = "";
			for(String s : this.whisperRecipients)
			{
				list += s + ", ";
			}
			this.vapid.message(list.substring(0, list.length() - 2));
		}
		else if(name.equals("wdelall"))
		{
			this.whisperRecipients.clear();
			this.vapid.confirmMessage("Cleared!");
		}
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
	@Override
	public void onTick() {
		
		this.currentMs = System.nanoTime() / 1000000;
		
	
  	  if(this.isEnabled && (this.currentMs - this.lastMs) >= this.intervalMs)
  	  {
  		  this.currentMs = System.nanoTime() / 1000000;
  		  
  		  if (index >= messages.size()) {
  			  index = 0;
  			  System.out.println("INDEX HAS BEEN RESET NIGGA!!!!!");
  		  }
  		  
  		  if(messages.get(index).length() <= 100) {
  			  if(this.whisperMode)
  			  {
    					String isOnline = this.isPlayerOnline(this.whisperRecipients.get(this.whisperIndex));
    					if(isOnline != null)
    					{
    						mc.thePlayer.sendChatMessage("/w " + this.whisperRecipients.get(this.whisperIndex) + " " + messages.get(index));
    					}
    					else
      					{
      						this.whisperRecipients.remove(this.whisperRecipients.get(this.whisperIndex));
      						this.vapid.confirmMessage("Removed " + this.whisperRecipients.get(this.whisperIndex) + " from whisper list because they are not online");
      					}
    						
  			  } else
  				  mc.thePlayer.sendChatMessage(messages.get(index));
  		  } else {
  			
  			  if(this.whisperMode)
  			  {
  					String isOnline = this.isPlayerOnline(this.whisperRecipients.get(this.whisperIndex));
  					if(isOnline != null)
  					{
		  				this.charsPerLine = 100 - (this.whisperRecipients.get(this.whisperIndex).length());
		  				int numberOfSplits = (int) Math.ceil(messages.get(index).length() / charsPerLine);
			  			for (int i = 0;i <= numberOfSplits; i++) {
			  				mc.thePlayer.sendChatMessage("/w " + this.whisperRecipients.get(this.whisperIndex) + " " + messages.get(index).substring(i * charsPerLine, (((i + 1) * charsPerLine)) > messages.get(index).length() ? messages.get(index).length() : ((i + 1) * charsPerLine)));
			  			}
  					}
  					else
  					{
  						this.whisperRecipients.remove(this.whisperRecipients.get(this.whisperIndex));
  						this.vapid.confirmMessage("Removed " + this.whisperRecipients.get(this.whisperIndex) + " from whisper list because they are not online");
  					}
  				
  			  }
  			  else
  			  {
  				  this.charsPerLine = 100;
	  			int numberOfSplits = (int) Math.ceil(messages.get(index).length() / charsPerLine);
	  			for (int i = 0;i <= numberOfSplits; i++) {
	  				mc.thePlayer.sendChatMessage(messages.get(index).substring(i * 100, (((i + 1) * 100)) > messages.get(index).length() ? messages.get(index).length() : ((i + 1) * 100)));
	  			}
  			  }
  		  }

		  if(this.whisperMode)
			  this.whisperIndex++;
		  
		  if(this.whisperIndex > this.whisperRecipients.size() - 1)
			  this.whisperIndex = 0;
		  
		  if(this.whisperMode && this.whisperIndex == 0)
			  index++;
		  else if(!this.whisperMode)
			  index++;
		  
  		  this.lastMs = System.nanoTime() / 1000000;
  		  
  	  }
		
	}
	
	@Override
	public void onEnable() {
		
		
		
		if (file == "") {
			vapid.errorMessage("You must load a file first, silly!");
			return;
		} else {
			this.isEnabled = true;
			messages = this.vapid.readUTF8Lines(file);
		}
		
		
		
	}
	
	@Override
	public void onDisable() {
		
		this.isEnabled = false;
		
	}
	
	@Override
	public String getMetadata() {
		String ret = "( " + intervalMs + "ms - " + (fileLoaded ? "File: " + file : "") + " )";
		if(this.whisperMode)
			ret += " [Whisper]";
		
		return ret;
	}
	

}
