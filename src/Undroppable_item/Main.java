package Undroppable_item;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private boolean enable;
	private FileConfiguration config;
	private String unDroppableAfterDeathLore;
	private String blockedInInventory;
	private Event_listener event_Listener;

	public void check_config() {
		config = getConfig();
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
		saveConfig();
		return;
	}
	
	private void printHelloInConsole() {
		ConsoleCommandSender console = getServer().getConsoleSender();
		console.sendMessage("===========================");
		console.sendMessage("|   |     ___   ___        ");
		console.sendMessage("|   |  0  |  |  |     \\  / ");
		console.sendMessage("|===|     |__|  |__    \\/  ");
		console.sendMessage("|   |  |  |     |      /\\  ");
		console.sendMessage("|   |  |  |     |__   /  \\ ");
		console.sendMessage("===========================");
	}

	@Override
	public void onEnable() {
		check_config();
		if (!enable) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		printHelloInConsole();
		event_Listener = new Event_listener(
				unDroppableAfterDeathLore, blockedInInventory);
		Bukkit.getServer().getPluginManager().registerEvents(event_Listener, this);
		getCommand("UndroppableItem").setExecutor(new CommandReload(event_Listener));
		return;
	}
	
	@Override
	public void onDisable() {
		if (enable) {
			enable = false;
		}
		return;
	}

}
