package essenmakanan.parser;

import essenmakanan.exception.EssenEditShortcutException;
import essenmakanan.exception.EssenFormatException;
import essenmakanan.exception.EssenShortcutException;
import essenmakanan.ingredient.IngredientList;
import essenmakanan.shortcut.Shortcut;
import essenmakanan.shortcut.ShortcutList;
import essenmakanan.ui.Ui;

public class ShortcutParser {

    private static boolean checkForValidQuantity(double quantity) {
        return quantity < 0 || Math.ceil(quantity) < 0;
    }

    public static Shortcut parseShortcut(IngredientList ingredients, String input) throws EssenFormatException
            , EssenShortcutException, NumberFormatException {
        input = input.replace("sc/", "");
        String[] shortcutDetails = input.split(",");

        if (shortcutDetails.length != 2 || shortcutDetails[0].isBlank()) {
            throw new EssenFormatException();
        }

        String ingredientName = shortcutDetails[0].strip();

        if (!ingredients.exist(ingredientName)) {
            throw new EssenShortcutException();
        }

        double quantity = Double.parseDouble(shortcutDetails[1].strip());

        if (checkForValidQuantity(quantity)) {
            throw new NumberFormatException();
        }

        return new Shortcut(ingredientName, quantity);
    }

    public static int getShortcutIndex(ShortcutList shortcuts, String input) {
        int index;

        if (input.matches("\\d+")) { //if input only contains numbers
            index = Integer.parseInt(input) - 1;
        } else {
            index = shortcuts.getIndex(input);
        }

        return index;
    }


    private static void editShortcutName(Shortcut shortcut, IngredientList ingredients, String editDetail
            , boolean hasEditName) throws EssenShortcutException, EssenEditShortcutException {
        String newName = editDetail.substring(2).strip();
        String oldName = shortcut.getIngredientName();

        if (!ingredients.exist(newName)) {
            throw new EssenShortcutException();
        }

        if (hasEditName) {
            throw new EssenEditShortcutException("usage");
        }

        if (newName.equals(oldName)) {
            throw new EssenEditShortcutException("same name");
        }

        Ui.printEditShortcutName(shortcut.getIngredientName(), newName);
        shortcut.setIngredientName(newName);
    }

    private static void editShortcutQuantity(Shortcut shortcut, String editDetail, boolean hasEditQuantity)
            throws EssenEditShortcutException, NumberFormatException {
        double newQuantity = Double.parseDouble(editDetail.substring(2).strip());

        if (hasEditQuantity) {
            throw new EssenEditShortcutException("usage");
        }

        if (checkForValidQuantity(newQuantity)) {
            throw new NumberFormatException();
        }

        if (newQuantity == shortcut.getQuantity()) {
            throw new EssenEditShortcutException("same quantity");
        }

        Ui.printEditShortcutQuantity(shortcut.getQuantity(), newQuantity);
        shortcut.setQuantity(newQuantity);
    }

    public static void editShortcut(Shortcut shortcut, IngredientList ingredients, String[] editDetails)
            throws EssenFormatException, EssenShortcutException, EssenEditShortcutException, NumberFormatException {
        boolean hasEditName = false;
        boolean hasEditQuantity = false;

        for (int i = 1; i < editDetails.length; i++) {
            String flag = editDetails[i].substring(0, 2).strip();

            switch (flag) {
            case "n/":
                editShortcutName(shortcut, ingredients, editDetails[i], hasEditName);
                hasEditName = true;
                break;
            case "q/":
                editShortcutQuantity(shortcut, editDetails[i], hasEditQuantity);
                hasEditQuantity = true;
                break;
            default:
                throw new EssenFormatException();
            }
        }
    }

    public static String convertToString(Shortcut shortcut) {
        return shortcut.getIngredientName() + " | " + shortcut.getQuantity();
    }
}
