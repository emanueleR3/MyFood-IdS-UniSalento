package it.unisalento.myfood.Listener;

import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.View.PaymentFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentFrameListener implements ActionListener {

    public static final String CASSA_BTN = "CASSA_BTN";

    private Integer idOrdineCreato;

    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();

    private PaymentFrame paymentFrame;

    public PaymentFrameListener(PaymentFrame paymentFrame) {
        this.paymentFrame = paymentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case CASSA_BTN :
                if (ordineBusiness.setSelectedOrdinePagato()) {
                    ordineBusiness.sendEmailOrdinePagato(paymentFrame.getIdOrdineCraeto());
                    JOptionPane.showMessageDialog(paymentFrame, "Pagamento effettuato, grazie. Riceverai una mail quando il tuo ordine sarà pronto");
                    paymentFrame.dispose();
                } else
                    JOptionPane.showMessageDialog(paymentFrame, "Si è verificato un errore");
                break;

        }
    }
}
