package it.unisalento.myfood.View.Panels;

import it.unisalento.myfood.View.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.View.Decorator.Icon.TableIconDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.LoginKeyListener;
import it.unisalento.myfood.Listener.LoginListener;

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

    private JPanel grid = new JPanel();
    private JPanel firstRow = new JPanel();
    private JPanel secondRow = new JPanel();
    private JPanel thirdRow = new JPanel();
    private JPanel fourthRow = new JPanel();
    private JPanel fifthRow = new JPanel();
    private JPanel emptyRow1 = new JPanel();
    private JPanel emptyRow2 = new JPanel();
    private LoginKeyListener loginKeyListener;


    public LoginPanel(Frame frame){
        this.frame = frame;
        setLayout(new BorderLayout());
        loginKeyListener = new LoginKeyListener(this);

        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new FlowLayout());
        pnlNorth.setBackground(Color.ORANGE);

        JPanel gridNorth = new JPanel(new GridLayout(0, 1));

        OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/icone_app/icona_myfood.png");
        TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
        JLabel lblFoto = new JLabel(tableIconDecorator.getImageIcon());
        JPanel pnlFoto = new JPanel(new FlowLayout());
        pnlFoto.setBackground(Color.ORANGE);
        pnlFoto.add(lblFoto);
        gridNorth.add(pnlFoto);

        JPanel emptyRowTitle = new JPanel(new FlowLayout());
        emptyRowTitle.setBackground(Color.ORANGE);
        gridNorth.add(emptyRowTitle);

        JPanel rowTitle = new JPanel(new FlowLayout());
        rowTitle.setBackground(Color.ORANGE);
        TextLabel title = new TextLabel("Login");
        TitleTextLabelDecorator titleTextLabel = new TitleTextLabelDecorator(title);
        rowTitle.add(titleTextLabel.getLabel());

        gridNorth.add(rowTitle);

        pnlNorth.add(gridNorth);
        add(pnlNorth, BorderLayout.NORTH);

        firstRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        firstRow.setBackground(Color.ORANGE);
        secondRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        secondRow.setBackground(Color.ORANGE);
        thirdRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        thirdRow.setBackground(Color.ORANGE);
        fourthRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        fourthRow.setBackground(Color.ORANGE);
        fifthRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        fifthRow.setBackground(Color.ORANGE);
        emptyRow1.setLayout(new FlowLayout(FlowLayout.CENTER));
        emptyRow1.setBackground(Color.ORANGE);
        emptyRow2.setLayout(new FlowLayout(FlowLayout.CENTER));
        emptyRow2.setBackground(Color.ORANGE);
        grid.setLayout(new GridLayout(0, 1));

        mostraPassword.setBackground(Color.ORANGE);

        mostraPassword.addActionListener(e -> {
            if (mostraPassword.isSelected()) {
                password.setEchoChar((char) 0);
            } else {
                password.setEchoChar('•');
            }
        });

        firstRow.add(new JLabel("Email: "));
        firstRow.add(email);
        firstRow.add(new JLabel("Password: "));
        firstRow.add(password);
        firstRow.add(mostraPassword);

        login.setPreferredSize(new Dimension(180, 25));
        secondRow.add(login);

        thirdRow.add(new JLabel("Non sei ancora registrato?"));
        thirdRow.add(signUp);
        fourthRow.add(new JLabel("Continua come guest"));
        fourthRow.add(guest);

        fifthRow.add(new JLabel("Informazioni sul software "));
        about.setPreferredSize(new Dimension(80, 25));
        fifthRow.add(about);

        grid.add(firstRow);
        grid.add(secondRow);
        grid.add(emptyRow1);
        grid.add(thirdRow);
        grid.add(fourthRow);
        grid.add(emptyRow2);
        grid.add(fifthRow);


        JPanel pnlCenter = new JPanel(new FlowLayout());
        pnlCenter.setBackground(Color.ORANGE);
        pnlCenter.add(grid);

        add(pnlCenter, BorderLayout.CENTER);

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
