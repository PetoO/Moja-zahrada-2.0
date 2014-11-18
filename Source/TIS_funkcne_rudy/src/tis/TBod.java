package tis;


import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucia Budinská 
 * 
 */
public class TBod extends TPrvok{
    double x, y;
    double velkost;
    
    /**
     * konstruktor bodoveho prvku
     * @param xx - x-ova suradnica bodu
     * @param yy - y-ova suradnica bodu
     * @param m - meno/nazov bodu
     * @param f - farba, ktorou sa bude prvok vykreslovat
     * @param h - hrubka ciary, ktorou sa bude prvok vykreslovat
     * @param s - styl ciary
     * @param v - velkost kruznice, ktorou sa bod vykresli
     */
public TBod(double xx, double yy, String m, Color f, double h, int s, double v){
    super(h);
    x = xx;
    y = yy;
    meno = m;
    farba = f;
    hrubka = h;
    styl = s;
    velkost = v;
    
}

/**
 * presunie bod na nove suradnice
 * @param x1 - nova x-ova suradnica bodu
 * @param y1 - nova y-ova suradnica bodu
 * @return true ak su nove suradnice ine ako tie stare, inak false
 */
public boolean presun(int x1, int y1){
       if ((x!=x1) && (y!=y1)){
           x = x1;
           y = y1;
           return true;
       }
       else {
           return false;
       }
} 

/**
 * vykresli bod ako kruznicu s polomerom velkost, ciarkovanim styl a farbou farba 
 * @param g - graficka plocha, do ktorej sa vzkresluje
 */
public void draw(Graphics2D g){
    g.setColor(farba);
    float[] patt = new float[4];
    patt[0] = 10.0f; 
    patt[1]= styl + (float)5;
    patt[2]= styl * (float)2.5;
    patt[3] = patt[1];
    Stroke s = new BasicStroke((float) hrubka,                     
                   BasicStroke.CAP_SQUARE,    
                   BasicStroke.JOIN_MITER,    
                   10.0f,                    
                   patt, 
                   0.0f);
    g.setStroke(s);
    g.drawOval((int)(x - velkost/2 - hrubka), (int)(y - velkost/2 - hrubka), (int)(velkost + hrubka), (int)(velkost + hrubka));
    if (edited){
        float[] dash = {5.0f};
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2.0f,                      
                           BasicStroke.CAP_SQUARE,    
                           BasicStroke.JOIN_MITER,    
                           10.0f,                     
                           dash, 
                           0.0f));
       g.drawOval((int) (x- (velkost/2) - hrubka - 10), (int) (y- (velkost/2) - hrubka - 10), (int)(velkost + hrubka + 20),(int)(velkost + hrubka + 20));
        double polomer = (velkost * 2 + 30) / 2;
    }
  
   
}
/**
 * zisti, ci sa kliklo niekde v blizkosti bodu
 * @param xx - x-ova suradnica kliku
 * @param yy - y-ova suradnica kliku
 * @return boolovsku hodnotu, ci je kliknutie v blizkosti bodu
 */
public boolean isNear(double xx, double yy){  
    System.out.println("X / Y" + x + " / " + y);
        System.out.println("XX / YY" + xx + " / " + yy);
    if (Math.sqrt(((x-xx)*(x-xx)) + ((y-yy)*(y-yy)) ) <= (velkost/2 + hrubka)) {
      return true;
      
    } else { 
        
      return false; 
    }
}
}
