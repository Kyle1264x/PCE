package me.davejavu.pce.command;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class item extends PalCommand {
	Plugin plugin;
	public item(){}
	public item(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("item")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.item")) {
					Player player = (Player) sender;
					if (args.length > 0) {
						Material mat;
						int amt = 64;
						byte data = (byte) 0;
						if (args[0].matches("-?\\d+(.\\d+)?")){
							mat = Material.getMaterial(Integer.parseInt(args[0]));
							if (mat == null) {
								sendMessage(sender, ChatColor.RED + "No item with id " + args[0]);
							}
						} else {
							mat = Material.getMaterial(args[0].toUpperCase());
							if (mat == null) {
								sendMessage(sender, ChatColor.RED + "No item with name " + args[0]);
								return true;
							}
							
						}
						if (args.length == 2) {
							try{
								amt = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								sendMessage(sender, ChatColor.RED + "'" + args[1] + "' isn't a number!");
								return true;
							}
						}
						ItemStack item = new ItemStack(mat, amt, data);
						player.getInventory().addItem(item);
						sendMessage(sender, ChatColor.GOLD + "Given " + ChatColor.WHITE + amt + " " + mat.toString().toLowerCase());
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /item <id> [amount]");
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
