package me.davejavu.pce;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PalCraftEssentials extends JavaPlugin implements Listener {
	
	//Date stuff
	static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	static Date dates = new Date();
	public static String date = dateFormat.format(dates);
	
	//(this)
	static Plugin plugin;
	
	
	//MySQL info
	public static String host = PalCommand.getConfig().getFC().getString("mysql.host");
	public static String port = PalCommand.getConfig().getFC().getString("mysql.port");
	public static String database = PalCommand.getConfig().getFC().getString("mysql.database");
	public static String username = PalCommand.getConfig().getFC().getString("mysql.username");
	public static String password = PalCommand.getConfig().getFC().getString("mysql.password");
	
	//HashMaps for stuff.
	public static HashMap<String, String> reply = new HashMap<String, String>();
	public static HashMap<String, String> tpa = new HashMap<String, String>();
	
	//Logger
	public static Logger log = Logger.getLogger("Minecraft");
	
	
	public void onEnable() {
		long firstTime = System.currentTimeMillis();
		log.info("[PalCraftEssentials] Starting startup...");
		plugin = this;
		PalCraftListener.commands = getCommands();
		for (String cmd : PalCraftListener.commands) {
			try{
				getCommand(cmd.toLowerCase()).setExecutor((CommandExecutor) Class.forName("me.davejavu.palcraftessentials.command."+cmd.toLowerCase()).newInstance());

			} catch (Exception e) {
				log.log(Level.SEVERE, "Could not set command executor for: " + cmd);
				e.printStackTrace();
			}
			
		}
		for (String str : new String[] {"/", "superpickaxe"}) {
			if (Bukkit.getPluginCommand(str) != null)
				try {
					Bukkit.getPluginCommand(str).setExecutor((CommandExecutor) Class.forName("me.davejavu.palcraftessentials.command.superpick").newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		}
		if (Bukkit.getBukkitVersion().startsWith("1.2.5")) {
			getServer().getPluginManager().registerEvents(new PalCraftOldChatListener(this), this);
		} else {
			getServer().getPluginManager().registerEvents(new PalCraftAsyncListener(this), this);
		}
		log.info("[PCE] BUKKIT VERSION: " + Bukkit.getBukkitVersion() + " VERSION: " + Bukkit.getVersion());
		getServer().getPluginManager().registerEvents(new PalCraftListener(this), this);
		for (Player p : getServer().getOnlinePlayers()) {
			PalCraftListener.online.add(p.getName());
		}
		log.info("[PalCraftEssentials] Version 2 enabled!");
		getConfig().options().copyDefaults(true);
		saveConfig();
		host = getConfig().getString("mysql.host");
		port = getConfig().getString("mysql.port");
		database = getConfig().getString("mysql.database");
		username = getConfig().getString("mysql.username");
		password = getConfig().getString("mysql.password");
		if (getConfig().getString("mysql.host").equalsIgnoreCase("enter host")) {
			log.log(Level.SEVERE, "MySQL info has not been inputted! Shutting down...");
			getServer().getPluginManager().disablePlugin(this);
		}
		Methods.con = Methods.mysqlConnect(host, port, database, username, password);
		log.info("[PalCraftEssentials] Startup finished! Took " + (System.currentTimeMillis() - firstTime) + " ms");
	}
	
	public void onDisable() {
		log.info("[PalCraftEssentials] Version 2 disabled!");
		reloadConfig();
		saveConfig();
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(message);
		} else {
			log.info(message);
		}
	}
	
	public static boolean permissionCheck(CommandSender sender, String permission) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			return p.hasPermission(permission);
		} else {
			return true;
		}
	}
	
	//Long -> String
	public static String parseLongToString(long millis) {
		String rt = "";
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
		long mins = TimeUnit.MILLISECONDS.toMinutes(millis) - (TimeUnit.DAYS.toMinutes(days) + TimeUnit.HOURS.toMinutes(hours));
		long secs = TimeUnit.MILLISECONDS.toSeconds(millis) - (TimeUnit.DAYS.toSeconds(days) + TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(mins));
		rt = String.format("%02d days, %02d hours, %02d minutes, %02d seconds", days, hours, mins, secs);
		return rt;
	}
	
	//Returns all commands
	public String[] getCommands() {
		String[] result = new String[getDescription().getCommands().keySet().size()];
		for (int i = 0; i < result.length; i++) {
			String cmd = (String) getDescription().getCommands().keySet().toArray()[i];
			
			result[i] = cmd;
		}
		/*String[] rs = new String[1];
		rs[0] = "ban";
		log.info(rs[0]);*/
		return result;
	}
	
	//Warp stuff
	public static String setWarp(String name, Location loc) {
		CustomConfig customConfig = new CustomConfig("./plugins/PalCraftEssentials/warps", name);
		FileConfiguration warpConfig = customConfig.getFC();
		if (!warpConfig.contains("world")) {
			warpConfig.set("world", loc.getWorld().getName());
			warpConfig.set("x", loc.getX());
			warpConfig.set("y", loc.getY());
			warpConfig.set("z", loc.getZ());
			warpConfig.set("pitch", String.valueOf(loc.getPitch()));
			warpConfig.set("yaw", String.valueOf(loc.getYaw()));
			warpConfig.set("name", name);
			customConfig.save();
		} else {
			return "Warp already exists!";
		}
		return "noerror";
	}
	
	public static Location getWarp(String name) {
		Location loc;
		String folder = "./plugins/PalCraftEssentials/warps";
		CustomConfig wC = new CustomConfig(folder, name + ".yml");
		FileConfiguration warpConfig = wC.getFC();
		if (warpConfig.contains("world")) {
			String world = warpConfig.getString("world");
			double x = warpConfig.getDouble("x");
			double y = warpConfig.getDouble("y");
			double z = warpConfig.getDouble("z");
			float pitch = Float.parseFloat(warpConfig.getString("pitch"));
			float yaw = Float.parseFloat(warpConfig.getString("yaw"));
			loc = new Location(Bukkit.getWorld(world), x, y, z);
			loc.setPitch(pitch);
			loc.setYaw(yaw);
			return loc;
		} else {
			return null;
		}
	}
	
	public static List<String> listWarps() {
		File warpFolder = new File("./plugins/PalCraftEssentials/warps");
		List<String> ret = new ArrayList<String>();
		String[] tempS = new String[warpFolder.list().length];
		for (int i = 0; i < tempS.length; i++) {
			tempS[i] = warpFolder.list()[i];
		}
		Arrays.sort(tempS);
		for (String tempWarpString : tempS) {
			String warpString = tempWarpString.split(".yml")[0];
			ret.add(warpString);
		}
		return ret;
	}
	
	public static String deleteFile(String path, String name) {
		String ret = "none";
		File file = new File(path + File.separator + name);
		boolean done = file.delete();
		if (!done) {
			ret = "File could not be deleted";
		}
		return ret;
	}
}