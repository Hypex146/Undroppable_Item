package plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import plugin.UndroppableItem;

public class CommandReload implements CommandExecutor {
	private UndroppableItem main_plugin_;
	
	public CommandReload (UndroppableItem main_plugin) {
		this.main_plugin_ = main_plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender || 
				sender.hasPermission("undroppable_item.admin") || sender.isOp()) {
			if (args.length != 1 || !args[0].equals("reload")) {
				sender.sendMessage("Ошибка синтаксиса!");
				return false;
			}
			main_plugin_.loadConfig();
			main_plugin_.checkEnableStatus();
			sender.sendMessage("Конфиг успешно перезагружен");
			return true;
		}
		sender.sendMessage("У вас нет прав для выполнения данной комманды!");
		return false;
	}

}
