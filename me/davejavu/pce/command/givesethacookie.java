package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class givesethacookie extends PalCommand {
	Plugin plugin;
	public givesethacookie(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public givesethacookie(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("givesethacookie")) {
			if (Bukkit.getServer().getOfflinePlayer("sethprower").isOnline()) {
				Player p = Bukkit.getServer().getPlayer("sethprower");
				ItemStack a = new ItemStack(357);
				p.getInventory().addItem(a);
				sendMessage(sender, ChatColor.GOLD + "Gave seth a cookie!");
				p.sendMessage(ChatColor.GOLD + "Received a cookie from " + ChatColor.WHITE + sender.getName());
				return true;
			}
		}
		return false;
	}

}
