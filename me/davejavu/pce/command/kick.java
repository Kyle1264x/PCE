package me.davejavu.pce.command;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.Methods;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class kick extends PalCommand {
	Plugin plugin;
	public kick(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public kick(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kick")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.kick")) {
				if (args.length > 1) {
					if (args[0].equalsIgnoreCase("list")) {
						ResultSet r = Methods.getRows(Methods.con, "player_info");
						boolean isInTable = false;
						String prevKicks = "";
						try{
							while(r.next()) {
								if (args[1].equalsIgnoreCase(r.getString("player"))) {
									isInTable = true;
									prevKicks = r.getString("kicks");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (isInTable && !prevKicks.equalsIgnoreCase(" ")) {
							sendMessage(sender, ChatColor.RED + args[1] + " has been kicked " + (prevKicks.split(";").length > 4 ? ChatColor.DARK_RED : "") + prevKicks.split(";").length + " time" + (prevKicks.split(";").length > 1 ? "s" : ""));
							int counter = 1;
							for (String ra : prevKicks.split(";")) {
								sendMessage(sender, ChatColor.RED + String.valueOf(counter) + ChatColor.WHITE + ": " + ra);
								counter++;
							}
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + args[1] + " hasn't been kicked before!");
							return true;
						}
					} else {
						StringBuilder reason = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							reason.append(args[i] + " ");
						}
						Player p2 = getPlayer(args[0]);
						if (p2 == null) {
							sendMessage(sender, ChatColor.RED + args[0] + " is not online!");
							return true;
						}
						if (p2.hasPermission("PalCraftEssentials.kick.exempt")) {
							sendMessage(sender, ChatColor.RED + "You can't kick " + args[0]);
							return true;
						} else {
							ResultSet r = Methods.getRows(Methods.con, "player_info");
							boolean isInTable = false;
							String prevKicks = "";
							try{
								while(r.next()) {
									if (args[0].equalsIgnoreCase(r.getString("player"))) {
										isInTable = true;
										prevKicks = r.getString("kicks");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							String kick = sender.getName() + " - " + reason.toString() + ";";
							p2.kickPlayer(reason.toString());
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Kicking " + p2.getDisplayName() + ": " + ChatColor.WHITE + reason.toString());
							prevKicks += kick;
							if (isInTable) {
								Methods.editRow(Methods.con, "player_info", "player='" + args[0] + "'","kicks='" + prevKicks + kick + "'");
								sendMessage(sender, ChatColor.RED + args[0] + " has been kicked " + (prevKicks.split(";").length > 4 ? ChatColor.DARK_RED : "") + prevKicks.split(";").length + ChatColor.RED + " time" + (prevKicks.split(";").length > 1 ? "s" : "") + ".");
							} else {
								kick.replace("'", "");
								Methods.insertInfo(Methods.con, "player_info", "`player`,`warnings`,`bans`,`kicks`","'" + args[0] + "',' ',' ','" + kick + "'");
							}
							return true;
						}
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /kick <player> <reason>");
					sendMessage(sender, ChatColor.RED + "Usage: /kick <list> <player>");
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
