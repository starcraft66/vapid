package me.pyr0byte.vapid.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.MathHelper;


public class ModuleAura extends ModuleBase 
{
	List sortedEntities, entities;
	Iterator es;
	EntityClientPlayerMP player;
	Entity e;
	boolean mobs, players, animals;
	
	double reachDistance = 4.2D;
	long intervalMs = (long)(1000 / 8);
	long currentMs, lastMs;
	
	public ModuleAura(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		
		lastMs = -1L;
		sortedEntities = new ArrayList<Entity>();
		entities = new ArrayList<Entity>();
		
		mobs = false;
		animals = false;
		players = true;
		
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "Hits NPCs within a certain range; does not hit friends.");
		this.command.registerArg("target", new Class[] { String.class }, "(players/mobs/animals); toggles | -[pP][mM][aA] set all values at once, lowercase being false (ex. aura -PMa for players and mobs)");
		this.command.registerArg("reach", new Class[] { Double.class }, "will hit NPCs that are less than or equal to this distance from the player");

		this.defaultArg = "target";

		
	}

    
	public boolean isCalmPigman(Entity entity) {
		

		if(entity instanceof EntityPigZombie) {
			
			
			if(((EntityPigZombie)entity).entityToAttack != null)
				return false;
			else
				return true;
						
		} else
			return false;

	}
	
	public boolean isEntityWatched(Entity entity) {
		
		return   (players && entity instanceof EntityOtherPlayerMP)
			     || (mobs && entity instanceof EntityMob && !isCalmPigman(entity))
			     || (animals && entity instanceof EntityAnimal);
	}
	
	public void pruneUnwatchedEntities() {
		
		for(int i = 0; i < this.entities.size(); i++) {
		
			if(!this.isEntityWatched((Entity)entities.get(i)))				
				this.entities.remove(i);
		
		}
		
	}
	
	public int c_size() {
		if(this.sortedEntities == null || this.sortedEntities.isEmpty())
			return 0;
		else
			return this.sortedEntities.size();
	}
	
	public void sortByDistance() {
		
		int initial_size = this.entities.size();
		int least_index = 0;
		double current_least;
		double d;
		Entity e;
		
		while(c_size() < initial_size) {
			
			current_least = Double.MAX_VALUE;

			for(int i = 0; i < this.entities.size(); i++) {
			
				e = (Entity)this.entities.get(i);
				d = this.player.getDistanceSqToEntity(e);
				
				if(d < current_least) {
					current_least = d;
					least_index = i;
				}
				
			}
			
			this.sortedEntities.add(this.entities.get(least_index));
			this.entities.remove(least_index);
		}
	}
	
	@Override
	public void onTick() {
			if(this.isEnabled)
			{
	
						  if(entities.size() > 0)
							  entities.clear();
						  
						  Entity en;
						  
						  for(int i = 0; i < mc.theWorld.getLoadedEntityList().size(); i++) {
							  en = (Entity)mc.theWorld.getLoadedEntityList().get(i);
							  
							  if(this.isEntityWatched(en) && !(en instanceof EntityOtherPlayerMP && vapid.getModule(ModuleFriends.class).isFriend(((EntityOtherPlayerMP)en).getCommandSenderName())))
								  entities.add(en);
						  }
						  
				    	  player = mc.thePlayer;
				    	  
				    	  //this.pruneUnwatchedEntities();
						  this.sortByDistance();
											  
						  es = sortedEntities.iterator();
				  	      
						  this.currentMs = System.nanoTime() / 1000000;
	
				    	  while(es.hasNext() && ((this.currentMs - this.lastMs) >= this.intervalMs)) {
				  			
				    		  this.currentMs = System.nanoTime() / 1000000;
	 
				    		  e = (Entity)es.next();
				    		  
				    		  if(e != null && !e.equals(player) && !e.isDead && this.isEntityWatched(e) && player.getDistanceToEntity(e) <= this.reachDistance && player.canEntityBeSeen(e)) {
				    			  
				    				  this.faceEntity(e, 100F, 100F);
					    			  			    				  
				    				  player.swingItem();
				    				  mc.playerController.attackEntity(player, e);
				    				  
					    			  this.lastMs = System.nanoTime() / 1000000;
	
				    		  	
				    		  }
				    		  
				    	  }
			
				    	  sortedEntities.clear();
	
			}
		}
	
    public void faceEntity(Entity par1Entity, float par2, float par3)
    {
        double var4 = par1Entity.posX - mc.thePlayer.posX;
        double var8 = par1Entity.posZ - mc.thePlayer.posZ;
        double var6;

        if (par1Entity instanceof EntityLiving)
        {
            EntityLiving var10 = (EntityLiving)par1Entity;
            var6 = var10.posY + (double)var10.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        }
        else
        {
            var6 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        }

        double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / Math.PI));
        mc.thePlayer.rotationPitch = this.updateRotation(mc.thePlayer.rotationPitch, var13, par3);
        mc.thePlayer.rotationYaw = this.updateRotation(mc.thePlayer.rotationYaw, var12, par2);
    }
    
    private float updateRotation(float par1, float par2, float par3)
    {
        float var4 = MathHelper.wrapAngleTo180_float(par2 - par1);

        if (var4 > par3)
        {
            var4 = par3;
        }

        if (var4 < -par3)
        {
            var4 = -par3;
        }

        return par1 + var4;
    }
    
    @Override
    public void processArguments(String name, String argv[])
    {
    	if(name.equals("target"))
    	{
    		String arg = argv[0];
    		
    		if(arg.startsWith("-"))
    		{
    			arg = arg.substring(1);
    			
    			int length = arg.length();
    			
    			if(length < 1 || length > 3)
    			{
    				vapid.errorMessage("You must provide 1-3 flags.");
    				return;
    			}
    							
				this.players = false;
				this.animals = false;
				this.mobs = false;
				
    			for(int i = 0; i < length; i++)
    			{
    				char c = arg.charAt(i);
    				boolean wasUpperCase = Character.isUpperCase(c);
    				c = Character.toLowerCase(c);

    				
    				switch(c)
    				{
	    				case 'p':
	    					this.players = wasUpperCase;
	    					break;
	    					
	    				case 'a':
	    					this.animals = wasUpperCase;
	    					break;
	    					
	    				case 'm':
	    					this.mobs = wasUpperCase;
	    					break;
	    					
	    				default:
	    					vapid.errorMessage("Invalid flag #" + Integer.toString(i) + "; ignoring");
	    					break;
    				}
    			}
    			
    		} else if(arg.equals("players") || arg.equals("p")) {
    			this.players = !this.players;
    		} else if(arg.equals("mobs") || arg.equals("m")) {
    			this.mobs = !this.mobs;
    		} else if(arg.equals("animals") || arg.equals("a")) {
     			this.animals = !this.animals;
     		} else {
				vapid.errorMessage("Invalid argument!");
     		}
    	} 
    	else if(name.equals("reach"))
    	{
    		this.reachDistance = Double.parseDouble(argv[0]);
    	}
    
    }
	
    @Override
    public String getMetadata()
    {
    	String ret = "";
    	
    	if(this.players)
    		ret += "Players, ";
    	
       	if(this.mobs)
    		ret += "Mobs, ";
       	
       	if(this.animals)
    		ret += "Animals [PETA], ";
       	
       	if(ret.length() == 0)
       		ret = "NOTHING?! Your problem.";
       	else
       		if(ret.endsWith(", "))
       			ret = ret.substring(0, ret.length() - 2);
       	
    	ret = "(" + ret + ")";

       	if(this.reachDistance != 4.2D)
       		ret += " [" + Double.toString(this.reachDistance) + "]";
       	
       	return ret;
    }
}
