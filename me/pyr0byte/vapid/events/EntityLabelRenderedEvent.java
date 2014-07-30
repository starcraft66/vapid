package me.pyr0byte.vapid.events;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class EntityLabelRenderedEvent {

	public EntityLivingBase entity;
	public double interpolationX, interpolationY, interpolationZ;
	public RenderManager rm;
	
	public EntityLabelRenderedEvent(EntityLivingBase e, double interpolationX, double interpolationY, double interpolationZ, RenderManager rm)
	{
		this.entity = e;
		this.interpolationX = interpolationX;
		this.interpolationY = interpolationY;
		this.interpolationZ = interpolationZ;
		this.rm = rm;
	}
}
