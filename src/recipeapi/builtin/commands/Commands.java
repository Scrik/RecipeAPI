package recipeapi.builtin.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import recipeapi.RecipeAPI;

public class Commands implements CommandExecutor {

	private RecipeAPI plugin;

	public Commands(RecipeAPI plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only player can use it command");
			return false;
		}
		if (args.length == 1) {
			switch (args[0]) {
				case "remove": {
					break;
				}
				case "list": {
					break;
				}
				case "createshapeless": {
					Player player = (Player) sender;
					player.beginConversation(ShapelessRecipeConversation.create(plugin, player));
					break;
				}
				case "createshaped": {
					Player player = (Player) sender;
					player.beginConversation(ShapedRecipeConversation.create(plugin, player));
					break;
				}
			}
			return true;
		}
		return true;
	}

}
