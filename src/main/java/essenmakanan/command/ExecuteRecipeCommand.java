package essenmakanan.command;

import essenmakanan.exception.EssenFormatException;
import essenmakanan.exception.EssenNullInputException;
import essenmakanan.exception.EssenOutOfRangeException;
import essenmakanan.ingredient.Ingredient;
import essenmakanan.ingredient.IngredientList;
import essenmakanan.parser.RecipeParser;
import essenmakanan.recipe.Recipe;
import essenmakanan.recipe.RecipeIngredientList;
import essenmakanan.recipe.RecipeList;
import essenmakanan.ui.Ui;

public class ExecuteRecipeCommand extends Command {
    private String recipeTitleToStart;
    private RecipeList recipes;
    private IngredientList allIngredientsList;
    private CheckRecipeCommand checkRecipeCommand;
    private IngredientList recipeIngredients;


    public ExecuteRecipeCommand(IngredientList allIngredientsList, RecipeList recipes, String recipeTitleToStart) {
        super();
        this.recipeTitleToStart = recipeTitleToStart;
        this.recipes = recipes;
        this.allIngredientsList = allIngredientsList;
        this.checkRecipeCommand = new CheckRecipeCommand(recipeTitleToStart, recipes, allIngredientsList);
    }

    @Override
    public void executeCommand() {
        Recipe recipe = null;
        try {
            recipe = this.getRecipe();
        } catch (EssenNullInputException e) {
            e.handleException();
        }

        RecipeIngredientList recipeIngredients = recipe.getRecipeIngredients();
        if (checkRecipeCommand.allIngredientsReady(recipeIngredients)){
            assert recipe != null : "Recipe should not be null";
            updateAllIngredientQuantity(recipeIngredients);
            Ui.printExecuteRecipeSuccess(recipe.getTitle());
        } else {
            Ui.printExecuteRecipeFail(recipe.getTitle());
        }

    }

    public Recipe getRecipe() throws EssenNullInputException {
        try {
            if (recipeTitleToStart.isEmpty()) {
                System.out.println("Recipe Title is empty! Please enter valid title after \"execute\"");
                throw new EssenNullInputException();
            }

            int recipeIndex = RecipeParser.getRecipeIndex(recipes, recipeTitleToStart);
            Recipe recipe = recipes.getRecipe(recipeIndex);
            return recipe;
        } catch (EssenOutOfRangeException | EssenFormatException e) {
            e.handleException();
        }
        return null;
    }

    public void updateAllIngredientQuantity(RecipeIngredientList recipeIngredients) {
        for (Ingredient ingredient: recipeIngredients.getIngredients()) {
            try {
                // Decrease ingredient quantity in allIngredientsList
                ingredient.setQuantity(-ingredient.getQuantity());
                allIngredientsList.updateIngredient(ingredient);
            } catch (EssenFormatException e) {
                e.handleException();
            }
        }
    }

}
