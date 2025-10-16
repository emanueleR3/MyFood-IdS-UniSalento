package it.unisalento.myfood.View.Decorator.Menu;

import javax.swing.*;
import java.util.ArrayList;

public class EditMenuDecorator extends MenuDecorator{

    private JMenuItem undo;
    private JMenuItem redo;
    public static final String UNDO_MENU = "UNDO_MENU";
    public static final String REDO_MENU = "REDO_MENU";

    public EditMenuDecorator(Menu menu) {
        this.menu = menu;

        undo = new JMenuItem("Annulla");
        undo.setActionCommand(UNDO_MENU);
        redo = new JMenuItem("Ripeti");
        redo.setActionCommand(REDO_MENU);

    }

    public ArrayList<JMenuItem> getMenuItems(){
        items.addAll(menu.getItems());

        items.add(undo);
        items.add(redo);

        return items;
    }

    public JMenu getJMenu(){
        jMenu = menu.getJMenu();
        jMenu.setText("Modifica");

        return jMenu;
    }

}
