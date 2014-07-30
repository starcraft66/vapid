package me.pyr0byte.vapid.events;

import me.pyr0byte.vapid.Location;

public class BlockRemovedEvent {

	int id;
	Location location;
	
	public BlockRemovedEvent(int id, Location location)
	{
		this.id = id;
		this.location = location;
	}
	
	public int getBlockId()
	{
		return this.id;
	}
	
	public Location getBlockLocation()
	{
		return this.location;
	}
}
