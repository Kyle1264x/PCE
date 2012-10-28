package me.davejavu.pce.command;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.*;

public class convert extends PalCommand {
	Plugin plugin;
	public convert(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public convert(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("convert")) {
			if (permissionCheck(sender, "command.convert")) {
				if (args.length == 1) {
					ResultSet r = MySQL.getRows(MySQL.con, "perma_bans");
					if (args[0].equalsIgnoreCase("from")) {
						try {
							while (r.next()) {
								Player p = Bukkit.getServer().getPlayer(r.getString("player"));
								p.setBanned(true);
							}
						} catch (Exception e) {
							sendMessage(sender, ChatColor.RED + "An error occurred.");
							e.printStackTrace();
							return true;
						}
						sendMessage(sender, ChatColor.GOLD + "All bans converted from MySQL to local.");
						return true;
					} else if (args[0].equalsIgnoreCase("to")) {
						List<String> s = new ArrayList<String>();
						int id = 0;
						try{
							while(r.next()) {
								s.add(r.getString("player").toLowerCase());
								id++;
							}
						} catch (Exception e) {
							sendMessage(sender, ChatColor.RED + "An error occurred");
							e.printStackTrace();
							return true;
						}
						
						for (OfflinePlayer p : Bukkit.getServer().getBannedPlayers()) {
							if (!s.contains(p.getName().toLowerCase())) {
								MySQL.insertInfo(MySQL.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + p.getName() + "','" + getConfig().getFC().getString("banreason." + p.getName().toLowerCase()) + "','" + getConfig().getFC().getString("whobanned." + p.getName().toLowerCase()) + "','" + date + "'");
								id++;
							}
						}
						sendMessage(sender, ChatColor.GOLD + "All bans converted from local to MySQL.");
					} else if (args[0].equalsIgnoreCase("ultraban")) {
						try{
							File file = new File("ultrabans.txt");
							FileInputStream fs = new FileInputStream(file);
							BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(fs)));
							String ln;
							
							List<String> s = new ArrayList<String>();
							int id = 0;
							try{
								while(r.next()) {
									s.add(r.getString("player").toLowerCase());
									id++;
								}
							} catch (Exception e) {
								sendMessage(sender, ChatColor.RED + "An error occurred");
								e.printStackTrace();
								br.close();
								return true;
							}
							while ((ln = br.readLine()) != null) {
								String[] lns = ln.split("#");
								if (lns.length > 0) {
									if (!s.contains(lns[0])) {
										Bukkit.getServer().getOfflinePlayer(lns[0]).setBanned(true);
										MySQL.insertInfo(MySQL.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + lns[0] + "','" + lns[4] + "','" + lns[2] + "','" + lns[1] + "'");
										id++;
									}
								}
							}
							sendMessage(sender, ChatColor.GOLD + "All bans converted from ultraban to PalCraftEssentials MySQL.");
							br.close();
						} catch (Exception e) {
							e.printStackTrace();
							sendMessage(sender, ChatColor.RED + "An error occurred.");
							return true;
						}
						
					} else if (args[0].equalsIgnoreCase("homes")){
						File file = new File("./plugins/Essentials/userdata");
						for (String x : file.list()) {
							CustomConfig essCon = new CustomConfig("./plugins/Essentials/userdata", x);
							if (essCon.getFC().contains("homes.home.world")) {
								Location loc =  new Location(Bukkit.getWorld(essCon.getFC().getString("homes.home.world")), essCon.getFC().getDouble("homes.home.x"), essCon.getFC().getDouble("homes.home.y"), essCon.getFC().getDouble("homes.home.z"));
								loc.setPitch(Float.parseFloat(essCon.getFC().getString("homes.home.yaw")));
								loc.setYaw(Float.parseFloat(essCon.getFC().getString("homes.home.yaw")));
								sethome.setHome(x, loc);
							}
						}
						sendMessage(sender, ChatColor.GOLD + "Converted homes");
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /convert <from/to/ultraban>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /convert <from/to/ultraban>");
					return true;
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	public static List<String> listWarps() {
		List<String> ret = new ArrayList<String>();
		File file = new File("./plugins/PalCraftEssentials/warps");
		for (String x : file.list()) {
			ret.add(x.replace(".yml", ""));
		}
		return ret;
	}
}
