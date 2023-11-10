package essenmakanan.recipe;

import essenmakanan.exception.EssenFormatException;
import essenmakanan.ui.Ui;
import essenmakanan.ingredient.Ingredient;

import java.util.ArrayList;

public class RecipeList {
    private ArrayList<essenmakanan.recipe.Recipe> recipes;

    public RecipeList() {
        recipes = new ArrayList<>();
    }

    public RecipeList(ArrayList<essenmakanan.recipe.Recipe> recipes) {
        this.recipes = recipes;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        assert getRecipe(recipes.size() - 1).getTitle().equals(recipe.getTitle())
                : "Recipe is not successfully added into the list.";
    }

    public void addRecipe(String title, String[] steps) {
        recipes.add(new essenmakanan.recipe.Recipe(title, steps));
    }

    public void deleteRecipe(int index) {
        Ui.printDeleteRecipeSuccess(recipes.get(index).getTitle());
        recipes.remove(index);
    }

    public Recipe getRecipe(int index) {
        assert recipeExist(index) : "Index is out of bounds";
        return recipes.get(index);
    }

    public Recipe getRecipe(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getTitle().equals(name)) {
                return recipe;
            }
        }
        return null;
    }

    public int getIndexOfRecipe(String recipeTitle) {
        int i = 0;
        for (essenmakanan.recipe.Recipe recipe : recipes) {
            if (recipe.getTitle().equalsIgnoreCase(recipeTitle)) {
                return i;
            }
            i++;
        }
        return -1;
    }
    public boolean recipeExist(int index) {
        if (index >= 0 && index < recipes.size()) {
            return true;
        }
        return false;
    }


    public void listRecipeTitles() {
        int count = 1;

        for (essenmakanan.recipe.Recipe recipe : recipes) {
            assert recipes.get(count - 1).getTitle().equals(recipe.getTitle())
                    : "Title is not matching with the current index";

            System.out.println(count + ". " + recipe);
            count++;
        }
    }

    private static void listRecipeSteps(essenmakanan.recipe.Recipe recipe) {
        recipe.viewTimeLine();
    }

    private static void listRecipeIngredients(essenmakanan.recipe.Recipe recipe) {
        RecipeIngredientList ingredients = recipe.getRecipeIngredients();

        // if ingredient list is empty
        if (ingredients == null) {
            System.out.println("\tNo ingredients needed!");
            return;
        }

        int count = 1;
        for (Ingredient ingredient : ingredients.getIngredients()) {
            assert ingredients.getIngredientByIndex(count - 1).equals(ingredient)
                    : "Ingredient is not matching with the current index";

            System.out.println("\t" + count + ") " + ingredient);
            count++;
        }
    }

    public void viewRecipe(int index) {
        Ui.drawDivider();

        assert recipeExist(index) : "Index is out of bounds";
        Recipe recipe = recipes.get(index);
        System.out.println("To make: [" + recipe.getTitle().toUpperCase() + "]");

        System.out.println("Ingredients needed: ");
        listRecipeIngredients(recipe);
        System.out.println("Steps to follow: ");
        listRecipeSteps(recipe);
    }

    public void viewRecipe(String title) {
        Ui.drawDivider();
        essenmakanan.recipe.Recipe recipe = recipes.stream()
            .filter(recipe1 -> recipe1.getTitle().equals(title))
            .findFirst()
            .orElse(null);
        assert getRecipe(title) == recipe : "Recipe does not exist";
        listRecipeSteps(recipe);
    }

    public void editRecipe(Recipe existingRecipe, String[] editDetails) throws EssenFormatException {
        for (int i = 1; i < editDetails.length; i++) {
            // get flag of input to know which field to edit
            String flag = editDetails[i].substring(0, 2);

            assert (flag != null) : "Invalid flag";
            switch (flag) {
            case "n/":
                String newName = editDetails[i].substring(2);
                Ui.printEditRecipeNameSuccess(existingRecipe.getTitle(), newName);
                existingRecipe.setTitle(newName);
                break;
            case "s/":
                String[] stepDetails = editDetails[i].substring(2).split(",");
                if (stepDetails.length <= 1) {
                    System.out.println("The description is empty! You have to provide details to edit this step!");
                    return;
                }
                int stepIndex = Integer.parseInt(stepDetails[0])-1;
                Step existingStep = existingRecipe.getRecipeStepByIndex(stepIndex);
                String newStep = stepDetails[1];

                Ui.printEditRecipeStepSuccess(existingStep.getDescription(), newStep);
                existingStep.setDescription(newStep);
                break;

            default:
                throw new EssenFormatException();
            }
        }

    }

    public boolean isEmpty() {
        return recipes.isEmpty();
    }
}
