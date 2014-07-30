package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;


public class ModuleTeleport extends ModuleBase 
{	
	public ModuleTeleport(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		aliases.add("tp");
		
		this.command = new Command(this.vapid, this, aliases, "Teleports you to a coordinate set");
		this.command.registerArg("do", new Class[] { Integer.class, Integer.class, Integer.class }, "Coordinates x, y, z respectively");
		this.defaultArg = "do";
	}

	@Override
	public void onEnable() {}
	
	@Override
	public void onDisable() {}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("do"))
		{
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Integer.parseInt(argv[0]), Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), 0, true));

		}
	}
}
