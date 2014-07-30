package me.pyr0byte.vapid;

public interface IModule {

	public void onEnable();
	public void onDisable();
	public void toggleState();
	
	public void onTick();
	public void processArguments(String name, String argv[]);
	
	public String getName();
	public String getMetadata();
}
