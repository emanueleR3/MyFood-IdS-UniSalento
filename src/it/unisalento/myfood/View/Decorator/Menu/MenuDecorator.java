package it.unisalento.myfood.View.Decorator.Menu;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDecorator extends Menu {
    Menu menu;
    JMenu jMenu;

    public ArrayList<JMenuItem> getMenuItems(){
        return menu.getItems();
    }

    public JMenu getJMenu() {
        return jMenu;
    }
}
