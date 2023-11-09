package essenmakanan.command;

import essenmakanan.exception.EssenFormatException;
import essenmakanan.ingredient.Ingredient;
import essenmakanan.ingredient.IngredientList;
import essenmakanan.parser.IngredientParser;
import essenmakanan.ui.Ui;

public class AddIngredientCommand extends Command {
    private String toAdd;
    private IngredientList ingredients;

    public AddIngredientCommand(String toAdd, IngredientList ingredients) {
        super();
        this.toAdd = toAdd;
        this.ingredients = ingredients;
    }

    @Override
    public void executeCommand() {
        String[] allIngredients = toAdd.split("i/");

        for (String ingredient : allIngredients) {
            if (ingredient.isEmpty()) {
                continue;
            }

            Ingredient newIngredient;
            try {
                newIngredient = IngredientParser.parseIngredient(ingredient);
                ingredients.addIngredient(newIngredient);
            } catch (EssenFormatException e) {
                e.handleException();
            }
        }

    }

}

