package me.davejavu.pce.command;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;
import me.davejavu.pce.PalCraftListener;

public class info extends PalCommand {
	
	static Plugin plugin;
	public info(){}
	public info(PalCraftEssentials plugin) {
		info.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("info")) {
			if (args.length == 0 || args.length == 1) {
				if (permissionCheck(sender, "PalCraftEssentials.command.info.self")) {
					if (sender instanceof Player) {
						String name = sender.getName().toLowerCase();
						if (args.length == 1) {
							if (permissionCheck(sender, "PalCraftEssentials.command.info.others")) {
								name = args[0].toLowerCase();
							} else {
								noPermission(cmd.getName(), sender);
								return true;
							}
						}
						CustomConfig config = getConfig(((Player)sender));
						long op = config.getFC().getLong("playtime");
						config.getFC().set("playtime", op + (System.currentTimeMillis() - PalCraftListener.play.get(name)));
						config.save();
						PalCraftListener.play.remove(name);
						PalCraftListener.play.put(name, System.currentTimeMillis());
						long millis = System.currentTimeMillis() - Bukkit.getOfflinePlayer(name).getFirstPlayed();
						long days = TimeUnit.MILLISECONDS.toDays(millis);
						long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
						long mins = TimeUnit.MILLISECONDS.toMinutes(millis) - (TimeUnit.DAYS.toMinutes(days) + TimeUnit.HOURS.toMinutes(hours));
						long secs = TimeUnit.MILLISECONDS.toSeconds(millis) - (TimeUnit.DAYS.toSeconds(days) + TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(mins));
						sendMessage(sender, ChatColor.GOLD + name + "'s stats:");
						sendMessage(sender, ChatColor.GOLD + "First played: " + ChatColor.WHITE + String.format("%02d days, %02d hours, %02d minutes, %02d seconds", days, hours, mins, secs) + " ago.");
						sendMessage(sender, ChatColor.GOLD + "Playtime: " + ChatColor.WHITE + getPlaytime(name));
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "This command can only be used in game!");
						return true;
					}
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Usage: /info [player]");
				return true;
			}
			
		}
		return false;
	}
	
	public static String getPlaytime(String player) {
		reloadConfig();
		String rt = "";
		if (getConfig().getFC().contains("players." + player.toLowerCase() + ".playtime")) {
			long l = getConfig().getFC().getLong("playtime." + player.toLowerCase());
			rt = PalCraftEssentials.parseLongToString(l);
		}
		return rt;
	}

}
