package me.davejavu.pce.command;

import java.sql.ResultSet;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.davejavu.pce.MySQL;
import me.davejavu.pce.PalCommand;

public class bupdate extends PalCommand {

	public bupdate(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bupdate")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.bupdate")) {
				if (args.length > 1) {
					String currentType = "";
					String currentReason = "";
					String currentLength = "";
					String staff = "";
					int id = 0;
					ResultSet r = MySQL.getRow(MySQL.con, "perma_bans", "player='" + Bukkit.getOfflinePlayer(args[0]).getName() + "'");
					boolean banned = false;
					try {
						while (r.next()) {
							if (r.getString("player").equalsIgnoreCase(args[0])) {
								banned = true;
								currentType = "perma";
								id = r.getInt("id");
								currentReason = r.getString("reason");
								staff = r.getString("staff");
							}
						}
					} catch (Exception e) {}
					if (!banned) {
						ResultSet rr = MySQL.getRow(MySQL.con, "temp_bans", "player='" + Bukkit.getOfflinePlayer(args[0]).getName() + "'");
						try {
							while (rr.next()) {
								if (rr.getString("player").equalsIgnoreCase(args[0])) {
									banned = true;
									currentType = "temp";
									id = rr.getInt("id");
									currentReason = rr.getString("reason");
									staff = rr.getString("staff");
									currentLength = r.getString("length");
									log.info(currentLength);
								}
							}
						} catch (Exception e) {}
					}
					if (banned) {
						HashMap<String, String> arg = new HashMap<String, String>();
						StringBuilder sb = new StringBuilder();
						String currentString = "";
						boolean rn = false;
						boolean d = false;
						for (int i = 1; i < args.length; i++) {
							if (args[i].charAt(1) == ':') {
								d = false;
								if (rn) {
									arg.put(currentString, sb.toString());
								}
								rn = true;
								sb = new StringBuilder();
								if (args[i].split(":").length == 2) {
									currentString = args[i].split(":")[0];
									sb.append(args[i].split(":")[1] + " ");
								} else {
									d = true;
								}
								continue;
							} else {
								if (!d) {
									sb.append(args[i] + " ");
									if (i == args.length-1) {
										arg.put(currentString, sb.toString());
									}
								}
								
							}
						}
						boolean one = false;
						String newReason = "";
						if (arg.containsKey("r")) {
							newReason = arg.get("r");
							newReason = newReason.substring(0, newReason.length() - 1);
							one = true;
						}
						String newLength = "";
						if (arg.containsKey("l")) {
							newLength = arg.get("l");
							newLength = newLength.substring(0, newLength.length() - 1);
						}
						String newType = "";
						if (arg.containsKey("t")) {
							newType = arg.get("t");
							newType = newType.substring(0, newType.length() - 1);
							one = true;
						}
						if (!one) {
							sendMessage(sender, ChatColor.RED + "Usage: /bupdate <player> [r:reason] [l:length] [t:type]");
							return true;
						}
						
						
						
						if (!newReason.equalsIgnoreCase("") && newType.equalsIgnoreCase("")) {
							MySQL.editRow(MySQL.con, currentType + "_bans", "reason='" + newReason + "'", "id=" + id);
						}
						
						if (!newLength.equalsIgnoreCase("") && newType.equalsIgnoreCase("")) {
							if (currentType.equalsIgnoreCase("temp")) {
								MySQL.editRow(MySQL.con, "temp_bans", "length='" + newLength + "'", "id=" + id);
							} else {
								sendMessage(sender, ChatColor.RED + "You can't change the length of a perma ban!");
								return true;
							}
						}
						
						if (!newType.equalsIgnoreCase("")) {
							if (newType.equalsIgnoreCase("perma")) {
								if (currentType.equalsIgnoreCase("temp")) {
									MySQL.deleteRow(MySQL.con, "temp_bans", id, "player='" + args[0] + "'");
									MySQL.insertInfo(MySQL.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + args[0] + "','" + (newReason.equalsIgnoreCase("") ? currentReason : newReason) + "','" + staff + "','" + date + "'");
								} else {
									sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is already perma banned!");
									return true;
								}
							} else if (newType.equalsIgnoreCase("temp")) {
								if (currentType.equalsIgnoreCase("perma")) {
									if (!newLength.equalsIgnoreCase("")) {
										MySQL.deleteRow(MySQL.con, "perma_bans", id, "player='" + args[0] + "'");
										MySQL.insertInfo(MySQL.con, "temp_bans", "`id`,`player`,`length`,`staff`,`reason`,`when`,`date`", id + ",'" + args[0] + "','" + newLength + "','" + staff + "','" + (newReason.equalsIgnoreCase("") ? currentReason : newReason) + "','" + String.valueOf(System.currentTimeMillis()) + "','" + date + "'");
									} else {
										sendMessage(sender, ChatColor.RED + "When changing type to temp, length is required!");
										return true;
									}
								} else {
									sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is already temp banned!");
									return true;
								}
								
							} else {
								Bukkit.broadcastMessage(newType);
								sendMessage(sender, ChatColor.RED + "Type can only be perma/temp!");
								return true;
							}
						}
						sendMessage(sender, ChatColor.GOLD + "Ban updated successfully");
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not banned!");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /bupdate <player> [r:reason] [l:length] [t:type]");
					return true;
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}


}
