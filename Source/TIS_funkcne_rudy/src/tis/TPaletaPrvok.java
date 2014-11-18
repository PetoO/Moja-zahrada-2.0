package tis;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Rudolf Cvacho
 * Všetky informácie každého prvku v tabuľke
 */
public class TPaletaPrvok implements Serializable {
    int typ; // bod = 1, ciara = 2, plocha = 3
    int num;
    String name; // meno prvku
    String img; // adresa k ikonke obrazku
    TPrvok typPrvku;
     Color farba;
    double hrubka;
    int styl;
    double velkost;
    String info;
    /**
     * konstruktor bodoveho prvku
     * @param num - id číslo prvku
     * @param typ - typ prvku bod, ciara alebo plocha
     * @param name - meno/nazov prvku
     * @param img - obrazok
     * @param farba - farba prvku
     * @param hrubka - hrubka ciary
     * @param styl -  index stylu srafovania
     * @param velkost - velkost bodu , kruznice
     */
    public TPaletaPrvok(int num,int typ, String name, String img, Color farba, double hrubka, int styl, double velkost) {
        this.num = num;
        this.typ = typ;
        this.name = name;
        this.farba = farba;
        this.hrubka = hrubka;
        this.styl = styl;
        this.velkost = velkost;
        this.img = img;
    }
 /**
  * @param num - id číslo prvku
  */
         
    public void setNum(int num) {
        this.num = num;
    }

    
    public void setTypPrvku(TPrvok typPrvku) {
        this.typPrvku = typPrvku;
    }
}
