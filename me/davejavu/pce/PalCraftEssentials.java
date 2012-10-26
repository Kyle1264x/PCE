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
	
	static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	static Date dates = new Date();
	public static String date = dateFormat.format(dates);
	
	static Plugin plugin;
	
	public static String host = "";
	public static String port = "";
	public static String database = "";
	public static String username = "";
	public static String password = "";
	
	public static HashMap<String, String> reply = new HashMap<String, String>();
	public static HashMap<String, String> tpa = new HashMap<String, String>();
	
	public static Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		PalCraftEssentials.plugin = this;
		PalCraftListener.cmds = new String[] {"banlist","kill","timings","defaultgamemode","kick","ban","ban-ip","pardon","pardon-ip","kill","list","tp","give","say","me","tell","help","?","time","stop","op","deop","save-all","save-off","save-on","whitelist","version","reload"};
		PalCraftListener.commands = getCommands();
		for (String cmd : PalCraftListener.commands) {
			try{
				//Constructor<?> con = Class.forName("me.davejavu.palcraftessentials.command."+cmd.toLowerCase()).getConstructor();
				//con.setAccessible(true);
				getCommand(cmd.toLowerCase()).setExecutor((CommandExecutor) Class.forName("me.davejavu.palcraftessentials.command."+cmd.toLowerCase()).newInstance());
				//getCommand(cmd.toLowerCase()).setExecutor((CommandExecutor) con.newInstance());
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
	
	public static String parseLongToString(long millis) {
		String rt = "";
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
		long mins = TimeUnit.MILLISECONDS.toMinutes(millis) - (TimeUnit.DAYS.toMinutes(days) + TimeUnit.HOURS.toMinutes(hours));
		long secs = TimeUnit.MILLISECONDS.toSeconds(millis) - (TimeUnit.DAYS.toSeconds(days) + TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(mins));
		rt = String.format("%02d days, %02d hours, %02d minutes, %02d seconds", days, hours, mins, secs);
		return rt;
	}
	
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
	
	/*public static Location getWarp(String name) {
		Location ret;
		CustomConfig wC = new CustomConfig("./plugins/PalCraftEssentials/warps", name);
		if (!wC.getFC().contains("world")) {
			ret = new Location(Bukkit.getServer().getWorld(wC.getFC().getString("world")), wC.getFC().getDouble("x"), wC.getFC().getDouble("y"), wC.getFC().getDouble("z"));
			ret.setPitch(Float.parseFloat(wC.getFC().getString("pitch")));
			ret.setYaw(Float.parseFloat(wC.getFC().getString("yaw")));
			return ret;
		} else {
			return null;
		}
	}*/
	
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
	/*public static String[] listWarps() {
		File file = new File("./plugins/PalCraftEssentials/warps");
		String[] ret = new String[file.list().length];
		int a = 0;
		Bukkit.broadcastMessage("Start for 1");
		for (String x : file.list()) {
			Bukkit.broadcastMessage(x);
			String[] xS = x.split(".");
			String wN = "";
			int th = 0;
			Bukkit.broadcastMessage("Start for 2");
			for (String xT : xS) {
				Bukkit.broadcastMessage("xT " + xT);
				if (th != (xS.length - 1))
					wN = wN + xT;
			}
			Bukkit.broadcastMessage("End for 2");
			ret[a] = x;
			Bukkit.broadcastMessage(wN + " " + a);
			a++;
		}
		Bukkit.broadcastMessage("End for 1");
		return ret;
	}*/
	
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
	
	/*public static FileConfiguration getCustomConfig(String path, String name) {
    	if (path.endsWith("/")) {
    		path.charAt(path.toCharArray().length - 1);
    		String[] pt = path.split("/");
    		StringBuilder np = null;
    		for (int i = 0; i < pt.length; i++) {
    			if (np == null) {
    				np = new StringBuilder();
    				np.append(pt[i]);
    			} else {
    				np.append("/" + pt[i]);
    			}
    			
    		}
    		path = np.toString();
    	}
    	if (!path.startsWith(".")) {
    		path = "." + path;
    	}
    	if (!name.endsWith(".yml")) {
    		name = name + ".yml";
    	}
		File file = new File(path + File.separator + name);
    	if (!file.exists()) {
    		log.info(path + File.separator + name + " doesnt exist");
    		file.mkdirs();
    		try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
				oos.writeObject(file);
				oos.close();
			} catch (Exception e) {
			}
    	} else {
    		log.info(path + File.separator + name + " exists");
    	}
		return reloadCustomConfig(path, name);
    }

    public static FileConfiguration reloadCustomConfig(String path, String name) {
        if (new File(path + File.separator + name) == null){
        	return YamlConfiguration.loadConfiguration(new File(path + File.separator + name));
        }
        return YamlConfiguration.loadConfiguration(new File(path + File.separator + name));
        
    }

    public static void saveCustomConfig(File file, FileConfiguration fileConfig) {
    	try {
    		if (fileConfig == null){
    			reloadCustomConfig(file.getPath(), file.getName());
    		}
    		fileConfig.save(file);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	if (fileConfig == null || file == null) {
    		return;
    	}
    	try {
    		fileConfig.save(file);
    	} catch (IOException ex) {
    		Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + file, ex);
    	}

    }*/
	/*static FileConfiguration customConfig = null;
	static File customConfigFile = null;
	public static FileConfiguration reloadCustomConfig(String path, String name) {
		if (path.endsWith("/")) {
    		path.charAt(path.toCharArray().length - 1);
    		String[] pt = path.split("/");
    		StringBuilder np = null;
    		for (int i = 0; i < pt.length; i++) {
    			if (np == null) {
    				np = new StringBuilder();
    				np.append(pt[i]);
    			} else {
    				np.append("/" + pt[i]);
    			}
    			
    		}
    		path = np.toString();
    	}
    	if (!path.startsWith(".")) {
    		path = "." + path;
    	}
    	if (!name.endsWith(".yml")) {
    		name = name + ".yml";
    	}
		File customConfigFile = null;
		if (customConfigFile == null) {
			customConfigFile = new File(path, name);
		}
		return YamlConfiguration.loadConfiguration(customConfigFile);
	}

	public static void saveCustomConfig(File customConfigFile, FileConfiguration customConfig) {
		if (customConfig == null || customConfigFile == null) {
			return;
		}
		try {
			getCustomConfig(customConfigFile.getPath(), customConfigFile.getName()).save(customConfigFile);
		} catch (IOException ex) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
		}
	}

	public static FileConfiguration getCustomConfig(String path, String name) {
		customConfig = null;
		if (path.endsWith("/")) {
    		path.charAt(path.toCharArray().length - 1);
    		String[] pt = path.split("/");
    		StringBuilder np = null;
    		for (int i = 0; i < pt.length; i++) {
    			if (np == null) {
    				np = new StringBuilder();
    				np.append(pt[i]);
    			} else {
    				np.append("/" + pt[i]);
    			}
    			
    		}
    		path = np.toString();
    	}
    	if (!path.startsWith(".")) {
    		path = "." + path;
    	}
    	if (!name.endsWith(".yml")) {
    		name = name + ".yml";
    	}
		
		if (customConfig == null) {
			customConfig = reloadCustomConfig(path, name);
		}
		return customConfig;
	}*/
}