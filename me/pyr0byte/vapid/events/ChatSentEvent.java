package me.pyr0byte.vapid.events;


public class ChatSentEvent {

	String message;
	
	public ChatSentEvent(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}
