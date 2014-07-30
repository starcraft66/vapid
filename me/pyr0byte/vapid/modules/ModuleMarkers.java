package me.pyr0byte.vapid.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Location;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.BlockChangedEvent;
import me.pyr0byte.vapid.events.BlockRenderedEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class ModuleMarkers extends ModuleBase 
{

	/*
	 * Built with performance in mind§ I think.
	 * Beware convoluted bitwise operations which were used in place of classes for data storage
	 */
	
	// Maps inside of maps inside of maps inside of maps inside of maps inside of maps inside of maps inside of maps inside of maps inside of
	Map<Integer, Integer> blockDescriptors;
	
	Map<Location, Integer> blocks;
	ArrayList<Location> chunks;

	Iterator<Location> i;
	boolean iterating;
	boolean chunk;
	String mode;
	
	public ModuleMarkers(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		
		chunks = new ArrayList<Location>();

		blocks = new HashMap<Location, Integer>();
		blockDescriptors = new HashMap<Integer, Integer>();
						
		this.needsRendererTick = true;
		aliases.add("m");
		
		this.command = new Command(this.vapid, this, aliases, "Draws a marker of a given style over a given block. Replaces x-ray. All IDs are in the format id:metadata. If you do not provide metadata, it is assumed to be 0. (ex. markers new 98:1; or markers new 98) Comes preloaded with a marker for chests.");
		
		this.command.registerArg("new", new Class[] { String.class }, "Adds a new marker of the given ID and metadata with the default style, a white star.");
		this.command.registerArg("del", new Class[] { String.class }, "Deletes a marker of the given ID and metadata");
		this.command.registerArg("type", new Class[] { String.class, Integer.class }, "Changes the type of a marker; 0 is box, 1 is star, 2 is big rainbow star");
		this.command.registerArg("add", new Class[] { String.class, Integer.class, Integer.class, Integer.class, Integer.class }, "Adds a new marker of the given ID and metadata with colors R, G, B (see color) and the type (see type); (ex. markers new 98:2 255 0 0 1; adds a red star for mossy stone brick)");
		this.command.registerArg("color", new Class[] { String.class, Integer.class, Integer.class, Integer.class }, "Changes the color for a marker of the given ID and metadata in the format R G B, where integers R G and B are in the interval [0, 255]");
		this.command.registerArg("mode", new Class[] { String.class }, "Switches modes; if the mode does not exist, it is created. Modes let you store different sets of marker descriptors to switch between. (ex. mode nether) If a mode is not displayed on the GUI, it is default (ex. mode default). While you are in a mode, all markers you add will be saved for that mode.");
		this.command.registerArg("list", new Class[] { }, "Lists markers");
		this.command.registerArg("chunk", new Class[] { }, "Hilight quartz chunks in the nether");

		this.iterating = false;
		this.mode = "default";
		
		this.populateMarkerData("markers_" + this.mode + ".vpd");
	}
	
	@EventHandler
	public void onBlockChanged(BlockChangedEvent e)
	{	
		int block = this.parseBlock(e.newId, e.newMetadata);
		
		if(blockDescriptors.containsKey(block)) 
		{
			this.blocks.put(e.location, block);
		} else 
		{
			this.blocks.remove(e.location);
			
		}

	}
	
	// Oh what a mess; shouldn't we just call events.onBlockRendered()? 
	@EventHandler
	public void onBlockRendered(BlockRenderedEvent e)
	{	
		
		int block = this.parseBlock(e.id, /* Block.isNormalCube(e.id) && !Block.blocksList[e.id].hasTileEntity() ? e.metadata : */0);
				
		if(blockDescriptors.containsKey(block)) 
		{
			// If the marker is bedrock, it will only point out blocks in unnatural locations
			if(e.id == 7)
			{
				if(mc.theWorld.provider.dimensionId == -1 && (e.loc.y >= 5.0D && e.loc.y < 122.0D))
					this.blocks.put(e.loc, block);
				else if (mc.theWorld.provider.dimensionId != -1 && e.loc.y >= 5.0D)
					this.blocks.put(e.loc, block);

			}
			else
			{
				this.blocks.put(e.loc, block);
			}
		}
		else if(e.id == 153) // Quartz ore
		{
			if(!this.chunks.contains(this.getChunkLocation(e.loc)))
			{
				this.chunks.add(this.getChunkLocation(e.loc));
			}
		} 
	}

	public Location getChunkLocation(Location loc)
	{
		return new Location(loc.blockX >> 4, 0, loc.blockZ >> 4);
	}
	
	@Override
	public void onEnable()
	{
		mc.renderGlobal.loadRenderers();
		this.isEnabled = true;
	}
	
	@Override
	public void onRendererTick()
	{
		if(this.isEnabled)
		{
			this.drawMarkers();
		}
	}
	
	public void drawMarkers()
	{		
		int j2 = 225 % 0x10000;
		int k2 = 225 / 0x10000;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j2 / 1.0F, (float)k2 / 1.0F);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int id;
	
		for(Iterator<Location> i = blocks.keySet().iterator(); i.hasNext();) {
			Location loc = i.next();
			
			id = blocks.get(loc);
			
			if(loc.distanceFromSq(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerZ) > 65536.0D || !this.blockDescriptors.containsKey(id))
				i.remove();
			else
				this.drawMarker(loc, blockDescriptors.get(id));
		}

		if(this.chunk)
		{
			Location c;
			for(Iterator<Location> i = chunks.iterator(); i.hasNext();)
			{			
				c = i.next();
				
				if(c.distanceFromSq(TileEntityRendererDispatcher.staticPlayerX / 16, TileEntityRendererDispatcher.staticPlayerZ / 16) > 65536.0D)
					i.remove();
				else
					this.drawMarker(AxisAlignedBB.getBoundingBox(c.blockX * 16, 128, c.blockZ * 16, (c.blockX * 16) + 16, 128, (c.blockZ * 16) + 16), -256);
			}
		}

		}
	
	// 0 is box, 1 is star, 2 is diagonal line
    private void drawMarker(AxisAlignedBB aabb, int desc)
    {
    	
    	GL11.glBlendFunc(770, 771);
    	GL11.glEnable(3042);
    	GL11.glLineWidth(1.0F);
    	
    	// Hopefully works...
    	GL11.glColor3d(((desc >> 24) & 255) / 255.0D, ((desc >> 16) & 255) / 255.0D, ((desc >> 8) & 255) / 255.0D);
    	
    	GL11.glDisable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_LINE_SMOOTH);
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glDisable(GL11.GL_FOG);
    	GL11.glDisable(2929);
    	GL11.glDepthMask(false);

    	
    	Tessellator t = Tessellator.instance;
  
    	double minX = aabb.minX - TileEntityRendererDispatcher.staticPlayerX;
    	double minZ = aabb.minZ - TileEntityRendererDispatcher.staticPlayerZ;
    	double minY = aabb.minY - TileEntityRendererDispatcher.staticPlayerY;
    	
    	double maxX = aabb.maxX - TileEntityRendererDispatcher.staticPlayerX;
    	double maxZ = aabb.maxZ - TileEntityRendererDispatcher.staticPlayerZ;
    	double maxY = aabb.maxY - TileEntityRendererDispatcher.staticPlayerY;
    	
    	
    	if((desc & 255) == 0)
    	{
	        t.startDrawing(3);
	        t.addVertex(minX, minY, minZ);
	        t.addVertex(maxX, minY, minZ);
	        t.addVertex(maxX, minY, maxZ);
	        t.addVertex(minX, minY, maxZ);
	        t.addVertex(minX, minY, minZ);
	        t.draw();
	        t.startDrawing(3);
	        t.addVertex(minX, maxY, minZ);
	        t.addVertex(maxX, maxY, minZ);
	        t.addVertex(maxX, maxY, maxZ);
	        t.addVertex(minX, maxY, maxZ);
	        t.addVertex(minX, maxY, minZ);
	        t.draw();
	        t.startDrawing(1);
	        t.addVertex(minX, minY, minZ);
	        t.addVertex(minX, maxY, minZ);
	        t.addVertex(maxX, minY, minZ);
	        t.addVertex(maxX, maxY, minZ);
	        t.addVertex(maxX, minY, maxZ);
	        t.addVertex(maxX, maxY, maxZ);
	        t.addVertex(minX, minY, maxZ);
	        t.addVertex(minX, maxY, maxZ);
	        t.draw();
    	}
    	
    	if((desc & 255) == 1)
    	{
	        t.startDrawing(3);
	        t.addVertex(minX, minY, minZ);
	        t.addVertex(maxX, maxY, maxZ);
	        t.draw();
	        
	        t.startDrawing(3);
	        t.addVertex(maxX, minY, minZ);
	        t.addVertex(minX, maxY, maxZ);
	        t.draw();
	        
	        t.startDrawing(3);
	        t.addVertex(minX, minY, maxZ);
	        t.addVertex(maxX, maxY, minZ);
	        t.draw();
	        
	        t.startDrawing(3);
	        t.addVertex(maxX, minY, maxZ);
	        t.addVertex(minX, maxY, minZ);
	        t.draw();
    	}
    	
    	
    	
    	GL11.glEnable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(2929);
    	GL11.glEnable(GL11.GL_FOG);
    	GL11.glDepthMask(true);
    	GL11.glDisable(3042);
    }
    
    // 0 is box, 1 is star, 2 is diagonal line
    public void drawMarker(Location loc, int desc)
    {
    	
    	GL11.glBlendFunc(770, 771);
    	GL11.glEnable(3042);
    	GL11.glLineWidth(1.0F);
    	    	
    	GL11.glColor3d(((desc >> 24) & 255) / 255.0D, ((desc >> 16) & 255) / 255.0D, ((desc >> 8) & 255) / 255.0D);

    	GL11.glDisable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_LINE_SMOOTH);
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glDisable(GL11.GL_FOG);
    	GL11.glDisable(2929);
    	GL11.glDepthMask(false);
    	
    	double minX = loc.x - TileEntityRendererDispatcher.staticPlayerX;
    	double minZ = loc.z - TileEntityRendererDispatcher.staticPlayerZ;
    	double minY = loc.y - TileEntityRendererDispatcher.staticPlayerY;
    	
    	double maxX = minX + 1.0D;
    	double maxZ = minZ + 1.0D;
    	double maxY = minY + 1.0D;
    	
    	Tessellator t = Tessellator.instance;
    	
    	if((desc & 255) == 0)
    	{
	        t.startDrawing(3);
	        t.addVertex(minX, minY, minZ);
	        t.addVertex(maxX, minY, minZ);
	        t.addVertex(maxX, minY, maxZ);
	        t.addVertex(minX, minY, maxZ);
	        t.addVertex(minX, minY, minZ);
	        t.draw();
	        t.startDrawing(3);
	        t.addVertex(minX, maxY, minZ);
	        t.addVertex(maxX, maxY, minZ);
	        t.addVertex(maxX, maxY, maxZ);
	        t.addVertex(minX, maxY, maxZ);
	        t.addVertex(minX, maxY, minZ);
	        t.draw();
	        t.startDrawing(1);
	        t.addVertex(minX, minY, minZ);
	        t.addVertex(minX, maxY, minZ);
	        t.addVertex(maxX, minY, minZ);
	        t.addVertex(maxX, maxY, minZ);
	        t.addVertex(maxX, minY, maxZ);
	        t.addVertex(maxX, maxY, maxZ);
	        t.addVertex(minX, minY, maxZ);
	        t.addVertex(minX, maxY, maxZ);
	        t.draw();
    	}
    	
    	if((desc & 255) == 1)
    	{
	        t.startDrawing(3);
	        t.addVertex(minX, minY, minZ);
	        t.addVertex(maxX, maxY, maxZ);
	        t.draw();
	        
	        t.startDrawing(3);
	        t.addVertex(maxX, minY, minZ);
	        t.addVertex(minX, maxY, maxZ);
	        t.draw();
	        
	        t.startDrawing(3);
	        t.addVertex(minX, minY, maxZ);
	        t.addVertex(maxX, maxY, minZ);
	        t.draw();
	        
	        t.startDrawing(3);
	        t.addVertex(maxX, minY, maxZ);
	        t.addVertex(minX, maxY, minZ);
	        t.draw();
    	}
    	
    	if((desc & 255) == 2)
    	{
        	GL11.glColor3d(1.0D, 0D, 0D);

	        t.startDrawing(3);
	        t.addVertex(minX + -1.0D, minY - 1.0D, minZ - 1.0D);
	        t.addVertex(maxX + 1.0D, maxY + 1.0D, maxZ + 1.0D);
	        t.draw();
	        
	        GL11.glColor3d(0.0D, 1.0D, 0D);
	        
	        t.startDrawing(3);
	        t.addVertex(maxX + 1.0D, minY - 1.0D, minZ - 1.0D);
	        t.addVertex(minX - 1.0D, maxY + 1.0D, maxZ + 1.0D);
	        t.draw();
	        
	        GL11.glColor3d(0.0D, 0.0D, 1.0D);

	        
	        t.startDrawing(3);
	        t.addVertex(minX - 1.0D, minY - 1.0D, maxZ + 1.0D);
	        t.addVertex(maxX + 1.0D, maxY + 1.0D, minZ - 1.0D);
	        t.draw();
	        
	        GL11.glColor3d(1.0D, 0.0D, 1.0D);

	        t.startDrawing(3);
	        t.addVertex(maxX + 1.0D, minY - 1.0D, maxZ + 1.0D);
	        t.addVertex(minX - 1.0D, maxY + 1.0D, minZ - 1.0D);
	        t.draw();
    	}
    	
    	GL11.glEnable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(2929);
    	GL11.glEnable(GL11.GL_FOG);
    	GL11.glDepthMask(true);
    	GL11.glDisable(3042);
    }
	
	
    public int parseDescriptor(int r, int g, int b, int type)
    {
  	   return ( r << 24) | ( g << 16) | (b << 8) | type;
    }
    
    public int parseRGB(int r, int g, int b)
    {
 	   return ( r << 16) | ( g << 8) | b;
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
    public void processArguments(String name, String argv[])
    {
    	int id, metadata, block;
    	id = 0;
    	metadata = 0;
    	block = 0;
    	String[] tmp;    	
    	
    	if(!name.equals("mode") && !name.equals("list") && !name.equals("chunk") && !name.equals("invertchunk"))
    	{
	    	argv[0].split(":");
	    	tmp = argv[0].split(":");
	    	id = Integer.parseInt(tmp[0]);
	    	metadata = tmp.length > 1 ? Integer.parseInt(tmp[1]) : 0;	
	    	block = this.parseBlock(id, metadata);
    	}
    	
    	boolean flag = false;
    	
    	if(name.equals("new"))
    	{
  
    		if(this.blockDescriptors.containsKey(block))
    		{
    			this.vapid.errorMessage("The block " + argv[0] + " has already been added");
    			return;
    		}
    		
    		this.blockDescriptors.put(block, this.parseDescriptor(255, 255, 255, 1));
    		
    	    vapid.confirmMessage("Added new block " + argv[0] + " with default settings");
    		mc.renderGlobal.loadRenderers();

    	} 
    	else if (name.equals("del"))
    	{
    		if(!this.blockDescriptors.containsKey(block))
    		{
    			this.vapid.errorMessage("The block " + argv[0] + " does not exist, so you cannot delete it");
    			return;
    		}
  		
    	    this.blockDescriptors.remove(block);

    	    vapid.confirmMessage("Removed block " + argv[0]);
    	}
    	else if(name.equals("type"))
    	{
    		
    		if(!this.blockDescriptors.containsKey(block))
    		{
    			this.vapid.errorMessage("The block " + argv[0] + " does not exist");
    			return;
    		}
    		
    		int desc = this.blockDescriptors.get(block);  		
    		this.blockDescriptors.put(block, (desc & ~(desc & 255)) | Integer.parseInt(argv[1]));
    		
    		vapid.confirmMessage("Changed marker type of block " + argv[0] + " to " + argv[1]);

    	}
    	else if(name.equals("color"))
    	{
    		if(!this.blockDescriptors.containsKey(block))
    		{
    			this.vapid.errorMessage("The block " + argv[0] + " has not been added yet");
    			return;
    		}
    		
    		int type = this.blockDescriptors.get(block) & 255;
    		
    		this.blockDescriptors.put(block, this.parseDescriptor(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Integer.parseInt(argv[3]), type));	      	
    		vapid.confirmMessage("Changed color for block " + argv[0]);
    	}
    	else if(name.equals("add"))
    	{
    		if(this.blockDescriptors.containsKey(block))
    		{
    			this.vapid.errorMessage("The block " + argv[0] + " has already been added");
    			return;
    		}
    		
    		this.blockDescriptors.put(block, this.parseDescriptor(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Integer.parseInt(argv[3]), Integer.parseInt(argv[4])));	
    	
    		vapid.confirmMessage("Added marker for block " + argv[0]);
    		mc.renderGlobal.loadRenderers();

    	} 
    	else if(name.equals("mode"))
    	{
    		File f = new File("markers_" + argv[0] + ".vpd");
    		
    		if(!f.exists())
    		{
    			vapid.confirmMessage("New mode created");
    		}
    		else
    		{
    			this.populateMarkerData("markers_" + argv[0] + ".vpd");
    		}
    		
			this.populateMarkerData("markers_" + argv[0] + ".vpd");
			mc.renderGlobal.loadRenderers();
    		this.mode = argv[0];
    		
    		flag = true;
    	}
    	else if(name.equals("chunk"))
    	{
    		this.chunk = !this.chunk;
    	}
    	else if(name.equals("list"))
    	{
    		for(int key : this.blockDescriptors.keySet())
    		{
    			int desc = this.blockDescriptors.get(key);
    			
    			this.vapid.notificationMessage(Integer.toString(this.getBlockId(key)) + ":" + Integer.toString(this.getBlockMetadata(key))
    					+ " " + (new ItemStack(Item.getItemById(this.getBlockId(key)), 1, this.getBlockMetadata(key))).getDisplayName()
    					+ " rgb(" + Integer.toString((desc >> 24) & 255)
    					+ ", " + Integer.toString((desc >> 16) & 255)
    					+ ", " + Integer.toString((desc >> 8) & 255)
    					+ ") Type: " + Integer.toString(desc & 255), false);
    		}
    		
    		flag = true;
    	}
    	
    	if(!flag)
    		this.saveMarkerData("markers_" + this.mode + ".vpd");
    }
    
    public void populateWithDefaults()
    {
		blockDescriptors.put(this.parseBlock(54, 0), this.parseDescriptor(255, 0, 0, 0));
		this.saveMarkerData("markers_default.vpd");
    }
    
	public void populateMarkerData(String filename) 
	{
		
	    if (!this.blockDescriptors.isEmpty()) 
	    {
		      this.blockDescriptors.clear();
		}
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {
	    	if(filename.equals("mode_default.vpd"))
	    		this.populateWithDefaults();
			else
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new FileReader(filename));
	     
	     String line;
	     while ((line = br.readLine()) != null)
	     {
	        String[] data = line.split(" ");
	        this.blockDescriptors.put(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
	     }
	      
	     	br.close();
	     	
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
	}
	
	public void saveMarkerData(String filename)
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
		
				for(Iterator<Integer> i = blockDescriptors.keySet().iterator(); i.hasNext();) {
					int block = i.next();
					
					int desc = blockDescriptors.get(block);
					
					bw.write(Integer.toString(block) + " " + Integer.toString(desc) + "\n");
					
				}
		
		        bw.close();
		        
		     
	    }
	    catch (IOException localIOException)
	    {
	    	localIOException.printStackTrace();
	    }
			  
	}
	
	@Override
	public String getMetadata()
	{
		return (this.mode.equals("default") ? "": "(" + this.mode + ")") + (this.chunk ? " [Quartz Chunks]" : "");
	}
}
