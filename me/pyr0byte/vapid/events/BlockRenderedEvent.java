package me.pyr0byte.vapid.events;

import me.pyr0byte.vapid.Location;

public class BlockRenderedEvent {

	public int id;
	public int metadata;
	public Location loc;
	
	public BlockRenderedEvent(int id, int metadata, int x, int y, int z)
	{
		this.id = id;
		this.metadata = metadata;
		this.loc = new Location(x, y, z);
	}

}
