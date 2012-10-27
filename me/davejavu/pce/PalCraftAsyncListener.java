package me.davejavu.pce;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import me.davejavu.pce.command.vanish;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class PalCraftAsyncListener implements Listener {
	//Listener for Bukkit newer than 1.2.5
	//Note, the OldChatListener is identical, however just using PlayerChatEvent,
	//This allows use of the plugin on Tekkit servers for example.
	
	Plugin plugin;
	public PalCraftAsyncListener(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	//MySQL information
	public static String host = PalCommand.getConfig().getFC().getString("mysql.host");
	public static String port = PalCommand.getConfig().getFC().getString("mysql.port");
	public static String database = PalCommand.getConfig().getFC().getString("mysql.database");
	public static String username = PalCommand.getConfig().getFC().getString("mysql.username");
	public static String password = PalCommand.getConfig().getFC().getString("mysql.password");
	public static String date = PalCraftEssentials.date;
	
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent evt) {
		Player player = evt.getPlayer();
		String name = player.getDisplayName();
		String message = evt.getMessage();
		
		//A troll thing.
		if (PalCraftListener.moo.contains(player.getName().toLowerCase())) {
			name = "Cow";
			message = "moo";
			player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.COW);
		}
		
		//If the player's vanish chat block is enabled, it will cancel the event.
		if (!vanish.canChat(player)) {
			evt.setCancelled(true);
		}
		
		//Just some random troll stuff I did a while ago.
		if (!evt.getMessage().contains("grief")) {
			boolean ig = false;
			for (String pl : PalCraftListener.gag) {
				if (player.getName().equalsIgnoreCase(Bukkit.getServer().getPlayer(pl).getName())) {
					ig = true;
				}
			}
			
			if (ig) {
				ResultSet r = Methods.getRows(Methods.con, "troll");
				List<String> se = new ArrayList<String>();
				try{
					while (r.next()) {
						se.add(r.getString("message"));
					}
				} catch (Exception e) {
					
				}
				Random rand = new Random();
				int s = rand.nextInt(se.size());
				if (!player.getName().equalsIgnoreCase("sunstars")) {
					String nMsg = se.get(s);
					nMsg = nMsg.replaceAll("&[(p-pP-P)]", PalCraftListener.getRandomPlayer());
					nMsg = nMsg.replaceAll("&[(s-sS-S)]", player.getDisplayName());
					nMsg = nMsg.replaceAll("&[(q-qQ-Q)]", PalCraftListener.getRandomPlayer());
					nMsg = nMsg.replaceAll("&[(r-rR-R)]", PalCraftListener.getRandomPlayer());
					message = nMsg;
				}
			}
		}
		
		//Mute - if they're muted they cannot talk.
		CustomConfig conf = PalCommand.getConfig(player);
		FileConfiguration fc = conf.getFC();
		if (fc.getBoolean("mute.boolean")) {
			String d = fc.getString("mute.time");
			if (d.equalsIgnoreCase("forever")) {
				evt.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Muted!");
			} else {
				long diff = Long.parseLong(d) - System.currentTimeMillis();
				if(diff <= 0){
					fc.set("mute.boolean", false);
					conf.save();
				} else {
					player.sendMessage(ChatColor.RED + "Muted for "+diff/1000/60+" more minutes!");
					evt.setCancelled(true);
				}
			}
			
		}
		//Replaces colour codes with colours if they have permission, example: &1 = blue etc
		if (PalCommand.permissionCheck((CommandSender)player,"PalCraftEssentials.chat.colours")) {
			message = message.replaceAll("&([0-9a-rA-R])", "§$1");
		}
		//Enables the use of GM prefixes/suffixes
		if (Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
			GroupManager gm = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
			if (gm != null) {
				AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
				if (handler == null) {
					PalCraftEssentials.log.log(Level.SEVERE, "Handler == null!");
				} else {
					String gmPrefix = handler.getUserPrefix(player.getName()).replaceAll("&([0-9a-rA-R])", "§$1");
					String gmSuffix = handler.getUserSuffix(player.getName()).replaceAll("&([0-9a-rA-R])", "§$1");
					String fm = "%s%s%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s";
					evt.setFormat(String.format(fm, gmPrefix, name, gmSuffix, ChatColor.WHITE + message));
				}
			} else {
				PalCraftEssentials.log.log(Level.SEVERE, "GM == null!");
			}
		}
	}
}
