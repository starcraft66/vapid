package me.pyr0byte.vapid;

import net.minecraft.util.AxisAlignedBB;

public class Marker {

	Color color;
	AxisAlignedBB aabb;
	
	// 0 box, 1 star
	int type;
	
	public Marker(AxisAlignedBB aabb, Color color, int type)
	{
		this.aabb = aabb;
		this.color = color;
		this.type = type;
	}
	
	public Color getColor()
	{
		return this.color;
	}

	public AxisAlignedBB getAABB()
	{
		return this.aabb;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	
}
