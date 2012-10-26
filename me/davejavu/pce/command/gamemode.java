package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class gamemode extends PalCommand {
	
	Plugin plugin;
	public gamemode(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public gamemode(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gamemode")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.gamemode")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length == 0) {
						player.setGameMode((player.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL));
						sendMessage(sender, ChatColor.GOLD + "Switched to " + player.getGameMode().toString().toLowerCase());
						return true;
					} else {
						if (args.length == 1 || args.length == 2) {
							Player pl;
							String name;
							String nameCaps;
							String be;
							String nb;
							if (args.length == 1) {
								pl = player;
								name = "you";
								nameCaps = "You";
								be = "are";
								nb = "You are";
							} else {
								pl = getPlayer(args[1]);
								if (pl == null) {
									sendMessage(sender, ChatColor.RED + "'" + args[1] + "' is not online!");
									return true;
								}
								name = pl.getName();
								nameCaps = pl.getName();
								be = "is";
								nb = pl.getName() + "'s";
							}
							boolean num = false;
							try{
								Integer.parseInt(args[0]);
								num = true;
							} catch (NumberFormatException e) {
								num = false;
							}
							if (num) {
								if (Integer.parseInt(args[0]) < 3) {
									if (pl.getGameMode() == GameMode.getByValue(Integer.parseInt(args[0]))) {
										sendMessage(sender, ChatColor.RED + nameCaps + " " + be + " already in game mode " + args[0]);
										return true;
									} else {
										pl.setGameMode(GameMode.getByValue(Integer.parseInt(args[0])));
										sendMessage(sender, ChatColor.GOLD + "Switched " + name + " to game mode " + pl.getGameMode().toString().toLowerCase());
										return true;
									}
								} else {
									sendMessage(sender, ChatColor.RED + args[0] + " isn't a gamemode! (0/1/2)");
									return true;
								}
								
								
							} else {
								try{
									GameMode.valueOf(args[0]);
								} catch (Exception e) {
									sendMessage(sender, ChatColor.RED + args[0] + " isn't a gamemode!");
									return true;
								}
								if (pl.getGameMode() == GameMode.valueOf(args[0])) {
									sendMessage(sender, ChatColor.RED + nb + " game mode is already " + args[0]);
									return true;
								} else {
									pl.setGameMode(GameMode.valueOf(args[0]));
									sendMessage(sender, ChatColor.GOLD + nb + " to game mode " + args[0]);
									return true;
								}
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /gamemode [0/1/2] [player]");
							return true;
						}
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Only players in game can run this command!");
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
