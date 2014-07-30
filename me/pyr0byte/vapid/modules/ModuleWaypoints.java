package me.pyr0byte.vapid.modules;

import org.lwjgl.opengl.GL11;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Location;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class ModuleWaypoints extends ModuleBase 
{
	
	
	public ModuleWaypoints(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsRendererTick = true;
		this.aliases.add("way");
		this.aliases.add("w");

		this.command = new Command(this.vapid, this, aliases, "TOTALLY NOT WORKING! Draws an icon in the location of coordinate sets");
		
		this.defaultArg = "deg";

	}

	
	@Override
	public void onRendererTick()
	{
		if(this.isEnabled) {
			this.drawMarker(new Location(100D, 64D, 100D), 6553600);
		}
		
	}
	
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
	    	
	        
	        
	        double minX = (loc.x * 1 - RenderManager.renderPosX + 0.5D);
	        double minY = (loc.y - RenderManager.renderPosY + 0.5D);
	        double minZ = (loc.z * 1 - RenderManager.renderPosZ + 0.5D);
	        
	        
	        double distance = Math.round(mc.thePlayer.getDistance(loc.x, loc.y, loc.z));
	        
	        float var14;
	        double dl = Math.sqrt((minX * minX) + (minY * minY) + (minZ * minZ));
	        var14 = (float)(dl * 0.1F + 1.0F) * 0.02666666666666667F;
	        GL11.glScalef(-var14, -var14, var14);

	    	double maxX = minX + 1.0D;
	    	double maxZ = minZ + 1.0D;
	    	double maxY = minY + 1.0D;
	    	
	    	Tessellator t = Tessellator.instance;

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
	    	
	    	GL11.glEnable(GL11.GL_LIGHTING);
	    	GL11.glEnable(GL11.GL_TEXTURE_2D);
	    	GL11.glEnable(2929);
	    	GL11.glEnable(GL11.GL_FOG);
	    	GL11.glDepthMask(true);
	    	GL11.glDisable(3042);
	    }
	 
	@Override
	public void processArguments(String name, String[] argv)
	{

	}
	
}
