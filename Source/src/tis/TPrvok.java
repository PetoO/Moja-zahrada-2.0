package tis;

import java.awt.BasicStroke;
import java.awt.Canvas;
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
public class TPrvok implements Serializable{
    String meno;
    String obrazok;
    Color farba;
    double hrubka;
    int styl;
    String info;
    boolean edited;
    ArrayList<BasicStroke> strokes = new ArrayList<BasicStroke>();
  
    /**
     * vytvorí nový prvok
     * @param h - hrúbka čiary, ktorou sa bude prvok vykreslovať
     */
 public TPrvok(double h) {
     meno = "";
     obrazok = "";
     farba = Color.BLACK;
     hrubka = h;
     styl = 1;
     edited = false;
}   
   
/**
 * funkcia, ktorá vracia meno prvku
 * @return String meno
 */
 public String getName(){
     return meno;
 }
 
 /**
  * funkcia, ktorá vracia info o prvku
  * @return String info
  */
 public String vratInfo(){
     return info;
 }
   
 /**
  * hlavička funkcie na vykreslovanie
  * @param g grafika, do ktorej sa bude kresliť
  */
 public void draw(Graphics2D g){
        
 }
 
}
