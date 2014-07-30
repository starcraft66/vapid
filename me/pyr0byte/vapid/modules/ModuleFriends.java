package me.pyr0byte.vapid.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleFriends extends ModuleBase 
{

	ArrayList<String> friends;
	final String filename = "friendslist.vpd";
	
	public ModuleFriends(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.isToggleable = true;
		
		this.command = new Command(this.vapid, this, aliases, "friends list for various purposes");
		this.command.registerArg("add", new Class[] { String.class }, "add a friend (ex. friend add Pyrobyte)");
		this.command.registerArg("del", new Class[] { String.class }, "delete a friend (ex. friend del chrisleighton)");
		this.command.registerArg("off", new Class[] { }, "temporarily removes all entries; toggles friends (in case you get caesar'd)");
		this.isEnabled = true;
		
		this.defaultArg = "add";

		this.friends = new ArrayList<String>();
		this.showEnabled = false;
		
		this.populateFriendData(this.filename);
	}

	@Override
	public void onEnable()
	{
		this.isEnabled = true;
		this.vapid.confirmMessage("Friends enabled.");
	}
	
	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		this.vapid.notificationMessage("FRIENDS DISABLED. DON'T ACCIDENTALLY KILL ANYONE!");
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		
		String friend = argv[0].toLowerCase();
		
		if(name.equals("add"))
		{
			if(!this.friends.contains(friend))
			{
				this.friends.add(friend);
				this.saveFriendData(this.filename);
				vapid.confirmMessage("Added " + argv[0] + " to friends list");

			}
			else
			{
				vapid.errorMessage("That friend has already been added");
			}
		}
		else if(name.equals("del"))
		{
			if(this.friends.contains(friend))
			{
				this.friends.remove(friend);
				this.saveFriendData(this.filename);
			}
			else
			{
				vapid.errorMessage("That friend doesn't exist");
			}
		}
	}
	
	public boolean isFriend(String name)
	{
		return this.isEnabled && this.friends.contains(name.toLowerCase());
	}
	
	public void populateFriendData(String filename) 
	{
		
	    if (!this.friends.isEmpty()) 
	    {
		      this.friends.clear();
		}
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {
	    	return;
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new FileReader(filename));
	     
	     String line;
	     while ((line = br.readLine()) != null)
	     {
	        this.friends.add(line);
	     }
	      
	     	br.close();
	     	
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
	}
	
	public void saveFriendData(String filename)
	{			  
		  try 
		  {
		        File f = new File(filename);
		
		        if (f.exists()) 
		        {
		        	f.delete();
		        	f.createNewFile();
		        }
		
		        FileWriter fw = new FileWriter(filename);
		        BufferedWriter bw = new BufferedWriter(fw);
		
				for(Iterator<String> i = friends.iterator(); i.hasNext();) {
					String friend = i.next();

					bw.write(friend + "\n");
					
				}
		
		        bw.close();
		        
		     
	    }
	    catch (IOException localIOException)
	    {
	    	localIOException.printStackTrace();
	    }
			  
	}

}
