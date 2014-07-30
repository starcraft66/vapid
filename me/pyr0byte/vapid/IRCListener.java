package me.pyr0byte.vapid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import me.pyr0byte.vapid.modules.ModuleIRC;

public class IRCListener implements Runnable {

	
	String server, nick, channel, login, password;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	Vapid vapid;
	
	public IRCListener(Vapid vapid, String server, String nick, String channel, BufferedWriter writer, BufferedReader reader, String password)
	{
		this.vapid = vapid;
		this.server = server;
		this.nick = nick;
		this.channel = channel;
		this.login = nick;
		this.writer = writer;
		this.reader = reader;
		this.password = password;
	}
	
	void send(String str)
	{
		try 
		{
			writer.write(str + "\r\n");
			writer.flush();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void join(String user)
	{
		try
		{
			
		this.send("NICK " + user);
		this.send("USER " + user + " 8 * : Vapid Client");
	       
		String line = null;
				
		while ((line = reader.readLine()) != null) 
		{
			if (line.indexOf("004") >= 0) 
			{
				vapid.italicMessage("You are now logged in.");
				this.nick = user;
				vapid.getModule(ModuleIRC.class).nick = nick;
				break;
			}
			else if (line.indexOf("433") >= 0) 
			{
				vapid.italicMessage("Username is already in use! Trying with a new name...");
				join(user + "|V");
				return;
			}
		}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		
        
		try {
			   
			vapid.italicMessage("Logging in with password " + password);
			
			String line = null;
			
			join(this.nick);
			
			this.send("JOIN " + channel + " " + password);
			vapid.getModule(ModuleIRC.class).connected = true;
			
			vapid.italicMessage("Joined channel " + channel);

			while((line = reader.readLine()) != null) 
			{
				if(line.indexOf("475") >= 0 && line.contains(":Cannot join channel (+k)"))
				{
					vapid.italicMessage("Never mind, invalid password! Try again.");
					return;
				}
				else
				if (line.startsWith("PING ")) 
				{
					this.send("PONG " + line.substring(5));
				}
				else 
				{
					String message = line;
					System.out.print(line + "\n");
					if(line.split(" ")[1].equals("PRIVMSG"))
					{
						int index = message.indexOf("!");
						String username = line.substring(1, index);
						String recv = line.split(" ")[2];
						String msg = line.split(" ", 4)[3].substring(1);
						if(recv.equals(this.channel))	
							vapid.message("§e§l<" + username + ">§r§e " + msg);
						else
						{
							vapid.message("§e§l" + username + " whispers§r§e: " + msg);
							vapid.getModule(ModuleIRC.class).lastWhisper = username;
						}
					}
					else if(line.split(" ")[1].equals("353"))
					{
						vapid.message("§lOnline:§r " + line.split(":")[2]);
					}
					else if(line.split(" ")[1].equals("332"))
					{
						vapid.message(line.split(":")[2].replaceAll("1", "§"));	
					}
					else if(line.split(" ")[1].equals("432"))
					{
						vapid.errorMessage("Nick reserved for services");

					}
					else if(line.split(" ")[1].equals("433"))
					{
						vapid.errorMessage("Nick already in use");
					}
					else if((line.split(" ")[1]).equals("JOIN"))
					{
						
						String message2 = line;
						int index = message2.indexOf("!");
						String username = line.substring(1, index);
						vapid.message("§e§o" + username + " joined the channel");
					}
					else if((line.split(" ")[1]).equals("PART"))
					{
						String message2 = line;
						int index = message2.indexOf("!");
						String username = line.substring(1, index);
						vapid.message("§e§o" + username + " left the channel");
					}
					else if((line.split(" ")[1]).equals("NICK"))
					{
						
						String message2 = line;
						int index = message2.indexOf("!");
						String username = line.substring(1, index);
						String new_name = line.split(" ", 3)[2].substring(1);
						
						if(username.equals(this.nick))
						{
							this.nick = new_name;
							vapid.getModule(ModuleIRC.class).nick = nick;
							vapid.message("§eYOU are now known as " + new_name);
						}
						else
							vapid.message("§e" + username + " is now known as " + new_name);
					}
					else if((line.split(" ")[1]).equals("NOTICE"))
					{
						vapid.message("*** " + line.split(" ", 4)[3].substring(1));
					}

				}
	
			}
			
		   } catch (Exception e)
		   {
			   e.printStackTrace();
		   }
	}
	

}
