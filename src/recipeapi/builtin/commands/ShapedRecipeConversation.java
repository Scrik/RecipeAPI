package recipeapi.builtin.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import recipeapi.RecipeAPI;
import recipeapi.api.recipes.CustomRecipe;
import recipeapi.api.recipes.Ingredient;
import recipeapi.api.recipes.ShapedRecipe;
import recipeapi.api.registry.RecipeRegistry;
import recipeapi.builtin.RecipesStorage;

public class ShapedRecipeConversation {

	public static Conversation create(final RecipeAPI plugin, Player player) {
		ConversationFactory factory = new ConversationFactory(plugin);
		HashMap<Object, Object> initialdata = new HashMap<>();
		initialdata.put("builder", ShapedRecipe.builder());
		factory
		.withEscapeSequence("cancel")
		.withInitialSessionData(initialdata)
		.withFirstPrompt(new RecipeCreatePromt())
		.withPrefix(new ConversationPrefix() {
			@Override
			public String getPrefix(ConversationContext ctx) {
				return ChatColor.YELLOW + "[" + plugin.getName() + "] ";
			}
		});
		return factory.buildConversation(player);
	}

	static ItemStack getItemInHand(Conversable conv) {
		return ((Player) conv).getItemInHand();
	}

	static ShapedRecipe.Builder getBuilder(ConversationContext ctx) {
		return (ShapedRecipe.Builder) ctx.getSessionData("builder");
	}

	private static class RecipeCreatePromt extends ValidatingPrompt {

		@Override
		public String getPromptText(ConversationContext ctx) {
			return "Select itemstack you want to use as an result and type use, or cancel to cancel creating recipe";
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext ctx, String input) {
			getBuilder(ctx).setReuslt(getItemInHand(ctx.getForWhom()));
			return new RecipeSetShapePromt();
		}

		@Override
		protected boolean isInputValid(ConversationContext ctx, String input) {
			return input.equalsIgnoreCase("use"); 
		}

	}

	private static class RecipeSetShapePromt extends ValidatingPrompt {

		@Override
		public String getPromptText(ConversationContext ctx) {
			return "Set shape which you want to use (bukkit-api like setup), or cancel to cancel creating recipe";
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext ctx, String input) {
			getBuilder(ctx).setShape(input.split("[ ]"));
			return new RecipeSetIngredientPromt();
		}

		@Override
		protected boolean isInputValid(ConversationContext ctx, String input) {
			String[] split = input.split("[ ]");
			if (split.length > 3) {
				return false;
			}
			for (String string : split) {
				if (string.length() > 3) {
					return false;
				}
			}
			return true;
		}

	}

	private static class RecipeSetIngredientPromt extends ValidatingPrompt {

		@Override
		public String getPromptText(ConversationContext ctx) {
			return "Select item you want to use and type use or wuse and it's character in a shape separated by a space, finish to finish creating recipe. or cancel to cancel creating recipe";
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext ctx, String input) {
			switch (input.toLowerCase()) {
				case "finish": {
					CustomRecipe recipe = getBuilder(ctx).build();
					RecipesStorage.getInstance().addRecipe(recipe);
					RecipeRegistry.register(recipe);
					ctx.getForWhom().sendRawMessage(ChatColor.YELLOW + "Recipe created and registerd");
					return END_OF_CONVERSATION;
				}
				default: {
					String[] split = input.split("[ ]");
					if (split.length != 2) {
						return this;
					}
					char character = split[1].charAt(0);
					switch (split[0].toLowerCase()) {
						case "use": {
							getBuilder(ctx).setIngredient(character, Ingredient.fromItemStack(getItemInHand(ctx.getForWhom()), false));
							return this;
						}
						case "wuse": {
							getBuilder(ctx).setIngredient(character, Ingredient.fromItemStack(getItemInHand(ctx.getForWhom()), true));
							return this;
						}
						default: {
							return this;
						}
					}
				}
			}
		}

		@Override
		protected boolean isInputValid(ConversationContext ctx, String input) {
			switch (input.toLowerCase()) {
				case "finish": {
					return getBuilder(ctx).hasIngredients();
				}
				default: {
					return (input.startsWith("use ") && input.length() == 5) || (input.startsWith("wuse ") && input.length() == 6);
				}
			}
		}
		
	}

}
