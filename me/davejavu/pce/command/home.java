package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class home extends PalCommand {
	
	public home(){}
	
	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("home")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.home")) {
				if (sender instanceof Player) {
					String youhave = "You have";
					String your = "your";
					OfflinePlayer player = (OfflinePlayer) sender;
					if (args.length > 0) {
						if (permissionCheck(sender, "PalCraftEssentials.command.home.others")) {
							player = Bukkit.getOfflinePlayer(args[0]);
							your = player.getName() + "'s";
							youhave = player.getName() + " has";
						} else {
							noPermission(cmd.getName() + " - others", sender);
							return true;
						}
					}
					if (player != null) {
						CustomConfig pConfig = getConfig(player);
						if (pConfig == null || !pConfig.getFC().contains("homes.home.world")) {
							sendMessage(sender, ChatColor.RED + youhave + " no home!");
							sendMessage(sender, ChatColor.GOLD + "Attempting to load home from backup...");
							CustomConfig conf = new CustomConfig("./plugins/PalCraftEssentials/", "homeBackup.yml");
							if (conf.getFC().contains("homes." + player.getName().toLowerCase() + ".world")) {
								Location nH = new Location(Bukkit.getWorld(conf.getFC().getString("homes." + player.getName().toLowerCase() + ".world")), conf.getFC().getDouble("homes." + player.getName().toLowerCase() + ".x"), conf.getFC().getDouble("homes." + player.getName().toLowerCase() + ".y"), conf.getFC().getDouble("homes." + player.getName().toLowerCase() + ".z"));
								nH.setPitch(Float.parseFloat(conf.getFC().getString("homes." + player.getName().toLowerCase() + ".pitch")));
								nH.setYaw(Float.parseFloat(conf.getFC().getString("homes." + player.getName().toLowerCase() + ".yaw")));
								((Player)sender).teleport(nH);
								sendMessage(sender, ChatColor.GOLD + "Teleported to " + your + " home");
								sethome.setHome(player.getName(), nH);
							} else {
								sendMessage(sender, ChatColor.RED + "Unable to load from backup!");
								return true;
							}
						} else {
							Location nH = new Location(Bukkit.getWorld(pConfig.getFC().getString("homes.home.world")), pConfig.getFC().getDouble("homes.home.x"), pConfig.getFC().getDouble("homes.home.y"), pConfig.getFC().getDouble("homes.home.z"));
							nH.setPitch(Float.parseFloat(pConfig.getFC().getString("homes.home.pitch")));
							nH.setYaw(Float.parseFloat(pConfig.getFC().getString("homes.home.yaw")));
							((Player)sender).teleport(nH);
							sendMessage(sender, ChatColor.GOLD + "Teleported to " + your + " home");
							return true;
						}
					} else {
						sendMessage(sender, ChatColor.RED + youhave + " no home!");
						return true;
					}
					
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

}
