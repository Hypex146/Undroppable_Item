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
			sender.sendMessage("������ ������������ ������� ������!");
			return false;
		}
		if (sender.hasPermission("undroppable_item.admin") || sender.isOp()) {
			if (args.length == 1) {
				ItemStack item_stack = ((Player)sender).getInventory().getItemInMainHand();
				if (item_stack == null) {
					sender.sendMessage("������� ������ ��������� � �������� ����!");
					return false;
				}
				ItemMeta item_meta = item_stack.getItemMeta();
				if (args[0].equals("partly")) {
					item_meta.getPersistentDataContainer().set(main_plugin_.undroppable_mod_key_, 
							PersistentDataType.STRING, main_plugin_.getPartlyUndroppableTag());
					item_stack.setItemMeta(item_meta);
					sender.sendMessage("��� ������� ����������!");
					return true;
				}
				if (args[0].equals("fully")) {
					item_meta.getPersistentDataContainer().set(main_plugin_.undroppable_mod_key_, 
							PersistentDataType.STRING, main_plugin_.getFullyUndroppableTag());
					item_stack.setItemMeta(item_meta);
					sender.sendMessage("��� ������� ����������!");
					return true;
				}
				sender.sendMessage("�������� �������� ���������! ��������: partly � fully.");
				return false;
			}
			sender.sendMessage("�������� ���������! ����� �������� ������� ���� �� �����: partly ��� fully.");
			return false;
		}
		sender.sendMessage("� ��� ��� ���� �� ���������� ������ �������!");
		return false;
	}

}
