package me.pyr0byte.vapid.modules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.HttpGet;
import me.pyr0byte.vapid.Vapid;
import me.pyr0byte.vapid.annotations.EventHandler;
import me.pyr0byte.vapid.events.PacketReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;


public class ModuleSounder extends ModuleBase 
{
	
	String filename;
	ExecutorService pool;
	
	public ModuleSounder(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.vapid, this, aliases, "Logs data from sound packets and sends it to pyr0byte's server");
		filename = "sounder.vpd";
		pool = Executors.newFixedThreadPool(8);
	}
	
	@EventHandler
	public void onPacketReceived(PacketReceivedEvent e)
	{
		
		Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("M-d-y hh;mm;ss");
        String stamp = "(" + ft.format(dNow) + ") ";
        
         EntityPlayer player = mc.thePlayer;
         Packet packet = e.getPacket();
         if (packet instanceof S2CPacketSpawnGlobalEntity) {
             S2CPacketSpawnGlobalEntity globalSpawnPacket = (S2CPacketSpawnGlobalEntity) packet;
             if (globalSpawnPacket.func_149053_g() == 1) {
                 double x = globalSpawnPacket.func_149051_d() / 32D;
                 double y = globalSpawnPacket.func_149050_e() / 32D;
                 double z = globalSpawnPacket.func_149049_f() / 32D;
                 if (player.getDistance(x, y, z) > 160D) {
                	 String str = "Detected lighting strike out of range. [x=" + x + ", y=" + y + ", z=" + z + "]";
                     vapid.italicMessage(str);
                     vapid.appendToFile(filename, stamp + str + "\n\r");
                     this.sendToServer((int)x, (int)z, "sound");
                     
                 }
             }
         } else if(packet instanceof S28PacketEffect) {
             S28PacketEffect effect = (S28PacketEffect) packet;
             int id = effect.func_149242_d();
             int x = effect.func_149240_f();
             int y = effect.func_149243_g();
             int z = effect.func_149239_h();

             if(player.getDistance(x, y, z) > 160D) {
                 String str = "Detected effect. " + "[x=" + x + ", y=" + y + ", z=" + z + "]";
                 vapid.italicMessage(str);
                 vapid.appendToFile(filename, stamp + str + "\n");
                 this.sendToServer((int)x, (int)z, "effect");

             }
         } else if(packet instanceof S29PacketSoundEffect) {
             S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;
             String name = soundEffect.func_149212_c();
             double x = soundEffect.func_149207_d();
             double y = soundEffect.func_149211_e();
             double z = soundEffect.func_149210_f();

             if(player.getDistance(x, y, z) > 160D) {
                 String str = "Detected sound effect out of range. [x=" + x + ", y=" + y + ", z=" + z + ", name=" + name + "]";
                 vapid.italicMessage(str);
                 vapid.appendToFile(filename, stamp + str + "\n");
                 this.sendToServer((int)x, (int)z, name);
             }
         }	
	}
	
	public void sendToServer(int x, int z, String type) {
		
		String username = mc.thePlayer.getCommandSenderName();
		String server = mc.thePlayer.sendQueue.getNetworkManager().getSocketAddress().toString();

		String urlString = "http://2b2t.pyrobyte.net/sound.php?username=" + username + "&server=" + server + "&x=" + Integer.toString(x) + "&z=" + Integer.toString(z) + "&type=" + type;
		//pool.submit(new HttpGet(urlString));
	}
}
