package recipeapi.builtin.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import recipeapi.RecipeAPI;
import recipeapi.api.recipes.CustomRecipe;
import recipeapi.builtin.RecipesStorage;

public class Commands implements CommandExecutor {

	private RecipeAPI plugin;

	public Commands(RecipeAPI plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only player can use it command");
			return true;
		}
		Player player = (Player) sender;
		if (!sender.hasPermission("recipeapi.admin")) {
			sender.sendMessage(ChatColor.RED + "No permission");
			return true;
		}
		if (args.length >= 1) {
			switch (args[0]) {
				case "remove": {
					if (args.length == 2) {
						RecipesStorage.getInstance().removeRecipe(RecipesStorage.getInstance().getRecipes().get(Integer.parseInt(args[1])));
					}
					break;
				}
				case "list": {
					int count = 0;
					for (CustomRecipe recipe : RecipesStorage.getInstance().getRecipes()) {
						player.sendMessage(count+": "+recipe);
						count++;
					}
					break;
				}
				case "createshapeless": {
					player.beginConversation(ShapelessRecipeConversation.create(plugin, player));
					break;
				}
				case "createshaped": {
					player.beginConversation(ShapedRecipeConversation.create(plugin, player));
					break;
				}
			}
			return true;
		}
		return true;
	}

}
