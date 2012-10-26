package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class god extends PalCommand {
	Plugin plugin;
	public god(){}
	public god(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("god")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.god")) {
					Player pl = (Player) sender;
					if(args.length == 1) {
						if (permissionCheck(sender, "PalCraftEssentials.command.god.others")) {
							pl = getPlayer(args[0]);
							if (pl == null) {
								sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
								return true;
							}
						} else {
							noPermission(cmd.getName() + " - others", sender);
							return true;
						}
					}
					CustomConfig con = getConfig(pl);
					FileConfiguration config = con.getFC();
					boolean isGod = config.getBoolean("god");
					config.set("god", !isGod);
					pl.sendMessage(ChatColor.GOLD + "God" + ChatColor.WHITE + ": " + (!isGod ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
					con.save();
					if (((Player) sender) != pl) {
						sendMessage(sender, ChatColor.GOLD + "God for " + ChatColor.WHITE +pl.getName() + ": " + (!isGod ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
						return true;
					}
					return true;
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "This command can only be run in game!");
				return true;
			}
		}
		return false;
	}

}
