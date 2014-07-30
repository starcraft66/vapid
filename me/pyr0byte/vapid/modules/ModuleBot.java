package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;

public class ModuleBot extends ModuleBase 
{
	
	boolean hasKilled;
	
	public ModuleBot(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "A minimal bot for botting things");

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			
			if(mc.thePlayer.isDead) {
				mc.thePlayer.respawnPlayer();
			} else if(!hasKilled) {
				mc.thePlayer.rotationPitch = 90;
				mc.botController.onPlayerDamageBlock(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ, mc.objectMouseOver.sideHit);
				
				if(mc.thePlayer.posY <= 6) {
					mc.thePlayer.sendChatMessage("/kill");
					mc.thePlayer.setDead();
					
					hasKilled = true;
				}
				
			} else if(hasKilled && !mc.thePlayer.isDead) {
				hasKilled = false;
			}
			
		}
		
	}

}
