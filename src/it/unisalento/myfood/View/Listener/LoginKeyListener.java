package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.View.LoginPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginKeyListener implements KeyListener {
    LoginPanel loginPanel;

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
