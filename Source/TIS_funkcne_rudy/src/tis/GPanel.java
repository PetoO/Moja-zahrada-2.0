/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tis;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Branislav Heriban
 * GPanel je špeciálny JPanel, ktorý je určený pre potrebu našej aplikácie
 */
public class GPanel extends JPanel implements MouseListener, KeyListener  {
    public Graphics graph;
    ArrayList<TPrvok> prvky;
    TPlan plan;
    
    private double scale = 1;    
    final int METERS_TO_PIXELS = 100;


    GPanel(TPlan planik) {
        this.plan = planik;
        this.setFocusable(true); 
    }
    
    /**
     * zavolá funkcie planiku na vykreslenie plochy
     * @param g 
     */
    @Override public void paintComponent(Graphics g) { // <-- HERE!
        plan.paint((Graphics2D)g);
        plan.width = getWidth();
        plan.heigth = getHeight();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    
}
