package it.unisalento.myfood.View;

import it.unisalento.myfood.View.Listener.ChangePasswordListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChangePasswordPanel extends JPanel {

    private Frame frame;

    private JLabel messaggio = new JLabel("Sei al tuo primo accesso! Cambia password per accedere al catalogo!");
    private JPasswordField password = new JPasswordField(20);
    private JButton btnCambiaPassword = new JButton("Salva nuova password");
    private JCheckBox mostraPassword = new JCheckBox("Mostra password");

    private JPanel grid = new JPanel();
    private JPanel firstRow = new JPanel();
    private JPanel secondRow = new JPanel();
    private JPanel thirdRow = new JPanel();

    public ChangePasswordPanel(Frame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());

        firstRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        secondRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        thirdRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        grid.setLayout(new GridLayout(3, 1));

        mostraPassword.addActionListener(e -> {
            if (mostraPassword.isSelected()) {
                password.setEchoChar((char) 0);
            } else {
                password.setEchoChar('•');  // sono impazzito per cercare questo carattere lms
            }
        });

        firstRow.add(messaggio);
        secondRow.add(new JLabel("Password: "));
        secondRow.add(password);
        secondRow.add(mostraPassword);
        thirdRow.add(btnCambiaPassword);

        grid.add(firstRow);
        grid.add(secondRow);
        grid.add(thirdRow);

        add(grid, BorderLayout.NORTH);

        ActionListener changePasswordListener = new ChangePasswordListener(this, frame);
        btnCambiaPassword.addActionListener(changePasswordListener);
        btnCambiaPassword.setActionCommand(ChangePasswordListener.CHANGE_PASSWORD_BTN);
    }

    public void emptyFields() {
        this.password.setText("");
    }

    public String getPassword() {
        return new String(password.getPassword());
    }
}
