package plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import plugin.UndroppableItem;

public class CommandSetUndMode implements CommandExecutor {
	private UndroppableItem main_plugin_;
	
	public CommandSetUndMode(UndroppableItem main_plugin) {
		main_plugin_ = main_plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Нельзя использовать команду отсюда!");
			return false;
		}
		if (sender.hasPermission("undroppable_item.admin") || sender.isOp()) {
			if (args.length == 1) {
				ItemStack item_stack = ((Player)sender).getInventory().getItemInMainHand();
				if (item_stack == null) {
					sender.sendMessage("Предмет должен находится в основной руке!");
					return false;
				}
				ItemMeta item_meta = item_stack.getItemMeta();
				if (args[0].equals("partly")) {
					item_meta.getPersistentDataContainer().set(main_plugin_.undroppable_mod_key_, 
							PersistentDataType.STRING, main_plugin_.getPartlyUndroppableTag());
					item_stack.setItemMeta(item_meta);
					sender.sendMessage("Тег успешно установлен!");
					return true;
				}
				if (args[0].equals("fully")) {
					item_meta.getPersistentDataContainer().set(main_plugin_.undroppable_mod_key_, 
							PersistentDataType.STRING, main_plugin_.getFullyUndroppableTag());
					item_stack.setItemMeta(item_meta);
					sender.sendMessage("Тег успешно установлен!");
					return true;
				}
				sender.sendMessage("Неверное значение параметра! Возможны: partly и fully.");
				return false;
			}
			sender.sendMessage("Неверный синтаксис! После комманды укажите один из тегов: partly или fully.");
			return false;
		}
		sender.sendMessage("У вас нет прав на выполнение данной команды!");
		return false;
	}

}
