package me.pyr0byte.vapid;

public class SimpleBlock {

	/*
	 * For holding very basic block information for the marker system
	 */
	public int id;
	public int metadata;
	
	public SimpleBlock(int id, int metadata)
	{
		this.id = id;
		this.metadata = metadata;
	}
	
	
	public SimpleBlock(int id)
	{
		this.id = id;
		this.metadata = 0;
	}
}
