package me.davejavu.pce.command;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.MySQL;
import me.davejavu.pce.PalCommand;

public class pce extends PalCommand {
	
	List<String> permaBans = new ArrayList<String>();
	List<String> tempBans = new ArrayList<String>();
	List<String> ipBans = new ArrayList<String>();
	List<String> info = new ArrayList<String>();
	
	int taskId = 0;
	
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pce")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.pce")) {
				if (args.length > 0) {
					switch(args[0]) {
					case "backup":
						if (args.length > 1) {
							switch(args[1]) {
							case "bans":
								permaBans = new ArrayList<String>();
								tempBans = new ArrayList<String>();
								ipBans = new ArrayList<String>();
								info = new ArrayList<String>();
								sendMessage(sender, ChatColor.GOLD + "Backing up all bans...");
								backupBans();
								sendMessage(sender, ChatColor.GREEN + "Successfully backed up!");
								return true;
							default:
								sendMessage(sender, ChatColor.RED + "Usage: /pce backup <bans>");
								return true;
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /pce backup <bans>");
							return true;
						}
					case "restore":
						if (args.length > 1) {
							switch(args[1]) {
							case "bans":
								if (args.length > 2) {
									if (args[2].equalsIgnoreCase("cancel")) {
										if (taskId != 0) {
											Bukkit.getScheduler().cancelTask(taskId);
											taskId = 0;
											sendMessage(sender, ChatColor.GOLD + "Restore cancelled");
											return true;
										} else {
											sendMessage(sender, ChatColor.RED + "Task is not running!");
											return true;
										}
									} else {
										sendMessage(sender, ChatColor.RED + "Usage: /restore bans [cancel]");
										return true;
									}
								}
								sendMessage(sender, ChatColor.RED + "WARNING! This will remove all current bans and restore the bans on file.");
								sendMessage(sender, ChatColor.RED + "You have 15 seconds to abort doing this, by typing /restore bans cancel");
								taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("PalCraftEssentials"), new Runnable() {
									@Override
									public void run() {
										sendMessage(sender, ChatColor.GOLD + "Restoring bans...");
										restoreBans();
										sendMessage(sender, ChatColor.GREEN + "Bans successfully restored!");
										taskId = 0;
									}
								}, 15 * 20L);
								return true;
							default:
								sendMessage(sender, ChatColor.RED + "Usage: /pce restore <bans> [cancel]");
								return true;
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /pce restore <bans>");
							return true;
						}
					default:
						sendMessage(sender, ChatColor.RED + "Usage: /pce <backup/restore>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /pce <backup/restore>");
					return true;
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	
	public void restoreBans() {
		CustomConfig conf = new CustomConfig("./","bansBackup.yml");
		FileConfiguration fc = conf.getFC();
		List<String> bans = fc.getStringList("permaBans");
		MySQL.executeCommand("TRUNCATE TABLE perma_bans");
		for (String b : bans) {
			String[] bI = b.split("::,");
			MySQL.insertInfo(MySQL.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", bI[0] + ",'" + bI[1] + "','" + bI[2] + "','" + bI[3] + "','" + bI[4] + "'");
		}
		List<String> tempBans = fc.getStringList("tempBans");
		MySQL.executeCommand("TRUNCATE TABLE temp_bans");
		for (String b : tempBans) {
			String[] bI = b.split("::,");
			MySQL.insertInfo(MySQL.con, "temp_bans", "`id`,`player`,`length`,`staff`,`reason`,`when`,`date`", bI[0] + ",'" + bI[1] + "','" + bI[2] + "','" + bI[3] + "','" + bI[4] + "','" + bI[5] + "','" + bI[6] + "'");
		}
		List<String> ipBans = fc.getStringList("ipBans");
		MySQL.executeCommand("TRUNCATE TABLE ip_bans");
		for (String b : ipBans) {
			String[] bI = b.split("::,");
			MySQL.insertInfo(MySQL.con, "ip_bans", "`id`,`ip`,`staff`", bI[0] + ",'" + bI[1] + "','" + bI[2] + "'");
		}
		List<String> playerInfo = fc.getStringList("playerInfo");
		MySQL.executeCommand("TRUNCATE TABLE player_info");
		for (String p : playerInfo) {
			String[] pI = p.split("::,");
			MySQL.insertInfo(MySQL.con, "player_info", "`player`,`warnings`,`bans`,`kicks`", "'" + pI[0] + "','" + pI[1] +"','" + pI[2] + "','" + pI[3] +"'");
		}
	}
	
	
	public void backupBans() {
		ResultSet rpb = MySQL.getAllRows(MySQL.con, "perma_bans");
		try{
			while (rpb.next()) {
				permaBans.add(rpb.getInt("id") + "::," + rpb.getString("player") + "::," + rpb.getString("reason") + "::," + rpb.getString("staff") + "::," + rpb.getString("date"));
			}
		} catch (Exception e){}
		ResultSet rtb = MySQL.getAllRows(MySQL.con, "temp_bans");
		try{
			while (rtb.next()) {
				tempBans.add(rtb.getInt("id") + "::," + rtb.getString("player") + "::," + rtb.getString("length") + "::," +rtb.getString("staff") + "::," + rtb.getString("reason") + "::," + rtb.getLong("when") + "::," + rtb.getString("date"));
			}
		} catch (Exception e){}
		ResultSet rib = MySQL.getAllRows(MySQL.con, "ip_bans");
		try{
			while (rib.next()) {
				ipBans.add(rib.getInt("id") + "::," + rib.getString("ip") + "::," + rib.getString("staff"));
			}
		} catch (Exception e){}
		ResultSet rpi = MySQL.executeCommand("SELECT * FROM player_info");
		try{
			while (rpi.next()) {
				info.add(rpi.getString("player") + "::," + rpi.getString("warnings") + "::," + rpi.getString("kicks"));
			}
		} catch (Exception e){}
		CustomConfig conf = new CustomConfig("./", "bansBackup.yml");
		FileConfiguration fc = conf.getFC();
		fc.set("permaBans", permaBans);
		fc.set("tempBans", tempBans);
		fc.set("ipBans", ipBans);
		fc.set("playerInfo", info);
		conf.save(fc);
	}
}
