package essenmakanan.exception;

import essenmakanan.ui.Ui;

public class EssenShortcutException extends Exception {

    public void handleException() {
        Ui.drawDivider();
        System.out.println("Shortcut cannot be created for a non-existing ingredient");
        Ui.drawDivider();
    }
}
