package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class more extends PalCommand {
	Plugin plugin;
	public more(){}
	public more(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("more")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.more")) {
					if (((Player) sender).getItemInHand().getTypeId() != 0) {
						((Player) sender).getItemInHand().setAmount(64);
						sendMessage(sender, ChatColor.GOLD + "Given more of " + ((Player) sender).getItemInHand().getType().toString().toLowerCase());
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "No item in your hand!");
						return true;
					}
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	
	
	
	
}
