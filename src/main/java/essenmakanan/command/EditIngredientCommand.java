package essenmakanan.command;

import essenmakanan.exception.EssenFormatException;
import essenmakanan.exception.EssenInvalidQuantityException;
import essenmakanan.ingredient.Ingredient;
import essenmakanan.ingredient.IngredientList;

public class EditIngredientCommand extends Command {
    private String editDetails;
    private IngredientList ingredients;

    public EditIngredientCommand(String input, IngredientList ingredients)  {
        super();
        this.editDetails = input;
        this.ingredients = ingredients;
    }

    @Override
    public void executeCommand() {
        Ingredient existingIngredient;

        this.editDetails = this.editDetails.replace("i/", "");

        String[] splitDetails = this.editDetails.split(" ");
        String ingredientName = splitDetails[0];

        existingIngredient = ingredients.getIngredient(ingredientName);

        assert existingIngredient.getName().equals(ingredientName)
                : "Selected ingredient does not have matching name.";

        if (existingIngredient == null) {
            System.out.println("Ingredient not found!");
        } else {
            try {
                ingredients.editIngredient(existingIngredient, splitDetails);
            } catch (EssenFormatException e) {
                e.handleException();
            }
        }

    }
}
