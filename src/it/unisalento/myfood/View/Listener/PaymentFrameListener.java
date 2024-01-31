package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.View.PaymentFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PublicKey;

public class PaymentFrameListener implements ActionListener {

    public static final String CASSA_BTN = "CASSA_BTN";

    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();

    private PaymentFrame paymentFrame;

    public PaymentFrameListener(PaymentFrame paymentFrame) {
        this.paymentFrame = paymentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case CASSA_BTN :
                if(ordineBusiness.setSelectedOrdinePagato())
                    JOptionPane.showMessageDialog(paymentFrame, "Ordine pagato correttamente, riceverai una lista via mail");
                else
                    JOptionPane.showMessageDialog(paymentFrame, "Si è verificato un errore");
                break;

        }

    }
}
