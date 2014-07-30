package me.pyr0byte.vapid.events;


public class ChatReceivedEvent {

	String message;
	
	public ChatReceivedEvent(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}
