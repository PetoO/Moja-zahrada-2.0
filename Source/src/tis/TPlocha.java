package tis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lucia Budinská
 */
public class TPlocha extends TPrvok {

    class Bod implements Serializable {

        double x;
        double y;
    }
    ArrayList<Bod> body;
    boolean spojene;
/**
 * konstruktor plochy
 * @param xx x-ova suradnica prveho bodu
 * @param yy y-ova suradnica prveho bodu
 * @param xxx x-ova suradnica druheho bodu
 * @param yyy y-ova suradnica druheho bodu
 * @param m meno/nazov prvku
 * @param f farba, ktorou sa bude prvok vykreslovat
 * @param h hrubka, akou sa bude vykreslovat
 * @param s index stylu srafovania
 */
    public TPlocha(double xx, double yy, double xxx, double yyy, String m, Color f, double h, int s) {
        super(h);
        body = new ArrayList<Bod>();
        
        Bod b1 = new Bod(); 
        b1.x = xx;
        b1.y = yy;
        body.add(b1);
        Bod b2 = new Bod();
       
        b2.x = xxx;
        
        b2.y = yyy;
        
        body.add(b2);
        meno = m;
        farba = f;
        hrubka = h;
        styl = s;
        spojene = false;
    }

    /**
     * presunie cely prvok s tym, ze posunie prvy bod plochy na nove suradnice a zvysne posunie rovnakym vektorom
     * @param xx nova x-ova suradnica prveho bodu
     * @param yy nova y-ova suradnica prveho bodu
     */
    public void presun(double xx, double yy) {
        double v1 = xx - body.get(0).x;
        double v2 = yy - body.get(0).y;
        for (int i = 0; i < body.size(); i++) {
            Bod b = body.get(i);
            Bod b1 = new Bod();
            b1.x = b.x + v1;
            b1.y = b.y + v2;
            body.set(i, b1);
        }
    }
/**
 * posunie jeden bod na nove suradnice, tym zmeni velkost plochy
 * @param i - index posuvaneho prvku
 * @param xx - nova x-ova suradnica tohto prvku
 * @param yy - nova y-ova suradnica tohto prvku
 * @return boolovsku hodnotu, ci sa nam podarilo zmenit velkost
 */
    public boolean zmenVelkost(int i, double xx, double yy) {
        if ((i >= 0) && (i < body.size())) {
            Bod b = new Bod();
            b.x = xx;
            b.y = yy;
            body.set(i, b);
            return true;
        }
        return false;
    }

    /**
     * vykresli polygon do planiku, v pripade, ze este nie je dokonceny, vykresluje iba ciaru
     * v edit mode je kazdy vrchol zvyrazneny cervenym kruzkom
     * @param g 
     */
    public void draw(Graphics2D g) {
        int[] bodyX = new int[body.size()];
        int[] bodyY = new int[body.size()];
        for (int i = 0; i < body.size(); i++) {
            Bod b = body.get(i);
            bodyX[i] = (int) b.x;
            bodyY[i] = (int) b.y;
        }
        g.setColor(farba);
        if (!spojene) {
            g.setColor(farba);
            g.drawPolyline(bodyX, bodyY, body.size());
        } else {
            BufferedImage texture;
            try {
                String name = "src/srafy" + styl + ".png";
                texture = ImageIO.read(new File(name));
                TexturePaint tt = new TexturePaint(texture, new Rectangle(0, 0, 20, 20));
                g.setPaint(tt);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            g.fillPolygon(bodyX, bodyY, body.size());

        }
        g.setColor(Color.RED);
        if (edited) {
            for (int i = 0; i < body.size(); i++) {
                g.fillOval((int) body.get(i).x - 5, (int) body.get(i).y - 5, 10, 10);
            }
        }

    }

    /**
     * funkcia, ktora zisti, ci sa kliklo niekde do tejto plochy
     * @param xx x-ova suradnica kliku
     * @param yy y-ova suradnica kliku
     * @return true, ak sa kliklo do na tento prvok, false ak nie
     */
    public boolean isNear(double xx, double yy) {
        int j = body.size() - 1;
        boolean nepar = false;
        for (int i=0; i<body.size(); i++) {
            if ((((body.get(i).y < yy) &&  (body.get(j).y>=yy))
                || ((body.get(i).y >= yy) && (body.get(j).y<yy)))
                 && ((body.get(i).x <= xx) || (body.get(j).x<=xx))) {
                if ((body.get(i).x+(yy-body.get(i).y)/(body.get(j).y - body.get(i).y)*(body.get(j).x - body.get(i).x))<xx){
                    nepar = !nepar;
                }
            }
            j=i;
                        
        }

        return nepar;
    }

    /**
     * zisti, ci sa kliklo niekde v blizkosti nejakeho bodu plochy, ak ano, vrati index tohto bodu
     * @param xx - x-ova suradnica kliknutia
     * @param yy - y-ova suradnia kliknutia
     * @return - index bodu, na ktory sa kliklo
     */
    public int pointNear(int xx, int yy) {
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
 * vytvori novy bod - na koniec zoznamu bodov
     * @param x1 - x-ova suradnica noveho bodu
     * @param y1 - y-ova suradnica noveho bodu

*/
public void novyBod(double x1, double y1) {
    if ((x1 != body.get(body.size()-2).x) || (y1 != body.get(body.size()-2).y)){
        Bod b = new Bod();
        b.x = x1;
        b.y = y1;
        body.add(b);
    
  }
    
}

/**
 * vytvori novy bod na zaciatku zoznamu (novy prvy bod ciary) 
 * @param x1 - x-ova suradnica noveho bodu
 * @param y1 - y-ova suradnica noveho bodu
*/
public void novyBodZ(double x1, double y1) {
    Bod b = new Bod();
    b.x = x1;
    b.y = y1;
    body.add(0,b);

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
    
    public double getArea(){
        double d = 0;
        if(body.size()>=2){
            Bod a = new Bod();
            Bod b = new Bod();
            a=body.get(0);
            b=body.get(body.size()-1);
            d=((a.y*b.x)-(a.x*b.y));
                    
            for (int i=0; i+1<body.size(); i++){
                a = body.get(i);
                b = body.get(i+1);
                d+=((a.x*b.y) - (a.y*b.x));
            } 
        }
    
       return Math.abs(d/20000); 
    }
}
