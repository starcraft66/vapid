package me.pyr0byte.vapid.events;


public class PlayerLogOffEvent {

	String username;
	
	public PlayerLogOffEvent(String username)
	{
		this.username = username;
	}
	
	public String getUsername()
	{
		return this.username;
	}
}
