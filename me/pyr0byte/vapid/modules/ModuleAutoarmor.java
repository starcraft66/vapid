package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ModuleAutoarmor extends ModuleBase 
{
	
	public ModuleAutoarmor(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		aliases.add("aa");
		
		this.needsTick = true;
		this.command = new Command(this.vapid, this, aliases, "Equips armor if an armor slot is available and armor is found. Checks the 27 main inventory slots from top left to bottom right. Tells you when a piece is replaced.");
	}

    
	@Override
	public void onTick()
	{
		if(this.isEnabled)
		{
			
	    	// 3 is head, 2 is chest, 1 is leg, 0 is boot
	    	
	    	ItemStack inv[];
	    	ItemStack armor[] = mc.thePlayer.inventory.armorInventory;
	    	
	    	int i, k;
	    	
	    	for(i = 0; i < armor.length; i++) {
	    		
	    		if(armor[i] == null) {
	    			
	    			inv = mc.thePlayer.inventory.mainInventory;
	    			
	    			for(k = 0; k < inv.length; k++) {
	    				
	    				// Only do the main 27 slots.
	    				if(k < 9)
	    					continue;
	    				
	    				if(inv[k] != null && inv[k].getItem() instanceof ItemArmor) {
	    					
	    					int armorType = ((ItemArmor)inv[k].getItem()).armorType;
	    					// Fuck Notch
	    					if(    ( armorType == 0 && i == 3 )
	    						|| ( armorType == 1 && i == 2 )
	    						|| ( armorType == 2 && i == 1 )
	    						|| ( armorType == 3 && i == 0 ) ) {
	    						
	    						this.vapid.notificationMessage("REPLACED " + this.getArmorName(armorType));
	    		    			mc.playerController.windowClick(0, k, 0, 1, mc.thePlayer);
	    		    			break;
	    		    			
	    					}
	    					
	    				}
	    				
	    			}
	    			
	    			
	    		}
	    		
	    	}
	    	
		}
	}
	
	public String getArmorName(int armorType)
	{
		if(armorType == 0)
			return "HELMET";
		else if(armorType == 1)
			return "CHESTPLATE";
		else if(armorType == 2)
			return "LEGGINGS";
		else if(armorType == 3)
			return "BOOTS";
		
		return "UNKNOWN";
	}
}
