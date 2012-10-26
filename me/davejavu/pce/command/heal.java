package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class heal extends PalCommand {
	Plugin plugin;
	public heal(){}
	public heal(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("heal")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.heal")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length == 1) {
						if (permissionCheck(sender, "PalCraftEssentials.command.heal.others")) {
							Player p2 = getPlayer(args[0]);
							if (p2 == null) {
								sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
								return true;
							}
							player = p2;
						} else {
							noPermission(cmd.getName() + " - others", sender);
							return true;
						}
						
					}
					player.setHealth(20);
					player.setFoodLevel(20);
					sendMessage(sender, ChatColor.GOLD + "Healed!");
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
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
