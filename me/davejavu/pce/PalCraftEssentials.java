package me.davejavu.pce;

import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
	
	int taskId;
	
	//MySQL info
	public static String host = PalCommand.getConfig().getFC().getString("mysql.host");
	public static String port = PalCommand.getConfig().getFC().getString("mysql.port");
	public static String database = PalCommand.getConfig().getFC().getString("mysql.database");
	public static String username = PalCommand.getConfig().getFC().getString("mysql.username");
	public static String password = PalCommand.getConfig().getFC().getString("mysql.password");
	
	//HashMaps for storing temporary data
	public static HashMap<String, String> reply = new HashMap<String, String>();
	public static HashMap<String, String> tpa = new HashMap<String, String>();
	
	//Logger
	public static Logger log = Logger.getLogger("Minecraft");
	
	
	public void onEnable() {
		
		
		
		//Taking the time at the start of startup - enables us to
		//see how long overall startup took by taking the start time
		//away from the end time.
		long firstTime = System.currentTimeMillis();
		log.info("[PalCraftEssentials] Starting startup...");
		plugin = this;
		PalCraftListener.commands = getCommands();
		for (String cmd : PalCraftListener.commands) {
			try{
				//Sets command executor for each command specifically.
				getCommand(cmd.toLowerCase()).setExecutor((CommandExecutor) Class.forName("me.davejavu.pce.command."+cmd.toLowerCase()).newInstance());
			} catch (Exception e) {
				log.log(Level.SEVERE, "Could not set command executor for: " + cmd);
				e.printStackTrace();
			}
			
		}
		//Backup.setup(this);
		//Sets the chat listener depending on the version of bukkit being used.
		getServer().getPluginManager().registerEvents(new PalCraftAsyncListener(this), this);
		getServer().getPluginManager().registerEvents(new PalCraftListener(this), this);
		//In case of reload add all online players to the online list
		for (Player p : getServer().getOnlinePlayers()) {
			PalCraftListener.online.add(p.getName());
		}
		log.info("[PalCraftEssentials] Version 2 enabled!");
		getConfig().options().copyDefaults(true);
		saveConfig();
		//If the MySQL information is not entered, the plugin shuts itself down.
		if (getConfig().getString("mysql.host").equalsIgnoreCase("enter host")) {
			log.log(Level.SEVERE, "MySQL info has not been inputted! Shutting down...");
			getServer().getPluginManager().disablePlugin(this);
		}
		new MySQL(host, port, database, username, password);
		try{
			createTables();
			log.info("[PalCraftEssentials] Tables created in database!");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[PalCraftEssentials] Tables could not be created, posting error...");
			e.printStackTrace();
		}
		//taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

		//	@Override
		//	public void run() {
		//		backup.backupDats();
		//	}
			
		//}, (60 * 20) * 30L, (60 * 20) * 30L);
		log.info("[PalCraftEssentials] Startup finished! Took " + (System.currentTimeMillis() - firstTime) + " ms");
	}
	
	public void onDisable() {
		//getServer().getScheduler().cancelTask(taskId);
		try {
			//Close connection.
			MySQL.con.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Error closing the connection! Error: " + e.getCause());
		}
		log.info("[PalCraftEssentials] Version 2 disabled!");
		//Reload then save config to firstly check for any manual changes that have
		//been made then save it to the disk.
		reloadConfig();
		saveConfig();
	}
	
	
	//Long -> time format, 1 day 2 hours 6 minutes 26 seconds
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
	//Set a warp with the name specified, at the location specified
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
	//Returns the location of the warp with the name specified.
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
	//Lists all warps.
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
	//Deletes a file.
	public static String deleteFile(String path, String name) {
		String ret = "none";
		File file = new File(path + File.separator + name);
		boolean done = file.delete();
		if (!done) {
			ret = "File could not be deleted";
		}
		return ret;
	}
	public YamlConfiguration loadResourceFromJar(String name) {
		InputStream is = getResource("config.yml");
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(is);
		return yc;
	}
	public void createTables() throws SQLException {
		ResultSet res = MySQL.con.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
		boolean needToCreateTables = true;
		while (res.next()) {
			if (res.getString("TABLE_NAME").equalsIgnoreCase("perma_bans")) {
				needToCreateTables = false;
			}
		}
		String[] tables = {"perma_bans;id INT, player TEXT, reason TEXT, staff TEXT, date TEXT","ip_bans;id INT, ip TEXT, staff TEXT","temp_bans;id INT, player TEXT, length TEXT, staff TEXT, reason TEXT, `when` BIGINT, `date` TEXT","player_info;player TEXT, warnings TEXT, kicks TEXT, bans TEXT"};
		if (needToCreateTables) {
			for (String s : tables) {
				String tName = s.split(";")[0];
				String values = s.split(";")[1];
				MySQL.createTable(MySQL.con, tName, values);
			}
		} else {
			log.info("[PalCraftEssentials] Tables already created");
		}
	}
}


