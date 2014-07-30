package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleUtil extends ModuleBase 
{
	
	int etpx;
	int etpy;
	int etpz;
	
	public ModuleUtil(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		aliases.add("ns");

		this.isToggleable = false;
		
		this.command = new Command(this.vapid, this, aliases, "Pointless commands you'll never use");
		this.command.registerArg("reloadrenderers", new Class[] {}, "Reloads the renderer; good to get rid of some FPS lag");
		this.command.registerArg("suicide", new Class[] {}, "Sez /kill");
		this.command.registerArg("lag", new Class[] {}, "Sez /lag");
		this.command.registerArg("slot", new Class[] {Integer.class, Integer.class}, "Clicks on one slot and then on another");
		this.command.registerArg("cs", new Class[] {}, "Puts held item in 1st crafting slot");

		this.command.registerArg("insult", new Class[] {}, "Sez u suk LOL #rekt >:)");

	}

	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("reloadrenderers"))
		{
			mc.renderGlobal.loadRenderers();
		}
		else if(name.equals("suicide"))
		{
			mc.thePlayer.sendChatMessage("/kill");
		}
		else if(name.equals("lag"))
		{
			mc.thePlayer.sendChatMessage("/lag");
		}
		else if(name.equals("insult"))
		{
			mc.thePlayer.sendChatMessage("u suk LOL #rekt >:)");
		}
		else if(name.equals("slot"))
		{
			mc.playerController.windowClick(0, Integer.parseInt(argv[0]), 0, 0, mc.thePlayer);
			mc.playerController.windowClick(0, Integer.parseInt(argv[1]), 0, 0, mc.thePlayer);

		}
		else if(name.equals("cs"))
		{
			mc.playerController.windowClick(0, 36 + mc.thePlayer.inventory.currentItem, 0, 0, mc.thePlayer);
			mc.playerController.windowClick(0, 1, 0, 0, mc.thePlayer);	
		}
	}
}
