package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.Form;
import it.unisalento.myfood.View.Decorator.Form.FormClienteDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormPersonaleDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormUtente;
import it.unisalento.myfood.View.Listener.ClienteSignUpListener;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class SignUpPanel extends JPanel {

    private JPanel grid = new JPanel();
    private JPanel buttons = new JPanel();
    private JButton signUp = new JButton("Registrati");
    private JButton cancel = new JButton("Annulla");
    private FormClienteDecorator formCliente;
    private Form formPersonale;

    private static final UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
    //questo panel può essere usato sia dal cliente per registrarsi che dall'amministratore per aggoiungere lavoratori e amministratori

    public SignUpPanel(Frame frame) {
        setLayout(new BorderLayout());
        GridLayout gridLayout = new GridLayout(8, 2);

        grid.setLayout(gridLayout);
        buttons.setLayout(new FlowLayout());

        if (UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER) == null) {  //nessun utente in sessione: caso d'uso: guest si registra

            FormUtente formUtente = new FormUtente(frame);
            formCliente = new FormClienteDecorator(formUtente, frame);
            // TODO no viene mai aggiornato il campo residenza

            Iterator<Map.Entry<String, JTextField>> iterator = formCliente.getTextFields().entrySet().iterator();
            ClienteSignUpListener signUpListener = new ClienteSignUpListener(formCliente, frame);

            while (iterator.hasNext()) {  
                Map.Entry<String, JTextField> entry = iterator.next();
                grid.add(new JLabel(entry.getKey()));
                grid.add(entry.getValue());
            }
            for(JButton button : formCliente.getButtons()){
                buttons.add(button);
                button.addActionListener(signUpListener);
            }
        }

       else if (utenteBusiness.isLoggedAmministratore()) {  //amministratore in sessione: caso d'uso: amministratore aggiunge cucina e amministratori

            FormUtente formUtente = new FormUtente(frame);
            formPersonale = new FormPersonaleDecorator(formUtente, frame);

            Iterator<Map.Entry<String, JTextField>> iterator = formPersonale.getTextFields().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, JTextField> entry = iterator.next();
                grid.add(new JLabel(entry.getKey()));
                grid.add(entry.getValue());
            }


            grid.add(new JLabel("Ruolo"));

            JPanel panel = new JPanel(new GridLayout(2, 1));

            Iterator<JRadioButton> iterator1 = formPersonale.getRadioButtons().iterator();
            while (iterator1.hasNext()) {
                panel.add(iterator1.next());
            }

            grid.add(panel);

            for (JButton button : formPersonale.getButtons()) {
                buttons.add(button);
            }

        }
       add(grid, BorderLayout.NORTH);
       add(buttons, BorderLayout.CENTER);

    }

    public void emptyFields(){
        Iterator<Map.Entry<String, JTextField>> iterator = formCliente.getTextFields().entrySet().iterator();

        while (iterator.hasNext()){
             iterator.next().getValue().setText("");
        }
    }
}
