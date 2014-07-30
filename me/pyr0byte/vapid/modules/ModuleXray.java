package me.pyr0byte.vapid.modules;

import java.util.ArrayList;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleXray extends ModuleBase 
{
	
	ArrayList<Integer> blocks;
	public int opacity;
	
	public ModuleXray(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.vapid, this, aliases, "NOT WORKING YET! Lets you see through blocks to see other blocks");
		this.command.registerArg("add", new Class[] {String.class}, "Adds a block by id:metadata");
		this.command.registerArg("del", new Class[] {String.class}, "Deletes a block by id:metadata");
		this.command.registerArg("list", new Class[] {}, "List blocks");

		this.command.registerArg("opacity", new Class[] {Integer.class}, "Changes block opacity (0 - 255)");

		this.blocks = new ArrayList<Integer>();
		this.readIds();
		this.opacity = 90;
		
		this.defaultArg = "add";

	}

	@Override
	public void toggleState() {
		
		mc.renderGlobal.loadRenderers();
		
		if(this.isEnabled)
			this.onDisable();
		else
			this.onEnable();
	}
	
	private ArrayList<Integer> transcribeToInts(ArrayList<String> in)
	{
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(String s : in)
		{
			out.add(Integer.parseInt(s));
		}
		
		return out;
	}
	
	private ArrayList<String> transcribeToStrings(ArrayList<Integer> in)
	{
		ArrayList<String> out = new ArrayList<String>();
		for(int s : in)
		{
			out.add(Integer.toString(s));
		}
		
		return out;	
	}
	
	private void writeIds()
	{
		this.vapid.writeLines("xray.vpd", this.transcribeToStrings(blocks));
	}
	
	private void readIds()
	{
		this.blocks = this.transcribeToInts(this.vapid.readLines("xray.vpd"));	
	}
	
    public int parseBlock(int id, int metadata)
    {
    	return (id << 16) | metadata;
    }
    
    public int getBlockId(int block)
    {
    	return block >> 16;
    }
    
    public int getBlockMetadata(int block)
    {
    	return block & 255;
    }
  
	@Override
	public void processArguments(String name, String[] argv)
	{
		
    	int id, metadata, block;
    	id = 0;
    	metadata = 0;
    	block = 0;
    	String[] tmp;    	
    	
    	if(argv.length > 0 && !argv[0].equals("opacity")) 
    	{
	    	argv[0].split(":");
	    	tmp = argv[0].split(":");
	    	id = Integer.parseInt(tmp[0]);
	    	metadata = tmp.length > 1 ? Integer.parseInt(tmp[1]) : 0;	
	    	block = this.parseBlock(id, metadata);
    	}
	
		if(name.equals("add"))
		{
			if(!this.blocks.contains(block))
			{
				this.blocks.add(block);
				this.writeIds();
				mc.renderGlobal.loadRenderers();
			}
			else
				this.vapid.errorMessage("You have already added that block");
				
		}
		else if(name.equals("del"))
		{
			if(this.blocks.contains(block))
			{
				this.blocks.remove((Integer)block);
				this.writeIds();
				mc.renderGlobal.loadRenderers();
			}
			else
				this.vapid.errorMessage("You never added that block!");	
		}
		else if(name.equals("opacity"))
		{
			int o = Integer.parseInt(argv[0]);
			if(o >= 0 && o <= 255)
			{
				this.opacity = o;
				this.vapid.confirmMessage("Opacity set to " + argv[0]);
			}
			else
				this.vapid.errorMessage("Opacity must be of [0, 255]");
		}
		else if(name.equals("list"))
		{
			for(Integer i : this.blocks)
			{
				this.vapid.confirmMessage(Integer.toString(this.getBlockId(i)));
			}

		}
	}
	
	public boolean contains(int id, int metadata)
	{
		return this.blocks.contains(this.parseBlock(id, metadata));
	}

}
