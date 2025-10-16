package it.unisalento.myfood.Listener;

import it.unisalento.myfood.View.Panels.LoginPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginKeyListener implements KeyListener {  //preme il tasto login quando si schiaccia il tasto "a capo"
    private LoginPanel loginPanel;

    public LoginKeyListener(LoginPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loginPanel.clickLoginButton();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
