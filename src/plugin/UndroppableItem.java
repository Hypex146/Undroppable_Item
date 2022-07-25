package plugin;
import java.util.logging.Level;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import plugin.commands.CommandReload;
import plugin.commands.CommandSetUndMode;
import plugin.listeners.EventListener;
import plugin.utilities.LogLevel;
import plugin.utilities.UIConfigurator;
import plugin.utilities.UILogger;

public class UndroppableItem extends JavaPlugin {
	private final String plugin_name_ = "Undroppable_Item";
	private FileConfiguration config_;
	private UIConfigurator configurator_;
	private UILogger logger_;
	// configuration
	private boolean enable_;
	private String partly_undroppable_tag_;
	private String fully_undroppable_tag_;
	// const
	public final NamespacedKey undroppable_mod_key_ = new NamespacedKey(this, "undroppable_mod_key");
	
	public UndroppableItem() {
		enable_ = true;
		config_ = null;
		configurator_=  new UIConfigurator(this);
		logger_ = new UILogger(this, LogLevel.STANDART);
	}
	
	public String getPluginName() {
		return plugin_name_;
	}
	
	public boolean isEnable() {
		return enable_;
	}
	
	public UIConfigurator getUIConfigurator() {
		return configurator_;
	}
	
	public UILogger getUILogger() {
		return logger_;
	}
	
	public String getPartlyUndroppableTag() {
		return partly_undroppable_tag_;
	}
	
	public String getFullyUndroppableTag() {
		return fully_undroppable_tag_;
	}
	
	public void loadConfig() {
		logger_.log(LogLevel.DEBUG, Level.INFO, "Подгружаем конфиг");
		reloadConfig();
		config_ = getConfig();
		enable_ = configurator_.getBoolean(config_, "enable", true);
		LogLevel log_level = LogLevel.toEnum(configurator_.getString(config_, 
				"log_level", LogLevel.STANDART.toString()));
		if (log_level == null) {
			logger_.log(LogLevel.DEBUG, Level.WARNING, "Неверно выставлено поле \"log_level\" в конфиге");
			configurator_.setString(config_, "log_level", LogLevel.STANDART.toString());
			logger_.log(LogLevel.DEBUG, Level.WARNING, "Поле \"log_level\" установлено по умолчанию");
			log_level = LogLevel.STANDART;
		}
		logger_.setLogLevel(log_level);
		partly_undroppable_tag_ = configurator_.getString(config_, "partly_undroppable_tag", "partly_undroppable");
		fully_undroppable_tag_ = configurator_.getString(config_, "fully_undroppable_tag", "fully_undroppable");
		logger_.log(LogLevel.DEBUG, Level.INFO, "Сохраняем конфиг");
		saveConfig();
	}
	
	public boolean checkEnableStatus() {
		logger_.log(LogLevel.DEBUG, Level.INFO, "Проверяем не отключён ли плагин в конфиге");
		if (enable_ == true) { 
			logger_.log(LogLevel.DEBUG, Level.INFO, "Плагин не отключён в конфиге");
			return true; 
			}
		logger_.log(LogLevel.DEBUG, Level.INFO, "Плагин отключён в конфиге");
		getServer().getPluginManager().disablePlugin(this);
		logger_.log(LogLevel.STANDART, Level.INFO, "Плагин был отключён");
		return false;
	}
	
	private void printGreetingInConsole() {
		String greeting = "\n===========================\n"
				+ "|   |     ___   ___        \n"
				+ "|   |\\   /|  |  |     \\  / \n"
				+ "|===| \\ / |__|  |__    \\/  \n"
				+ "|   |  |  |     |      /\\  \n"
				+ "|   |  |  |     |__   /  \\ \n"
				+ "===========================";
		logger_.log(LogLevel.STANDART, Level.INFO, greeting);
	}
	
	@Override
	public void onEnable() {
		loadConfig();
		if (!checkEnableStatus()) { return; }
		printGreetingInConsole();
		getCommand("undroppable_item").setExecutor(new CommandReload(this));
		getCommand("set_und_mode").setExecutor(new CommandSetUndMode(this));
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
	}


}
