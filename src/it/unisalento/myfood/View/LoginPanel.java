package it.unisalento.myfood.View;

import it.unisalento.myfood.View.Listener.LoginKeyListener;
import it.unisalento.myfood.View.Listener.LoginListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel{

    private Frame frame;

    private JTextField email = new JTextField(20);
    private JPasswordField password = new JPasswordField(20);
    private JButton login = new JButton("Login");
    private JButton about = new JButton("About");
    private JButton guest = new JButton("Visualizza catalogo");
    private JButton signUp = new JButton("Registrati");
    private JCheckBox mostraPassword = new JCheckBox("Mostra password");

    // TODO DA RIMUOVERE!!!
    //private JButton clienteAccessoTemporaneo = new JButton("CLIENTE EMANUELE TEMPORANEO");
    private JButton clienteAccessoTemporaneo = new JButton("ADMIN EMANUELE TEMPORANEO");

    private JPanel grid = new JPanel();
    private JPanel firstRow = new JPanel();
    private JPanel secondRow = new JPanel();
    private JPanel thirdRow = new JPanel();
    private JPanel fourthRow = new JPanel();
    private LoginKeyListener loginKeyListener;


    public LoginPanel(Frame frame){
        this.frame = frame;
        setLayout(new BorderLayout());
        loginKeyListener = new LoginKeyListener(this);

        firstRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        secondRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        thirdRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        fourthRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        grid.setLayout(new GridLayout(5, 1));

        // TODO: togliere da qua, ma non sto capendo come


        mostraPassword.addActionListener(e -> {
            if (mostraPassword.isSelected()) {
                password.setEchoChar((char) 0);
            } else {
                password.setEchoChar('•');  // sono impazzito per cercare questo carattere lms
            }
        });

        firstRow.add(new JLabel("Email: "));
        firstRow.add(email);
        firstRow.add(new JLabel("Password: "));
        firstRow.add(password);
        firstRow.add(mostraPassword);
        secondRow.add(login);

        // TODO RIMUOVERE
        secondRow.add(clienteAccessoTemporaneo);
        // ----------------------------

        secondRow.add(about);
        thirdRow.add(new JLabel("Non sei ancora registrato?"));
        thirdRow.add(signUp);
        fourthRow.add(new JLabel("Continua come guest"));
        fourthRow.add(guest);

        grid.add(firstRow);
        grid.add(secondRow);
        grid.add(thirdRow);
        grid.add(fourthRow);

        //TODO: mettere centrale, se lo metto riempie tutta la finestra

        add(grid, BorderLayout.NORTH);

        ActionListener loginListener = new LoginListener(this, frame);
        login.addActionListener(loginListener);

        about.addActionListener(loginListener);
        signUp.addActionListener(loginListener);
        guest.addActionListener(loginListener);

        login.setActionCommand(LoginListener.LOGIN_BTN);
        about.setActionCommand(LoginListener.ABOUT_BTN);
        signUp.setActionCommand(LoginListener.SIGNUP_BTN);
        guest.setActionCommand(LoginListener.GUEST_BTN);

        password.addKeyListener(loginKeyListener);

        // TODO RIMUOVERE
        clienteAccessoTemporaneo.addActionListener(loginListener);
        clienteAccessoTemporaneo.setActionCommand(LoginListener.CLIENTE_TEMPORANEO);
        // ----------------------------
    }

    public String getEmail() {
        return email.getText();
    }

    public String getPassword() {
        return new String(password.getPassword());
    }

    public void emptyFields(){ //svuota i campi quando viene fatto il logout
        email.setText("");
        password.setText("");
    }

    public void clickLoginButton(){
        login.doClick();
    }


}
