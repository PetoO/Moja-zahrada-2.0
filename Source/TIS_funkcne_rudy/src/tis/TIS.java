/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tis;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import static java.lang.Thread.sleep;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author Branislav Heriban
 */
public class TIS {
    static TPlan planik;
  public static TPaleta paletka;
    static public TPrvok p;
    static public NewJFrame vr;
    public static GPanel G;
    /**
     * @param args the command line arguments
     * vytvorí nový planik, nový GPanel, pridá listenery a zavolá NewJFrame
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        planik = new TPlan();
        G = new GPanel(planik);
        G.addKeyListener(planik);
        G.addMouseListener(planik);
        G.addMouseMotionListener(planik);
        vr = new NewJFrame();
        vr.setDimension();            
        
        
        planik.nacitaj("src/zahrada.xls");
        G.requestFocus();  
    }
    
}
