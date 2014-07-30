package me.pyr0byte.vapid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import net.minecraft.util.AxisAlignedBB;

public class Util {

	public static boolean arrayContains(Object[] stack, Object needle) 
	{
		
		return Arrays.asList(stack).contains(needle);
	}
	
	public static String capitalize(String str) 
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static String formatArmorDurability(double percent)
	{
    	percent = Math.ceil(percent);
    	boolean red = percent < 10.0D;
    	String color = red ? "§c" : "";
    	if(percent == 100)
    		return color + Integer.toString((int)percent) + "§r";
    	
    	if(percent >= 10)
    		return color + "0" + Integer.toString((int)percent) + "§r";
    	
    	return color + "00" + Integer.toString((int)percent) + "§r";	    
	}
	
    public static void writeFile(String data, String name) 
    {
  	  
    	File file = new File(name);
	    try 
	    {
	    	
	        BufferedWriter out = new BufferedWriter(new FileWriter(file));
	        out.write(data);
	        out.close();
	        
	    } catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }
	    
  }
  
   public static String readFile(String name) 
   {
	
		BufferedReader in = null;
		
		String data = null;
		String ret = null;
		
		  	File file = new File(name);
		  	
		    try 
		    {
		        in = new BufferedReader(new FileReader(file));
		        
		        while((data = in.readLine()) != null) 
		        {
		        	ret += data;
		        }
		        
		        
		        in.close();
		        
		        return ret;
	
		    } catch (IOException e) 
		    {
				try {
				if (in != null)in.close();
			} catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		    }
		    
		    return null;	    
   }
   
   public static AxisAlignedBB locationToAABB(Location loc)
   {
	   return 	AxisAlignedBB.getBoundingBox(
		    	loc.x, // minX
		    	loc.y, // minY
		    	loc.z, // minZ
		    	loc.x + 1.0, // maxX
		    	loc.y + 1.0, // maxY
		    	loc.z + 1.0); // maxZ
   }

}
