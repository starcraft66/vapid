package me.pyr0byte.vapid.events;

import me.pyr0byte.vapid.Location;
import me.pyr0byte.vapid.SimpleBlock;

public class ChunkUnloadedEvent {

	public int x, z;
	public Location loc;
	
	public ChunkUnloadedEvent(int x, int z)
	{
		this.x = x;
		this.z = z;
		this.loc = new Location((double)x, 0, (double)z);
	}
	
}
