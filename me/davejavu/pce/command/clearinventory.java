package me.davejavu.pce.command;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class clearinventory extends PalCommand {
	Plugin plugin;
	public clearinventory(){}
	public clearinventory(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("clearinventory")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.clearinventory")) {
					if (args.length > 0) {
						Player p2 = getPlayer(args[0]);
						if (p2 == null) {
							sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
							return true;
						}
						p2.getInventory().clear();
						p2.sendMessage(ChatColor.GOLD + "Inventory cleared.");
						sendMessage(sender, ChatColor.GOLD + "Cleared " + p2.getName() + "'s inventory.");
						return true;
					} else {
						((Player) sender).getInventory().clear();
						sendMessage(sender, ChatColor.GOLD + "Inventory cleared.");
						return true;
					}
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
				return true;
			}
		}
		return false;
	}
	
}
