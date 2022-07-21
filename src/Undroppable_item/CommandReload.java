package Undroppable_item;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class CommandReload implements CommandExecutor{
	private Event_listener event_Listener;
	
	public CommandReload (Event_listener event_Listener) {
		this.event_Listener = event_Listener;
	}
	
	public void reload_config() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Undroppable_item");
		FileConfiguration config;
		boolean enable;
		String unDroppableAfterDeathLore;
		String blockedInInventory;
		plugin.reloadConfig();
		config = plugin.getConfig();
		if (!config.isBoolean("enable")) {
			config.set("enable", true);
		}
		enable = config.getBoolean("enable");
		if (!config.isString("undroppable-after-death-lore")) {
			config.set("undroppable-after-death-lore", "nonDrope");
		}
		unDroppableAfterDeathLore = config.getString("undroppable-after-death-lore");
		if (!config.isString("blocked-in-inventory-lore")) {
			config.set("blocked-in-inventory-lore", "blocked");
		}
		blockedInInventory = config.getString("blocked-in-inventory-lore");
		plugin.saveConfig();
		event_Listener.setBlockedInInventoryLore(blockedInInventory);
		event_Listener.setUnDroppableAfterDeathLore(unDroppableAfterDeathLore);
		if (!enable) {
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
		return;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof ConsoleCommandSender) && 
				!sender.hasPermission("UndroppableItem.reload")) {
			sender.sendMessage("У вас нет на это прав!");
			return false;
			}
		if (args.length!=1) {
			sender.sendMessage("Неверные аргументы!");
			return false;
		}
		if (args[0].equals("reload")) {
			reload_config();
			sender.sendMessage("Config has been reloaded!");
			return true;
		}
		return false;
	}

}
