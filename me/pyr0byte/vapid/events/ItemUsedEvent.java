package me.pyr0byte.vapid.events;

import net.minecraft.item.ItemStack;

public class ItemUsedEvent {

	public int slot;
	public ItemStack item;
	
	public ItemUsedEvent(int slot, ItemStack item)
	{
		this.slot = slot;
		this.item = item;
	}
	
}
