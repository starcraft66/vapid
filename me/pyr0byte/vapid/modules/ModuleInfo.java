package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Util;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;


public class ModuleInfo extends ModuleBase 
{	
	public ModuleInfo(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.vapid, this, aliases, "Gives info about a player; currently only their armor and it's enchantments; unless a player is designated, you will get info for the closest player to you");
		this.command.registerArg("player", new Class[] { String.class }, "Player to get info on; not case sensitive");
		this.defaultArg = "player";
	}
	
	@Override
	public void onEnable()
	{
		
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	@Override
	public void toggleState()
	{
		 double closestD = Double.MAX_VALUE;
		 double d = 0.0D;
		 EntityPlayer closest = null;
		 
        for (int i = 0; i < mc.theWorld.playerEntities.size(); ++i)
        {
        	if(((EntityPlayer)mc.theWorld.playerEntities.get(i)).getCommandSenderName().equals(mc.thePlayer.getCommandSenderName()))
        	{
        		continue;
        	}
        	
            d = mc.thePlayer.getDistanceSqToEntity((EntityPlayer)mc.theWorld.playerEntities.get(i));
            
            if(d < closestD)
            {
            	closestD = d;
            	closest = (EntityPlayer)mc.theWorld.playerEntities.get(i);
            }
        }
        
        if(closest != null)
        	this.getInfo(closest.getCommandSenderName());
        else
        	this.vapid.errorMessage("No players found!");
	}
	
	@Override
	public void processArguments(String name, String[] argv)
	{
		if(name.equals("player"))
		{
			this.getInfo(argv[0]);
		}
	}
	
	public EntityPlayer getPlayerByName(String player)
	{
        for (int var2 = 0; var2 < mc.theWorld.playerEntities.size(); ++var2)
        {
            if (player.equalsIgnoreCase(((EntityPlayer)mc.theWorld.playerEntities.get(var2)).getCommandSenderName()))
            {
                return (EntityPlayer)mc.theWorld.playerEntities.get(var2);
            }
        }
        
        return null;
	}
	
	public String getArmorDurability(String player)
	{
		EntityPlayer p = this.getPlayerByName(player);

		if(this.getPlayerByName(player) != null) 
		{
			String durability = "";
			
			ItemStack[] armor = p.inventory.armorInventory.clone();
			ArrayUtils.reverse(armor);
			boolean hasArmor = false;
			
			for(ItemStack i : armor)
			{
				durability += (i != null ? Util.formatArmorDurability( ( ((double)i.getMaxDamage() - (double)i.getItemDamage() ) / (double)i.getMaxDamage()) * 100.0D ): "---") + "/";
		    	
				if(i != null)
					hasArmor = true;
			}
				
			return hasArmor ? durability.substring(0, durability.length() - 1) : "no armor";
		}
		else
		{
			return null;
		}
	}
	
	public void getInfo(String player)
	{
		EntityPlayer p = this.getPlayerByName(player);

		mc.playerController.interactWithEntitySendPacket(mc.thePlayer, p);
		
		if(this.getPlayerByName(player) != null) 
		{
			this.vapid.message("§l" + p.getCommandSenderName() + "\'s Armor");
			String durability = "";
			
			ItemStack[] armor = p.inventory.armorInventory.clone();
			ArrayUtils.reverse(armor);

			for(ItemStack i : armor)
			{
				this.vapid.message(this.vapid.getModule(ModuleESP.class).getItemNameAndEnchantments(i));
				durability += (i != null ? Util.formatArmorDurability( ( ((double)i.getMaxDamage() - (double)i.getItemDamage() ) / (double)i.getMaxDamage()) * 100.0D ) : "---") + "/";
		    	
			}
			
			this.vapid.message("§l" + durability.substring(0, durability.length() - 1));
			
		}
		else
		{
			this.vapid.errorMessage("Player not found!");
		}
	}
}
