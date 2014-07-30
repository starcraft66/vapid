package me.pyr0byte.vapid.modules;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.pyr0byte.vapid.Base64;
import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.ChatReceivedEvent;
import me.pyr0byte.vapid.events.ChatSentEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;


public class ModuleEncryption extends ModuleBase 
{

	String delimeter;
	boolean partyMode;
	
	public HashMap a = new HashMap();
	private byte[] d = new byte[] { 11, 90, 27, 71, -45, 126, -61, -7, -56, 105, 106, -56, -52, 2, 114, 3 };
	private byte[] e = new byte[] { 120, -95, -6, -62, -120, -1, -61, -70, 36, -128, 61, 34, 39, -78, 56, 94 };
	private int f = 32;
	private SecretKeySpec g = new SecretKeySpec(d, "AES");
	private IvParameterSpec h = new IvParameterSpec(e);
	public Queue b = new LinkedList();
	public int c = 0;

	  
	public ModuleEncryption(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		
		this.name = "Encryption";
		this.needsTick = true;
		this.isToggleable = true;
		
		this.command = new Command(this.vapid, this, aliases, "encrypts chats");
		this.command.registerArg("delimeter", new Class[] { String.class }, "what to start a chat with to encrypt it, ex. %");

		this.defaultArg = "delimeter";
		this.delimeter = "%";
		
	}
	
	public String hash(String str)
	{		
		long hash = 2166136261L;
		for(char c : str.toCharArray())
		{
			hash *= 16777619L;
			hash ^= c;
		}
		
		return Long.toHexString(hash).substring(8);
	}
	
	  public String DecryptData(byte[] paramArrayOfByte)
	  {
	    ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
	    int i = paramArrayOfByte.length;
	    byte[] arrayOfByte1 = new byte[i];
	    localByteBuffer.get(arrayOfByte1);
	    Cipher localCipher = null;
	    
		try {
			localCipher = Cipher.getInstance("AES/CFB/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			localCipher.init(2, g, h);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] arrayOfByte2 = null;
		try {
			arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    byte[] arrayOfByte3 = new byte[arrayOfByte2.length - 16];
	    
	    for (int j = 0; j < arrayOfByte3.length; j++)
	    	arrayOfByte3[j] = arrayOfByte2[(j + 16)];
	    
	    return new String(arrayOfByte3);
	  }

	  public byte[] EncryptData(String paramString)
	  {
	    byte[] arrayOfByte1 = paramString.getBytes();
	    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 16];
	    new Random().nextBytes(arrayOfByte2);
	    
	    for (int i = 0; i < arrayOfByte1.length; i++)
	    	arrayOfByte2[(i + 16)] = arrayOfByte1[i];
	    
	    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
	    Cipher localCipher = null;
	    
		try {
			localCipher = Cipher.getInstance("AES/CFB/NoPadding");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
			localCipher.init(1, g, h);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			localByteArrayOutputStream.write(localCipher.doFinal(arrayOfByte2));
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    byte[] arrayOfByte3 = localByteArrayOutputStream.toByteArray();
	    try {
			localByteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return arrayOfByte3;
	  }

	  public byte[] DecodeBase64(String paramString)
	  {
	    return Base64.decodeToBytes(paramString);
	  }

	  public String EncodeBase64(byte[] paramArrayOfByte)
	  {
	    return Base64.encodeToString(paramArrayOfByte);
	  }

	  
	@Override
	public void onTick()
	{
	    c += 1;
	    if (c > 60)
	    {
	      c = 0;
	      String str = (String)b.poll();
	      if (str != null)
	    	  
	    	 mc.thePlayer.sendChatMessage(str);
	        
	    }
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("delimeter"))
		{
			this.delimeter = argv[0];
			this.vapid.confirmMessage("Delimeter changed to: " + argv[0]);
		}
		else if(name.equals("party"))
		{
			this.partyMode = !this.partyMode;
		}

	}

	@EventHandler
	public boolean onChatSent(ChatSentEvent e)
	{
		
		String message = e.getMessage();

		if (!(message.startsWith(this.delimeter)))
		      return true;
		   
		    String str1 = mc.thePlayer.getCommandSenderName();
		    System.out.print(str1);
		    
		    String str2 = this.hash(str1);
		        
		    try
		    {
		      int j = b.size() == 0 ? 1 : 0;
		      String str3 = EncodeBase64(EncryptData(message.substring(1)));
		      
		      str3 = str3.replace("\r", "");
		      str3 = str3.replace("\n", "");
		      String str4 = str2 + str3 + "\\\\";
		      if (str4.length() > 99 - str2.length() - 2)
		      {
		        StringBuilder localStringBuilder = new StringBuilder();
		        for (int k = 0; k < str3.length(); k++)
		        {
		          char c1 = str3.charAt(k);
		          localStringBuilder.append(c1);
		          
		          if ((ChatAllowedCharacters.isAllowedCharacter(c1)) && (localStringBuilder.length() <= 99 - str2.length() - 2))
		            continue;
		          
		          String str7 = str2 + localStringBuilder.toString();
		          b.add(str7);
		          localStringBuilder = new StringBuilder();
		        }
		        
		        localStringBuilder.append("\\\\");
		        String str5 = str2 + localStringBuilder.toString();
		        b.add(str5);
		        if (j == 0)
		          return true;
		        String str6 = (String)b.poll();

		        mc.thePlayer.sendChatMessage(str6);
		        return false;
		      }
		      if (j == 0)
		      {
		        b.add(str4);
		        return true;
		      }
		        mc.thePlayer.sendChatMessage(str4);

		      return false;
		    }
		    catch (Exception localException)
		    {
		    }
	        return true;
	}
	
	@EventHandler
	public boolean onChatReceived(ChatReceivedEvent e)
	{
		String username;
		String message;
		String paramString3;
		
		message = e.getMessage();
		username = message.split(">")[0];
		username = username.substring(1);
		message = message.split(">")[1].substring(1);
				
		String str1 = (String)a.get(username);
	    if ((str1 == null) || (str1.length() == 0))
	      str1 = "";
	    
	    String str2 = this.hash(username);

	        
	    if (!message.startsWith(str2))
	      return true;
	        
	    message = message.substring(str2.length());
	    str1 = str1 + message;
	       

	    if (str1.endsWith("\\\\"))
	    {
	      int j = 0;
	      int k = str1.length() - 2;
	      str1 = str1.substring(j, k);

	      try
	      {
	        String str3 = DecryptData(DecodeBase64(str1));
	        str3 = "§l" + username + ": §r" + str3 + "§r";
	        a.put(username, "");
	        
	        vapid.message(str3);
	        
	        return false;
	      }
	      catch (Exception localException)
	      {
	        a.put(username, "");
	        return !(str1.length() <= 99);
	      }
	    }
		    a.put(username, str1);
		    return false;	
	    

	}
}
