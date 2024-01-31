package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormDecorator extends Form{
    Form form;

    public TreeMap<String, JTextField> getTextFields() {
        return this.form.getTextFields();
    }

    public ArrayList<JButton> getButtons(){
        return this.form.getButtons();
    }

    public ArrayList<JRadioButton> getRadioButtons(){
        return this.form.getRadioButtons();
    }

    public Form getForm(){
        return form;
    }
}
