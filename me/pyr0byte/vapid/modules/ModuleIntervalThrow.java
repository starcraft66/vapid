package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class ModuleIntervalThrow extends ModuleBase 
{
	
	public int interval;
	int time;
	long intervalMs = 90000;
	long currentMs, lastMs;
	int changeToSlot;
	
	public ModuleIntervalThrow(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("intt");
		
		this.command = new Command(this.vapid, this, aliases, "Throws the item in your hotbar every nth millisecond.");
		this.command.registerArg("int", new Class[] {Long.class}, "period in milliseconds (ex. 100)");
		this.command.registerArg("slot", new Class[] {Long.class}, "hotbar slot to change to before throwing; -1 means no change");

		this.defaultArg = "int";
		
		this.changeToSlot = -1;

	}
	
	int priorSlot = 0;
	
	@Override
	public void onTick()
	{
		  this.currentMs = System.nanoTime() / 1000000;
			
    	  if(this.isEnabled && (this.currentMs - this.lastMs) >= this.intervalMs)
    	  {
    		  this.currentMs = System.nanoTime() / 1000000;
      		
    		  if(this.changeToSlot != -1) 
    		  {
    			  this.priorSlot = mc.thePlayer.inventory.currentItem;
    			  mc.thePlayer.inventory.currentItem = this.changeToSlot;
    		  }
    		  
    		  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));

    		  this.lastMs = System.nanoTime() / 1000000;
    		  
    		  if(this.changeToSlot != -1)
    		  {
    			  mc.thePlayer.inventory.currentItem = this.priorSlot;
    		  }
    	  }

	}
	
	@Override
	public void processArguments(String name, String[] argv)
	{
		if(name.equals("int"))
		{
			this.intervalMs = Long.parseLong(argv[0]);
		}
		else if(name.equals("slot"))
		{
			this.changeToSlot = Integer.parseInt(argv[0]);
		}
	}
	
	@Override
	public String getMetadata()
	{
		return "(" + Long.toString(this.intervalMs) + "ms)" + (this.changeToSlot != -1 ? (" [" + Integer.toString(this.changeToSlot) + "]" ) : "");
	}

}
