package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class give extends PalCommand {
	Plugin plugin;
	public give(){}
	public give(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("give")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.give")) {
				if (args.length > 0) {
					Player p2 = getPlayer(args[0]);
					if (p2 == null) {
						sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
						return true;
					}
					Material mat = Material.AIR;
					int amt = 64;
					if (args.length == 3) {
						try{
							amt = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							sendMessage(sender, ChatColor.RED + "'" + args[2] + "' is not a number!");
							return true;
						}
					}
					byte data = (byte) 0;
					String id;
					if (args[1].contains(":")) {
						String[] p = args[1].split(":");
						data = (byte)Integer.parseInt(p[1]);
						id = p[0];
					} else {
						id = args[1];
					}


					if (args[1].matches("-?\\d+(.\\d+)?")){
						mat = Material.getMaterial(Integer.parseInt(id));
						if (mat == null) {
							sendMessage(sender, ChatColor.RED + "No item with id " + args[1]);
						}
					} else {
						mat = Material.getMaterial(id.toUpperCase());
						if (mat == null) {
							sendMessage(sender, ChatColor.RED + "No item with name " + args[1]);
							return true;
						}

					}
					ItemStack item = new ItemStack(mat, amt, data);
					p2.getInventory().addItem(item);
					p2.sendMessage(ChatColor.GOLD + "Given " + ChatColor.WHITE + amt + " " + mat.toString().toLowerCase());
					sendMessage(sender, ChatColor.GOLD + "Gave " + p2.getName() + " " + ChatColor.WHITE + amt + " " + mat.toString().toLowerCase());
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /give <player> <item> [amount]");
					return true;
				}
			} else{
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	

}
