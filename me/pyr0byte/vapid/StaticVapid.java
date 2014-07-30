package me.pyr0byte.vapid;

import net.minecraft.client.Minecraft;

public class StaticVapid {

	public static Vapid vapid;
	public static Minecraft mc;
	
	public StaticVapid(Vapid vapid, Minecraft mc)
	{
		this.vapid = vapid;
		this.mc = mc;
	}

}
