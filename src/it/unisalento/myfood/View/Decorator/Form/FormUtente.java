package it.unisalento.myfood.View.Decorator.Form;

import it.unisalento.myfood.View.Frame;

import javax.swing.*;


public class FormUtente extends Form {

    private JTextField nome;
    private JTextField cognome;
    private JTextField email;
    private JTextField telefono;
    private JTextField dataDiNascita;
    private JTextField professione;
    private JButton signUp = new JButton("Registrati");
    private JButton cancel = new JButton("Annulla");
    private Frame frame;

    public FormUtente(Frame frame){
        this.frame = frame;
        nome = new JTextField();
        cognome = new JTextField();
        email = new JTextField();
        telefono = new JTextField();
        dataDiNascita = new JTextField();
        professione = new JTextField();

        textFields.put("1. Nome", nome);
        textFields.put("2. Cognome", cognome);
        textFields.put("3. Email", email);
        textFields.put("4. Telefono", telefono);
        textFields.put("5. Data di nascita", dataDiNascita);
        textFields.put("6. Professione", professione);

        buttons.add(signUp);
        buttons.add(cancel);

    }


    public JButton getSignUp() {
        return signUp;
    }

    public JButton getCancel() {
        return cancel;
    }

}
