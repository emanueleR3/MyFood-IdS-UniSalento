package it.unisalento.myfood.View.Panels;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.*;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.SignUp.AmministratoreSignUpListener;
import it.unisalento.myfood.Listener.SignUp.ClienteSignUpListener;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class SignUpPanel extends JPanel {

    private JPanel grid = new JPanel();
    private JPanel buttons = new JPanel();
    private JButton signUp = new JButton("Registrati");
    private JButton cancel = new JButton("Annulla");
    private FormDecorator formDecorated;
    private JLabel residenza;

    private static final UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
    //questo panel può essere usato sia dal cliente per registrarsi che dall'amministratore per aggoiungere lavoratori e amministratori

    public SignUpPanel(Frame frame) {
        setLayout(new BorderLayout());
        GridLayout gridLayout = new GridLayout(8, 2);

        grid.setLayout(gridLayout);
        buttons.setLayout(new FlowLayout());

        if (UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID) == null) {  //nessun utente in sessione: caso d'uso: guest si registra

            FormUtente form = new FormUtente();
            formDecorated = new FormClienteDecorator(form);

            Iterator<Map.Entry<String, JTextField>> iterator = formDecorated.getTextFields().entrySet().iterator();
            ClienteSignUpListener signUpListener = new ClienteSignUpListener((FormClienteDecorator) formDecorated, frame);

            while (iterator.hasNext()) {  
                Map.Entry<String, JTextField> entry = iterator.next();
                grid.add(new JLabel(entry.getKey()));
                grid.add(entry.getValue());
            }
            for(JButton button : formDecorated.getButtons()){
                buttons.add(button);
                button.addActionListener(signUpListener);
            }
        }

       else if (utenteBusiness.isLoggedAmministratore()) {  //amministratore in sessione: caso d'uso: amministratore utenti

            FormUtente form = new FormUtente();
            formDecorated = new FormAmministratoreDecorator(form);

            AmministratoreSignUpListener amministratoreSignUpListener = new AmministratoreSignUpListener((FormAmministratoreDecorator) formDecorated, frame, this);


            Iterator<Map.Entry<String, JTextField>> iterator = formDecorated.getTextFields().entrySet().iterator();

            residenza = new JLabel();

            while (iterator.hasNext()) {
                Map.Entry<String, JTextField> entry = iterator.next();
                if("7. Residenza".equals(entry.getKey())){
                    residenza.setText(entry.getKey());
                    grid.add(residenza);
                    residenza.setVisible(false);
                }

                else
                grid.add(new JLabel(entry.getKey()));

                grid.add(entry.getValue());
            }



            grid.add(new JLabel("Ruolo"));

            JPanel panel = new JPanel(new GridLayout(formDecorated.getRadioButtons().size(), 1));

            Iterator<JRadioButton> iterator1 = formDecorated.getRadioButtons().iterator();
            while (iterator1.hasNext()) {
                JRadioButton radioButton = iterator1.next();
                panel.add(radioButton);
                radioButton.addActionListener(amministratoreSignUpListener);
            }

            grid.add(panel);

            for (JButton button : formDecorated.getButtons()) {
                buttons.add(button);
                button.addActionListener(amministratoreSignUpListener);
            }

        }
       add(grid, BorderLayout.NORTH);
       add(buttons, BorderLayout.CENTER);

    }

    public void showResidenza(){
        residenza.setVisible(true);
        formDecorated.getTextFields().get("7. Residenza").setVisible(true);
    }

    public void hideResidenza(){
        residenza.setVisible(false);
        formDecorated.getTextFields().get("7. Residenza").setVisible(false);
    }


    public void emptyFields(){
        Iterator<Map.Entry<String, JTextField>> iterator = formDecorated.getTextFields().entrySet().iterator();

        while (iterator.hasNext()){
             iterator.next().getValue().setText("");
        }
    }
}
