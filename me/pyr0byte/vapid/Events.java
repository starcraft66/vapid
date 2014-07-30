package me.pyr0byte.vapid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.pyr0byte.vapid.modules.ModuleBase;
import net.minecraft.client.Minecraft;

public class Events {

	Minecraft mc;
	Vapid vapid;
	Map<Class, ArrayList<MethodHolder>> handlers;
	
	public Events(Minecraft mc, Vapid vapid) {
		this.mc = mc;
		this.vapid = vapid;
		
		handlers = new HashMap<Class, ArrayList<MethodHolder>>();
		this.getEventHandlers();
	}
	
	public void getEventHandlers()
	{
		
		ArrayList<MethodHolder> tmp;

		for(ModuleBase module : vapid.modules)
		{
						
			for(Method m : module.getClass().getDeclaredMethods())
			{
				if(m.isAnnotationPresent(me.pyr0byte.vapid.annotations.EventHandler.class))
				{
					Class<?>[] params = m.getParameterTypes();
					
					if(params.length == 1)
					{
						
						if(params[0].getPackage().getName().endsWith("events"))
						{
							if(this.handlers.containsKey(params[0]))
							{
								this.handlers.get(params[0]).add(new MethodHolder(module, m));
							}
							else 
							{
								tmp = new ArrayList<MethodHolder>();
								tmp.add(new MethodHolder(module, m));
								this.handlers.put(params[0], tmp);
								
							}
							
						}
					}
				}
			}
		}
		
	}
	
	public boolean onEvent(Object o)
	{
		
		boolean isCancelled = false;

		try {
			
			Class type = o.getClass();
			Object res = null;
			
			if(this.handlers.get(type) == null) 
			{
				return false;
			}
			
			for(MethodHolder mh : this.handlers.get(type))
			{
				res = mh.getMethod().invoke(mh.getObject(), type.cast(o));
				
				if(res != null)
					if(!((Boolean)res).booleanValue())
						isCancelled = true;
			}
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isCancelled;
	}
	
	public boolean onCommand(String command)
	{
		if(command.length() < 1)
			return false;
		
		List<String> argv = new ArrayList<String>();
		Matcher ma = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
		while (ma.find())
		    argv.add(ma.group(1).replace("\"", ""));


		
		for(ModuleBase m : vapid.modules) {
			if(m.aliases.contains(argv.get(0))) {
				m.command.parseArgs(argv.subList(1, argv.size()).toArray(new String[argv.size() - 1]));
				return true;
			}
		}
		
		return false;
	}
	public void hookRunTick()
	{
		this.vapid.onTick();
	}
	
	public void hookBotTick() {
	
		this.vapid.onBotTick();
		
	}
	
	public void hookGuiIngame()
	{
		//this.vapid.vg.update();
	}
	
	public void hookEntityRenderer()
	{
		this.vapid.onRendererTick();
	}
	
	public void onSendChat(String str)
	{
				
	}
	
	public void onPlayerLogOut(String str)
	{
		
	}
	
	public void onPlayerLogIn(String str)
	{
		
	}
	
	public void onPlayerEnterVisualRange(String str)
	{
		
	}
	
	public void onPlayerLeaveVisualRange(String str)
	{
		
	}
	
}

