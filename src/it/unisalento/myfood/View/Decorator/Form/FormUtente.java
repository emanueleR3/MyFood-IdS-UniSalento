package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;


public class FormUtente extends Form {

    private JTextField nome;
    private JTextField cognome;
    private JTextField email;
    private JTextField telefono;
    private JTextField dataDiNascita;
    private JTextField professione;
    private JTextField residenza;
    private JButton signUp = new JButton("Conferma dati");
    private JButton cancel = new JButton("Annulla");

    public FormUtente(){
        nome = new JTextField(20);
        cognome = new JTextField(20);
        email = new JTextField(20);
        telefono = new JTextField(20);
        dataDiNascita = new JTextField(20);
        professione = new JTextField(20);
        residenza = new JTextField(20);

        textFields.put("1. Nome", nome);
        textFields.put("2. Cognome", cognome);
        textFields.put("3. Email", email);
        textFields.put("4. Telefono", telefono);
        textFields.put("5. Data di nascita", dataDiNascita);
        textFields.put("6. Professione", professione);
        textFields.put("7. Residenza", residenza);

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
