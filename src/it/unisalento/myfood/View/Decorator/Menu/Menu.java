package it.unisalento.myfood.View.Decorator.Menu;

import javax.swing.*;
import java.util.ArrayList;

public abstract class Menu {
    ArrayList<JMenuItem> items = new ArrayList<>();

    JMenu jMenu = new JMenu();

    public ArrayList<JMenuItem> getItems() {
        return items;
    }

    public JMenu getJMenu() {
        return jMenu;
    }
}
