package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ModuleBlock extends ModuleBase 
{
	
	int tick = 0;
	
	public ModuleBlock(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "you're always blocking");
		
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled && tick % 2 == 0) 
		{
			if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
				
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
			
			}
		}
		
		tick++;
		
	}

}


