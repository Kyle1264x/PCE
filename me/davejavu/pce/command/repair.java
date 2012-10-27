package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class repair extends PalCommand {
	Plugin plugin;
	public repair(){}
	public repair(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("repair")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.repair")) {
					if (((Player)sender).getItemInHand() != null) {
						ItemStack itemInHand = ((Player)sender).getItemInHand();
						itemInHand.setDurability((short) 0);
						sendMessage(sender, ChatColor.GOLD + "Repaired your " + ChatColor.WHITE + itemInHand.getType().toString().toLowerCase());
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "You need an item in your hand!");
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
