package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class enchant extends PalCommand {
	Plugin plugin;
	public enchant(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public enchant(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("enchant")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.enchant")) {
				if (args.length > 1) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						try{
							Integer.parseInt(args[1]);
						} catch (NumberFormatException e) {
							sendMessage(sender, ChatColor.RED + "'" + args[1] + "' isn't a number!");
							return true;
						}
						try{
							Enchantment.getByName(args[0]);
						} catch (Exception e){
							sendMessage(sender, ChatColor.RED + "'" + args[0] + "' isn't an enchantment!");
							return true;
						}
						int level = Integer.parseInt(args[1]);
						Enchantment en = Enchantment.getByName(args[0]);
						if (player.getItemInHand().getType() != Material.AIR) {
							player.getItemInHand().addUnsafeEnchantment(en, level);
							sendMessage(sender, ChatColor.GOLD + "Enchanted " + player.getItemInHand().getType().toString().toLowerCase() + " with " + args[0].toLowerCase() + ", level " + args[1]);
						} else {
							sendMessage(sender, ChatColor.RED + "You don't have an item in your hand!");
							return true;
						}
					} else {
						sendMessage(sender, ChatColor.RED + "This command can only be executed in game!");
						return true;
					}
				} else {
					StringBuilder ench = new StringBuilder();
					for (Enchantment c : Enchantment.values()) {
						ench.append(c.getName()+" ");
					}
					sendMessage(sender, ChatColor.GOLD + "Enchant types: " + ChatColor.WHITE + ench.toString());
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
