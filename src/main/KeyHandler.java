package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    public boolean w, a, s, d;
    public boolean reset;
    public boolean pause = true;


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_W) { // creates the input where pac will keep moving in the direction inputted
            w = true;                  // until there is a new direction input
            a = false;
            s = false;
            d = false;
        }
        else if(keyCode == KeyEvent.VK_A) {
            w = false;
            a = true;
            s = false;
            d = false;
        }
        else if(keyCode == KeyEvent.VK_S) {
            w = false;
            a = false;
            s = true;
            d = false;
        }
        else if(keyCode == KeyEvent.VK_D) {
            w = false;
            a = false;
            s = false;
            d = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_R) {
            reset = true;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            pause = !pause;
        }
    }

}
