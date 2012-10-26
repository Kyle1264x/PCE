package me.davejavu.pce.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class socialspy extends PalCommand {
	public socialspy(){}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("socialspy")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.socialspy")) {
				if (sender instanceof Player) {
					CustomConfig config = getConfig(((Player)sender));
					boolean isSocialSpy = config.getFC().getBoolean("socialspy");
					config.getFC().set("socialspy", !isSocialSpy);
					sendMessage(sender, ChatColor.GOLD + "SocialSpy" + ChatColor.WHITE + ": " + (!isSocialSpy ? ChatColor.GREEN + "Enabled!" : ChatColor.RED + "Disabled!"));
					config.save();
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
	
	public static List<String> getSocialSpies() {
		List<String> ret = new ArrayList<String>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			CustomConfig conf = getConfig(p);
			boolean iSS = conf.getFC().getBoolean("socialspy");
			if (iSS) {
				ret.add(p.getName());
			}
		}
		return ret;
	}
}
