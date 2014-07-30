package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;


public class ModuleDump extends ModuleBase 
{	
	public ModuleDump(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
				
		this.command = new Command(this.vapid, this, aliases, "Removes 4 items in rapid succession");
	}

	@Override
	public void onEnable() {}
	
	@Override
	public void onDisable() {}
	
	@Override
	public void toggleState()
	{
        new Thread(new Runnable()
        {

			@Override
			public void run() {
				for(int i = 0; i < 4; i++)
				{
					mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 0, 1, mc.thePlayer);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
        	
        }).start();
        
	}
}
