package me.pyr0byte.vapid.events;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerLeaveVisualRangeEvent {

	EntityPlayer player;
	
	public PlayerLeaveVisualRangeEvent(EntityPlayer player)
	{
		this.player = player;
	}
	
	public EntityPlayer getPlayer()
	{
		return this.player;
	}
}
