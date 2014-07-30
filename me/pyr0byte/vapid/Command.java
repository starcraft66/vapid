package me.pyr0byte.vapid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.pyr0byte.vapid.modules.ModuleBase;

public class Command {

	public ArrayList<String> aliases;
	Vapid vapid;
	ModuleBase module;
	public Map<String, Argument> args;
	String desc;
	
	public Command(Vapid vapid, ModuleBase module, ArrayList<String> aliases) 
	{
		this.aliases = aliases;
		this.vapid = vapid;
		this.module = module;
		this.args = new HashMap<String, Argument>();
		this.desc = "No description available";
	}
	
	public Command(Vapid vapid, ModuleBase module, ArrayList<String> aliases, String desc) 
	{
		this.aliases = aliases;
		this.vapid = vapid;
		this.module = module;
		this.args = new HashMap<String, Argument>();
		this.desc = desc;
	}
	
	public void registerArg(String arg, Class sets[])
	{
		
		args.put(arg, new Argument(this, arg, sets));
		
	}
	
	public void registerArg(String arg, Class sets[], String usage)
	{
		
		args.put(arg, new Argument(this, arg, sets, usage));
		
	}
	
	public boolean parseArgs(String argv[]) 
	{
		Argument arg;
		String subarray[];
		boolean foundFlag = false;
		
		if(args.size() == 0 && argv.length > 0)
		{
			vapid.errorMessage("This command has no arguments (clich§ sad face)");
			return false;
		}
		
		for(int i = 0; i < argv.length; i++) {
			
			if(args.containsKey(argv[i])) {
				foundFlag = true;
				arg = (Argument)args.get(argv[i]);
				
				if(arg.getArgc() > argv.length - (i + 1)) {
					vapid.errorMessage("Not enough arguments for flag: " + argv[i] + "; calls for " + arg.getArgc() + " arguments");
					return false;
				}
				
				subarray = Arrays.copyOfRange(argv, i + 1, i + 1 + arg.getArgc());
				
				if(!arg.matches(subarray)) {
					return false;
				}
				
				this.module.processArguments(argv[i], subarray);
				i += arg.getArgc();
				
			} else if(argv[i].equals("set")) {
				if(argv.length > i) {
					foundFlag = true;
					// TODO: Set field in module to this value if it matches the type.
					
				}
			}
			
		}
		
		if(!foundFlag && argv.length > 0) 
		{
			// If no flag was found, use the default flag. This is absolutely bloat.
			
			if(this.module.defaultArg.equals(""))
			{
				vapid.errorMessage("Invalid argument");
				return false;
			}
			
			arg = (Argument)args.get(this.module.defaultArg);
			
			if(arg.getArgc() > argv.length) 
			{
				vapid.errorMessage("Not enough arguments for flag; calls for " + arg.getArgc() + " arguments");
				return false;
			}
			
			subarray = Arrays.copyOfRange(argv, 0, arg.getArgc());
			
			if(!arg.matches(subarray)) 
			{
				return false;
			}
			
			this.module.processArguments(this.module.defaultArg, subarray);
			
		} else if(!foundFlag) {
			if(this.module.isToggleable)
				this.module.toggleState();
			else {
				vapid.errorMessage("You have to provide arguments for this command");
				return false;
			}
		}
		
		return true;
	}
	
	public String getDescription()
	{
		return this.desc;
		
	}
}
