package it.unisalento.myfood.View;

import it.unisalento.myfood.View.Listener.PaymentFrameListener;

import javax.swing.*;
import java.awt.*;

public class PaymentFrame extends JFrame {

    private PaymentFrameListener paymentFrameListener;
   public PaymentFrame(){
       setTitle("Cassa");

       paymentFrameListener = new PaymentFrameListener(this);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       setLocationRelativeTo(null);

       setLayout(new GridLayout(1, 2));
       setSize(300, 150);

       JButton cassa = new JButton("Paga in contanti");
       JPanel cassaPan = new JPanel(new FlowLayout());
       cassaPan.add(cassa);

       add(cassaPan);

       setVisible(true);

       cassa.addActionListener(paymentFrameListener);
       cassa.setActionCommand(PaymentFrameListener.CASSA_BTN);


    }
}
