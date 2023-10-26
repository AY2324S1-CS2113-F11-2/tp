package essenmakanan.parser;

import essenmakanan.exception.EssenMakananFormatException;
import essenmakanan.exception.EssenMakananOutOfRangeException;
import essenmakanan.ingredient.Ingredient;
import essenmakanan.ingredient.IngredientList;
import essenmakanan.ingredient.IngredientUnit;

public class IngredientParser {
    public static int getIngredientId(IngredientList ingredients, String input) throws EssenMakananOutOfRangeException {
        int index;

        if (input.matches("\\d+")) { //if input only contains numbers
            index = Integer.parseInt(input);
        } else {
            index = ingredients.indexOfIngredientByName(input);
        }

        if (index == -1) {
            throw new EssenMakananOutOfRangeException();
        }

        return index;
    }

    public static Ingredient parseIngredient(IngredientList ingredients, String inputDetail)
            throws EssenMakananFormatException {

        IngredientUnit ingredientUnit;

        inputDetail = inputDetail.replace("i/", "");

        String[] ingredientDetails = inputDetail.split(",");

        if (ingredientDetails.length != 3) {
            throw new EssenMakananFormatException();
        }

        String ingredientName = ingredientDetails[0].strip();

        String ingredientQuantity = ingredientDetails[1].strip();

        String ingredientUnitString = ingredientDetails[2].strip().toLowerCase();
        ingredientUnit = mapIngredientUnit(ingredientUnitString);

        Ingredient newIngredient = new Ingredient(ingredientName, ingredientQuantity, ingredientUnit);

        return newIngredient;
    }

    public static IngredientUnit mapIngredientUnit(String ingredientUnitString) throws EssenMakananFormatException {
        IngredientUnit ingredientUnit;
        // return("Valid ingredient units are: g, kg, ml, l, tsp, tbsp, cup, pcs");
        switch(ingredientUnitString) {
        case "g":
            ingredientUnit = IngredientUnit.GRAM;
            break;
        case "kg":
            ingredientUnit = IngredientUnit.KILOGRAM;
            break;
        case "ml":
            ingredientUnit = IngredientUnit.MILLILITER;
            break;
        case "l":
            ingredientUnit = IngredientUnit.LITER;
            break;
        case "tsp":
            ingredientUnit = IngredientUnit.TEASPOON;
            break;
        case "tbsp":
            ingredientUnit = IngredientUnit.TABLESPOON;
            break;
        case "cup":
            ingredientUnit = IngredientUnit.CUP;
            break;
        case "pc":
            ingredientUnit = IngredientUnit.PIECE;
            break;
        default:
            throw new EssenMakananFormatException();
        }

        return ingredientUnit;
    }
}
