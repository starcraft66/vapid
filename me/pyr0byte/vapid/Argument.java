package me.pyr0byte.vapid;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Argument {

	private String id;
	public Class sets[];
	private Command c;
	private ArrayList<String> errors;
	String usage;
	
	public Argument(Command c, String id, Class sets[])
	{
		this.c = c;
		this.id = id;
		this.sets = sets;
		this.errors = new ArrayList<String>();
		
		this.usage = "of format: §o";
		
		for(Class clazz : sets)
		{
			this.usage += clazz.getSimpleName() + " ";
		}
		
		this.usage += "§r";
	}
	public Argument(Command c, String id, Class sets[], String use)
	{
		this.c = c;
		this.id = id;
		this.sets = sets;
		this.errors = new ArrayList<String>();
		
		this.usage = "of format: §o";
		
		for(Class clazz : sets)
		{
			this.usage += clazz.getSimpleName() + " ";
		}
		
		this.usage += "§r\n" + use;
	}
	
	public boolean matches(String argv[]) 
	{

		this.errors.clear();
		
			if(sets == null || sets.length == 0)
				return true;
			
			Object test;			
			for(int i = 0; i < argv.length; i++) {	 
				
				try {
					
					if(sets[i] == Boolean.class) {
						test = Boolean.parseBoolean(argv[i]);
					} else if(sets[i] == Character.class) {
						test = argv[i].charAt(0);
					} else if(sets[i] == String.class) {
						test = argv[i];
					} else if(sets[i] == Integer.class) {
						test = Integer.parseInt(argv[i]);
					} else if(sets[i] == Float.class) {
						test = Float.parseFloat(argv[i]);
					} else if(sets[i] == Double.class) {
						test = Double.parseDouble(argv[i]);
					}
					
				} catch(ClassCastException e) {
					this.c.vapid.errorMessage("Invalid type on argument " + i + "; should be of type: " + sets[i].getSimpleName());
					return false;
				} catch(NumberFormatException e) {
					this.c.vapid.errorMessage("Invalid type on argument " + i + "; should be of type: " + sets[i].getSimpleName());
					return false;
				}
				
			}

		
		return true;
	}
	
	public String getArgumentType(int i) 
	{
		return sets[i].getClass().getSimpleName();
	}
	
	public String getId() 
	{
		return this.id;
	}
	
	public int getArgc() 
	{
		return sets.length;
	}
	
	public String getUsage()
	{
		return this.usage;
	}
	
}
