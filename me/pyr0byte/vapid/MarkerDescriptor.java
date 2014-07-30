package me.pyr0byte.vapid;

public class MarkerDescriptor {

	/*
	 * Holds very simple data about a marker; color and type
	 */
	public Color color;
	
	// 0 is box, 1 is star
	public int type;
	
	public MarkerDescriptor(Color color, int type)
	{
		this.color = color;
		this.type = type;
	}
}
