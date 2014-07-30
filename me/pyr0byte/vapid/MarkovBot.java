package me.pyr0byte.vapid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;

import org.jibble.jmegahal.JMegaHal;

public class MarkovBot {

	
	private Minecraft mc;
	private JMegaHal hal;
	public boolean isLoaded;
	
	public MarkovBot(Minecraft mc) {
		
		this.mc 	= mc;
		hal 		= new JMegaHal();
		isLoaded 	= false;
				
	}
	
	public void addString(String str) {
		
		String message;
		
		message = str.replaceAll("(§.)|(\\n+)|(\\r+)", "");
		//message = message.replaceAll("\\d+", "666");
		
		for(char c : message.toCharArray()) {
		
			if(!ChatAllowedCharacters.isAllowedCharacter(c)) {
				message = message.replaceAll(String.valueOf(c), "");
			}
			
		}

		hal.add(message);
	}
	
	public int count(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public void addStringsFromFile(String file) {
		
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line, sender, message;
			double total_lines = (double)this.count(file);
			StaticVapid.vapid.confirmMessage("Loading " + total_lines + " lines from " + file);
			double lines = 0;
			
			while((line = in.readLine()) != null) {
				
				lines++;
				
				if(lines % 10000 == 0)
					StaticVapid.vapid.confirmMessage(Double.toString(Math.round((lines / total_lines) * 100.0D)) + "% loaded");
				
				
				if(!line.contains(">"))
					continue;
				
 				int i = line.indexOf("<");
				int j = line.indexOf(">");
				
				//sender 	= line.substring(i - 1, j - 1);
				message = line.substring(j + 1);
				
				//if(!sender.equals(mc.thePlayer.username))
					this.addString(message);
			}
			
			StaticVapid.vapid.confirmMessage("Loaded " + lines + " lines from " + file);
			this.isLoaded = true;
			in.close();
			
		} catch (FileNotFoundException e) {
			StaticVapid.vapid.errorMessage("Could not find chat log.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getSentence() {
		
		String ret = hal.getSentence();
		return ret.length() > 100 ? ret.substring(0, 100) : ret;
	}
	
	public String getSentence(String str) {
		
		String ret = hal.getSentence(str);
		return ret.length() > 100 ? ret.substring(0, 100) : ret;
	}
}
