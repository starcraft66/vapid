package me.pyr0byte.vapid.events;

import me.pyr0byte.vapid.Location;
import me.pyr0byte.vapid.SimpleBlock;

public class BlockChangedEvent {

	public int newId, newMetadata, oldId, oldMetadata, x, y, z;
	
	public Location location;
	
	public BlockChangedEvent(int newId, int newMetadata, int oldId, int oldMetadata, int x, int y, int z)
	{
		this.newId = newId;
		this.newMetadata = newMetadata;
		this.oldId = oldId;
		this.oldMetadata = oldMetadata;
		this.location = new Location((double)x, (double)y, (double)z);
	}
	
}
