package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;

public class chat extends PalCommand {
	
	public chat(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("chat")) {
			if (args.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (String a : args) {
					sb.append(a + " ");
				}
				if (sender instanceof Player) {
					Player pl = (Player)sender;
					pl.chat(sb.toString().replaceAll("&([0-9a-rA-R])", "§$1"));
				} else {
					Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] " + sb.toString().replaceAll("&([0-9a-rA-R])", "§$1"));
				}
				return true;
			} else {
				sendMessage(sender, ChatColor.RED + "Usage: /chat <message>");
				return true;
			}
		}
		return false;
	}
}
