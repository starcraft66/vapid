package me.pyr0byte.vapid.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;

import org.lwjgl.input.Keyboard;

public class ModuleBindEditor extends ModuleBase 
{
	
	boolean keys[];
	int key;
	public Map<Integer, String> binds;
	String filename = "keybinds.vpd";
	
	public ModuleBindEditor(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		
		this.needsTick = true;
		this.isToggleable = false;
		aliases.add("bind");
		aliases.add("binds");

		this.command = new Command(this.vapid, this, aliases, "Binds command sets to keys. Separate commands with pipes, and surround the whole set with quotes if it includes spaces. (ex. markers; or fly|freecam; or \"fly 0.065|bright -0.025\"");
		this.command.registerArg("add", new Class[] { Character.class, String.class }, "(key) (command) | adds or replaces a keybind (ex. binds add j brightness)");
		this.defaultArg = "add";
		
		this.command.registerArg("del", new Class[] { Character.class }, "(key) | removes a keybind");
		
		keys = new boolean[256];
		binds = new HashMap<Integer, String>();
		
		this.loadKeybinds();

	}

	public void loadDefault()
	{
		binds.put(Keyboard.KEY_F, "fly");
		binds.put(Keyboard.KEY_G, "sprint");
	}
	
	public void loadKeybinds()
	{
		if(!binds.isEmpty())
			binds.clear();
		
		try
		{
			File f = new File(this.filename);
			
			if(!f.exists())
			{
				f.createNewFile();
				this.loadDefault();
				this.saveKeybinds();
			} else
			{
				BufferedReader br = new BufferedReader(new FileReader(this.filename));
				String line;
				String s[];
				
				while((line = br.readLine()) != null)
				{
					s = line.split(" ", 2);
					
					try
					{
						this.binds.put(Integer.parseInt(s[0]), s[1]);
					} catch(NumberFormatException e)
					{
						e.printStackTrace();
					}
				}
				
				br.close();
			}
			
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveKeybinds()
	{
		try
		{
			if(!binds.isEmpty())
			{
				File f = new File(this.filename);
				
				if(f.exists())
				{
					f.delete();
					f.createNewFile();
				}
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename));
				
				for(int key : this.binds.keySet())
				{
					bw.write(Integer.toString(key) + " " + this.binds.get(key) + "\n");
				}
				
				bw.close();
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public int stringToKey(String key)
	{
		return Keyboard.getKeyIndex(key.substring(0, 1).toUpperCase());
	}
	
	public void putBind(String key, String command)
	{
		this.binds.put(this.stringToKey(key), command);
	}
	
	public void deleteBind(String key)
	{
		this.binds.remove(this.stringToKey(key));
	}
	
	public boolean keyDown(int k)
	{
		if(mc.currentScreen != null && !(mc.currentScreen instanceof GuiChest))
			return false;
		
		if(Keyboard.isKeyDown(k) != keys[k])
			return keys[k] = !keys[k];
		else
			return false;
		
		
	}
	
	@Override
	public void onTick()
	{
		for(int i : binds.keySet())
		{
			if(this.keyDown(i))
			{
				for(String c : binds.get(i).split("\\|"))
				{
					vapid.events.onCommand(c);
				}
			}
		}	
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("add")) 
		{
			this.putBind(argv[0], argv[1]);
			vapid.confirmMessage("Put bind for key: " + argv[0] + "; " + argv[1]);
			this.saveKeybinds();
		}
		
		if(name.equals("del")) 
		{
			this.deleteBind(argv[0]);
			vapid.confirmMessage("Deleted bind for key: " + argv[0]);
			this.saveKeybinds();

		}
	}

}
