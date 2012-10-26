package me.davejavu.pce.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.davejavu.pce.*;

public class troll extends PalCommand {
	Plugin plugin;
	public troll(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public troll(){}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("troll")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.troll")) {
				
				if (args.length > 0) {
					
					switch(args[0]) {
					case "gag":
						if (args[1].equalsIgnoreCase("add")) {
							if (args.length > 2) {
								StringBuilder msg = new StringBuilder();
								for (int i = 2; i < args.length; i++) {
									msg.append(args[i] + " ");
								}
								Methods.insertInfo(Methods.mysqlConnect(host, port, database, username, password), "troll", "`message`","'" + msg.toString() + "'");
								sendMessage(sender, ChatColor.GOLD + "Added TrollGag message: " + ChatColor.WHITE + msg.toString());
								return true;
							} else {
								sendMessage(sender, ChatColor.RED + "Usage: /troll gag add <message>");
								return true;
							}
						} else if (args[1].equalsIgnoreCase("removeall")) {
							PalCraftListener.gag = new ArrayList<String>();
							sendMessage(sender, ChatColor.GOLD + "All gags removed");
							return true;
						} else {
							if (args.length == 2) {
								Player p2 = getPlayer(args[1]);
								if (p2 == null) {
									sendMessage(sender, ChatColor.RED + "'" + args[1] + "' is not online!");
									return true;
								}
								boolean is = false;
								if (PalCraftListener.gag.contains(p2.getName())) {
									is = false;
									PalCraftListener.gag.remove(p2.getName());
								} else {
									is = true;
									PalCraftListener.gag.add(p2.getName());
								}
								sendMessage(sender, ChatColor.GOLD + "TrollGag for " + ChatColor.WHITE + p2.getName() + ": " + (is ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
								return true;
							} else {
								sendMessage(sender, ChatColor.RED + "Usage: /troll gag <player>");
								return true;
							}
						}
					case "drunk":
						if (args.length == 2) {
							int time = 1200;
							if (args.length == 3) {
								time = Integer.parseInt(args[2]) * 20;
							}
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							p2.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, time, 10000));
							sendMessage(sender, ChatColor.GOLD + "Made " + p2.getName() + " drunk.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll  <player>");
							return true;
						}
					case "blind":
						if (args.length == 2) {
							int time = 1200;
							if (args.length == 3) {
								time = Integer.parseInt(args[2]) * 20;
							}
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							p2.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time, 10000));
							sendMessage(sender, ChatColor.GOLD + "Made " + p2.getName() + " blind.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll blind <player>");
							return true;
						}
					case "wtf":
						if (args.length == 2) {
							int time = 1200;
							if (args.length == 3) {
								time = Integer.parseInt(args[2]) * 20;
							}
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							p2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time, 1000));
							p2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 1000));
							sendMessage(sender, ChatColor.GOLD + "Made " + p2.getName() + " have several really messed up effects.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll wtf <player>");
							return true;
						}
					case "removeeffects":
						if (args.length == 2) {
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							for (PotionEffect p : p2.getActivePotionEffects()) {
								p2.removePotionEffect(p.getType());
							}
							sendMessage(sender, ChatColor.GOLD + "Removed " + p2.getName() + "'s effects");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll removeeffects <player>");
							return true;
						}
					case "banish":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							int X = -1000 + (int)(Math.random() * 2001.0D);
							int Y = 75 + (int)(Math.random() * 11.0D);
							int Z = -1000 + (int)(Math.random() * 2001.0D);
							Location bLoc = new Location (p2.getWorld(), X, Y, Z);
							p2.teleport(bLoc);
							p2.sendMessage(ChatColor.RED + "You've been banished from society!");
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll banish <player>");
							return true;
						}
					case "cannon":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[2]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							double speed = 2.0;
							speed = Double.parseDouble(args[1]);
							TNTPrimed tnt = (TNTPrimed)p2.getWorld().spawn(p2.getLocation(), TNTPrimed.class);
							tnt.setVelocity(p2.getLocation().getDirection().multiply(speed));
							p2.sendMessage(ChatColor.RED + "FIRIN' THE CANNON");
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll cannon <speed> <player>");
							return true;
						}
					case "creep":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							Creeper creep = (Creeper) p2.getWorld().spawn(p2.getLocation(), Creeper.class);
							creep.setPowered(true);
							p2.getWorld().playEffect(p2.getLocation(), Effect.GHAST_SHRIEK, 0);
							p2.sendMessage(ChatColor.RED + "BOOM.");
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll creep <player>");
							return true;
						}
					case "electrify":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							p2.getWorld().strikeLightning(p2.getLocation());
							p2.sendMessage(ChatColor.RED + "Electrified, ouch.");
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll electrify <player>");
							return true;
						}
					case "entomb":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							Location tloc = p2.getLocation();
							tloc.setY(45);
							p2.teleport(tloc);
							p2.sendMessage(ChatColor.RED + "Might wanna dig up.");
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll entomb <player>");
							return true;
						}
					case "lift":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							p2.setVelocity(new Vector(0.0, 10.0, 0.0));
							p2.sendMessage(ChatColor.RED + "Fly like a bird!");
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll lift <player>");
							return true;
						}
					case "spam":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							for (int i = 0; i < 50; i++) {
								Random r = new Random();
								ChatColor[] cl = ChatColor.values();
								ChatColor x = cl[r.nextInt(cl.length)];
								p2.sendMessage(x + "SPAM SPAM SPAM SPAM SPAM SPAM SPAM SPAM SPAM" + ChatColor.RESET);
								p2.getWorld().playEffect(p2.getLocation(), Effect.EXTINGUISH, 0);
							}
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll spam <player>");
							return true;
						}
					case "swap":
						if (args.length > 0){
							Player p2;
							Player p3;
							if (args.length == 3) {
								List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
								if (!(p2l.size() > 0)) {
									sendMessage(sender, ChatColor.RED + "Player is not online!");
									return true;
								}
								p2 = p2l.get(0);
								List<Player> p3l = Bukkit.getServer().matchPlayer(args[1]);
								if (!(p3l.size() > 0)) {
									sendMessage(sender, ChatColor.RED + "Player is not online!");
									return true;
								}
								p3 = p2l.get(0);
							} else {
								Player[] op = Bukkit.getServer().getOnlinePlayers();
								Random rand = new Random();
								p2 = op[rand.nextInt(op.length)];
								p3 = op[rand.nextInt(op.length)];
							}
							Location p2loc = p2.getLocation();
							Location p3loc = p3.getLocation();
							p2.teleport(p3loc);
							p3.teleport(p2loc);
							p2.sendMessage(ChatColor.RED + "Swapped with " + p3.getName() + ", oh noes!");
							p3.sendMessage(ChatColor.RED + "Swapped with " + p3.getName() + ", oh noes!");
							sendMessage(sender, ChatColor.GOLD + "Troll successful. Swapped " + p2.getName() + " and " + p3.getName());
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll swap [player1] [player2]");
							return true;
						}
					case "zombify":
						if (args.length == 2){
							List<Player> p2l = Bukkit.getServer().matchPlayer(args[1]);
							if (!(p2l.size() > 0)) {
								sendMessage(sender, ChatColor.RED + "Player is not online!");
								return true;
							}
							Player p2 = p2l.get(0);
							for (int i = 0; i < 50; i++) {
								Zombie z = (Zombie)p2.getWorld().spawn(p2.getLocation(), Zombie.class);
								z.setTarget(p2);
							}
							p2.sendMessage(ChatColor.RED + "Good luck.");
							sendMessage(sender, ChatColor.GOLD + "Troll successful.");
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /troll removeeffects <player>");
							return true;
						}
					case "moo":
						if (args.length == 2){
							if (args[1].equalsIgnoreCase("removeall")) {
								PalCraftListener.moo.clear();
								sendMessage(sender, ChatColor.GOLD + "All moos removed");
								return true;
							}
							Player p2 = getPlayer(args[1]);
							if (p2 == null) {
								sendMessage(sender, ChatColor.RED + "'" + args[1] + "' is not online!");
								return true;
							}
							boolean c = false;
							String p2a = p2.getName().toLowerCase();
							if (PalCraftListener.moo.contains(p2a)) {
								c = false;
								PalCraftListener.moo.remove(p2a);
							} else {
								c = true;
								PalCraftListener.moo.add(p2a);
							}
							sendMessage(sender, ChatColor.GOLD + "Moo for " + ChatColor.WHITE + p2a + ": " + (c ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "/troll moo <player>");
							return true;
						}
					default:
						sendMessage(sender, ChatColor.RED + "Usage: /troll <moo/blind/drunk/wtf/banish/bomb/cannon/creep/electrify/entomb/lift/spam/swap//zombify>");
						sendMessage(sender, ChatColor.RED + "Usage: /troll gag <player/add> <message>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /troll <moo/blind/drunk/wtf/banish/bomb/cannon/creep/electrify/entomb/lift/spam/swap//zombify>");
					sendMessage(sender, ChatColor.RED + "Usage: /troll gag <player/add> <message>");
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
