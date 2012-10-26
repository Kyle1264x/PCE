package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class sethome extends PalCommand {
	public sethome(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sethome")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.sethome")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					Location nH = player.getLocation();
					if (args.length == 3) {
						nH = new Location(Bukkit.getWorld(player.getWorld().getName()), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
					}
					setHome(player.getName(), nH);
					sendMessage(sender, ChatColor.GOLD + "Home set!");
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
	public static void setHome(String player, Location nH) {
		CustomConfig pConfig = getConfig(player);
		pConfig.getFC().set("homes.home.world", nH.getWorld().getName());
		pConfig.getFC().set("homes.home.x", nH.getX());
		pConfig.getFC().set("homes.home.y", nH.getY());
		pConfig.getFC().set("homes.home.z", nH.getZ());
		pConfig.getFC().set("homes.home.pitch", nH.getPitch());
		pConfig.getFC().set("homes.home.yaw", nH.getYaw());
		pConfig.save();
		CustomConfig cf = new CustomConfig("./plugins/PalCraftEssentials", "homeBackup.yml");
		cf.getFC().set("homes." + player.toLowerCase() + ".world", nH.getWorld().getName());
		cf.getFC().set("homes." + player.toLowerCase() + ".x", nH.getX());
		cf.getFC().set("homes." + player.toLowerCase() + ".y", nH.getY());
		cf.getFC().set("homes." + player.toLowerCase() + ".z", nH.getZ());
		cf.getFC().set("homes." + player.toLowerCase() + ".pitch", nH.getPitch());
		cf.getFC().set("homes." + player.toLowerCase() + ".yaw", nH.getYaw());
		cf.save();
	}
	
}
