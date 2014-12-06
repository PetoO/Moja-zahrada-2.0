/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tis;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Branislav Heriban
 * tento špeciálny JPanel sa používa na vykreslenie tabuľky o aktuálnych prvkoch v ploche, teda slúži ako legenda
 */
public class JTabulka extends JPanel {
    public Graphics graph;
    TPlan planik;
    private ArrayList<TPrvok> prvky;
    private ArrayList<TPrvok> obrazky;
    
    JScrollPane scrollPane;

    JTabulka(ArrayList<TPrvok> prvky, JScrollPane scrollPane) {
        this.prvky = prvky;
        this.scrollPane = scrollPane;
//            add(scrollPane);
    }
    
    /**
     * vykreslí tabuľku: vľavo je meno prvku a napravo je jeho ikonka 
     * @param g 
     */
    @Override public void paintComponent(Graphics g) { // <-- HERE!
        g.setColor(Color.black);
        g.drawString("Meno prvku", 10, 15);
        g.drawString("Obrazok prvku", 120, 15);
        int y = 0;
        y+= scrollPane.getVerticalScrollBarPolicy();
        g.setColor(Color.white);
        g.fillRect(0, 20, this.getWidth(), this.getHeight());
        g.setColor(Color.black);
        for (TPrvok prvok: prvky) {
            g.setColor(Color.black);
            if (prvok.meno != null) g.drawString(prvok.meno, 10, y+15);
            
            if (prvok instanceof TBod) {
                TBod bod = (TBod) prvok;
                TBod bodObr = new TBod(160.0 - bod.velkost, y+30- bod.velkost, "", bod.farba, bod.hrubka, bod.styl, bod.velkost,TIS.planik.sadenie);
                bodObr.draw((Graphics2D) g);
            }

            if (prvok instanceof TPlocha) {
                TPlocha plocha = (TPlocha) prvok;
                TPlocha bodObr = new TPlocha(120.0, y, 220, y, "", plocha.farba, plocha.hrubka, plocha.styl);
                bodObr.novyBod(220.0, y+60);
                bodObr.novyBod(120.0, y+60);
                bodObr.spojene=true;
                bodObr.draw((Graphics2D) g);
            }
            if (prvok instanceof TCiara) {
                TCiara ciara = (TCiara) prvok;
                TCiara bodObr = new TCiara(120.0, y+30, 220, y+30, "", ciara.farba, ciara.hrubka, ciara.styl);
                bodObr.draw((Graphics2D) g);
            }
            y+=60;
        }
        
        
        graph = g;
        
    }
    
    /**
     * @return vráti použitú grafiku
     */
    public Graphics getGraph() {
        return graph;
    }
    
}
