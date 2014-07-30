package me.pyr0byte.vapid;

import java.util.HashMap;
import java.util.Map;

/*
 * For global access of boolean values set within the client 
 */

public class VapidBools {

	static Vapid vapid;
	static Map<String, Boolean> bools;
	
	public VapidBools(Vapid v)
	{
		vapid = v;
		bools = new HashMap<String, Boolean>();
	}
	
	public static void addBool(String name)
	{
		bools.put(name, false);
	}
	
	public static void addBool(String name, boolean b)
	{
		bools.put(name, b);
	}
	
	public static void toggle(String name)
	{
		bools.put(name, !bools.get(name));
	}
	
	public static void set(String name, boolean b)
	{
		bools.put(name, b);
	}
	
	public static void get(String name)
	{
		bools.get(name);
	}
}
