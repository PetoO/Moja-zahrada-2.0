package tis;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucia Budinská
 */
public class TCiara extends TPrvok {

    class Bod implements Serializable{
        double x;
        double y;
    }
    ArrayList<Bod> body; 
  
    /** 
     * konstruktor ciaroveho prvku
     * @param x1 - x-ova suradnica prveho bodu
     * @param y1 - y-ova suradnica prveho bodu
     * @param x2 - x-ova suradnica druheho bodu 
     * @param y2 - y-ova suradnica druheho bodu
     * @param m - meno/nazov prvku
     * @param f - farba, ktorou sa bude vykreslovat
     * @param h - hrubka, ktorou sa bude vykreslovat
     * @param s - cislo stylu, ktorym sa bude vykreslovat
    */
public TCiara(double x1, double y1, double x2, double y2,  String m, Color f, double h, int s){
    super(h);
    body = new ArrayList<Bod>();
    Bod b = new Bod();
    b.x = x1;
    b.y = y1;
    body.add(b);
    Bod b2 = new Bod();
    b2.x = x2;
    b2.y = y2;
    body.add(b2);
    meno = m;
    farba = f;
    hrubka = h;
    styl = s;
    //System.out.println(x1 + ","+ y1+",: "+ x2 + ","+ y2 );
}

/** 
 * vytvori novy bod - na koniec zoznamu bodov
 * @param x1 - x-ova suradnica noveho bodu
 * @param y1 - y-ova suradnica noveho bodu
*/
public boolean novyBod(double x1, double y1) {
  if ((x1 != body.get(body.size()-2).x) || (y1 != body.get(body.size()-2).y)){
        Bod b = new Bod();
        b.x = x1;
        b.y = y1;
        body.add(b);
        return true;
  }
    else {
        return false;
    }
}

/**
 * vytvori novy bod na zaciatku zoznamu (novy prvy bod ciary) 
 * @param x1 - x-ova suradnica noveho bodu
 * @param y1 - y-ova suradnica noveho bodu
*/
public boolean novyBodZ(double x1, double y1) {
    Bod b = new Bod();
    b.x = x1;
    b.y = y1;
    body.add(0,b);
    
    return true;
}

/**
 * vykresli do grafiky g ciaru daneho stylu, hrubky a farby
 * ak je ciara v edit mode, oznaci kazdy vrchol cervenym kruzkom
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
    int[] bodyX = new int[body.size()];
    int[] bodyY = new int[body.size()];
    for (int i=0; i<body.size(); i++){
        Bod b = body.get(i);
        bodyX[i] = (int) b.x;
        bodyY[i] = (int) b.y;
    }
    g.drawPolyline(bodyX, bodyY, body.size());
    g.setColor(Color.RED);
    if (edited){
        for (int i=0; i<body.size(); i++){
            g.fillOval((int)body.get(i).x-5, (int)body.get(i).y-5, 10, 10);
        }
    }
 
}

/**
    * presunie bod s indexom @i na nove suradnice
    * @param i - index bodu
    * @param 1 - nova x-ova suradnica bodu
    * @param y1 - nova y-ova suradnica bodu
*/
public boolean presun(int i, double x1, double y1){
    if ((i<body.size()) && (i>=0)){
        body.get(i).x = x1;
        body.get(i).y = y1;
        return true;
    }
    else{
        return false;
    }
}

/**
    * zisti, ci sa kliklo niekde v blizkosti tohto ciaroveho prvku (tj. hociktorej usecky)
    * @param xx - x-ova suradnica kliknutia
    * @param yy - y-ova suradnica kliknutia
*/
public boolean isNear(int xx, int yy){
    for (int i=0; i<body.size()-1; i++){
        Bod b1 = body.get(i);
        Bod b2 = body.get(i+1);
        double a = b2.y-b1.y;
        double b = b1.x-b2.x;
        double c = -1*((a*b1.x) + (b*b1.y));
        double vzdial = (((a*xx) + (b*yy) + c) / Math.sqrt((a*a)+(b*b)));
        if ((vzdial > -5 ) && (vzdial < 5)){
            double vx = Math.max(b1.x, b2.x);
            double mx = Math.min(b1.x, b2.x);
            double vy = Math.max(b1.y, b2.y);
            double my = Math.min(b1.y, b2.y);
            if ((xx < (mx-5)) || (xx > (vx+5))){
                break;
            }
            if ((yy < (my-5)) || (yy > (vy+5))){
                 break;
            }
            return true;    
        }
    }     
    
    return false;
}

/**
   *zisti, ci sa kliklo v blizkosti nejakeho hranicneho bodu na ciare
   * @param xx - x-ova suradnica kliku
   * @param yy - y-ova suradnica kliku
     * @return index bodu, na ktory sa kliklo
*/

public int pointNear(int xx, int yy){
    for (int i=0; i<body.size(); i++){
        double x = body.get(i).x;
        double y = body.get(i).y;
       if (Math.sqrt(((x-xx)*(x-xx)) + ((y-yy)*(y-yy)) ) <= (5)){
         return i;
       }
    }
    return -1;
}

/**
 * posunie celu ciaru tak, ze prvy bod ciary sa posunie na nove suradnice a zvysne body idu symetricky
 * @param x1 - nova x-ova suradnica prveho bodu
 * @param y1 - nova y-ova suradnica prveho bodu
*/
public void posunCiaru(double x1, double y1){
    double v1 = x1-body.get(0).x;
    double v2 = y1-body.get(0).y;
    for (int i=0; i<body.size(); i++){
        Bod b = body.get(i);
        Bod b1 = new Bod();
        b1.x = b.x+v1;
        b1.y = b.y+v2;
        body.set(i, b1);
    }
}
/**
* vymaze koncovy bod
*/
    void vymazK() {
       if (body.size() > 2 ) body.remove(body.size()-1);
    }

    /**
    * vymaze prvy bod
    */
    void vymazZ() {
        if (body.size() > 2 ) body.remove(0);
    }
    
    public double getLength(){
        double l = 0;
        Bod a = new Bod();
        Bod b = new Bod();
        for (int i=0; i+1<body.size(); i++){
            a = body.get(i);
            b = body.get(i+1);
            
            l+=Math.sqrt( Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2));
        }    
        return l/100;
    }
    
    
}
