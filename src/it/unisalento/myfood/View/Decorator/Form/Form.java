package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public abstract class Form {
    TreeMap<String, JTextField> textFields = new TreeMap();

    public TreeMap<String, JTextField> getTextFields(){
        return textFields;
    }

    ArrayList<JButton> buttons = new ArrayList<>();

    public ArrayList<JButton> getButtons (){
        return buttons;
    }

    ArrayList<JRadioButton> radioButtons = new ArrayList<>();

    public ArrayList<JRadioButton> getRadioButtons (){
        return radioButtons;
    }


}
