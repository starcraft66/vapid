package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S2BPacketChangeGameState;


public class ModuleSpawn extends ModuleBase 
{	
	public ModuleSpawn(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
				
		this.command = new Command(this.vapid, this, aliases, "Teleports you to spawn. You will be kicked from the server.");
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
            public void run()
            {
                    try
                    {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0, 0, Double.NaN, 0, true));
                            
                } catch (Exception e) { 
                	
                	e.printStackTrace();
                	
                }
            }
            
        }).start();
	}
}
