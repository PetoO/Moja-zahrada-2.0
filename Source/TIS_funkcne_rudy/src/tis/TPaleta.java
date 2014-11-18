
package tis;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Cvaky
 * V tejto triede je uložená celá paletka. 
 * Nachádza sa tu dynaické pole objektov Prvkov paletky
 */
class TPaleta {

    public ArrayList<TPaletaPrvok> paletaPrvky = new ArrayList<TPaletaPrvok>();
    public JPaletka G;

    public TPaleta() throws IOException {
        Load();
        G = new JPaletka(this);
    }
/**
     * Uloženie paletky do súboru, ukladá sa celý objekt.
     * @param paletaPrvky1 Pole prvkov
     */
    public void Save(ArrayList<TPaletaPrvok> paletaPrvky1) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File("paleta")));
            oos.writeObject(paletaPrvky1);
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
     * Obnovenie paletky zo súboru.
     * 
     */
    public void Load() {
        ObjectOutputStream oos;
        try {
            InputStream is = new FileInputStream(new File("paleta")); //moyno je to len string
            ObjectInputStream ois = new ObjectInputStream(is);
            paletaPrvky = (ArrayList<TPaletaPrvok>) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


/**
     * Vytvorí nový frame na paletku
     */
    public void pridajDoPaletky() {

        JFrame frame = new JFrame();
        frame.setSize(300, 500);
        frame.setContentPane(G);
        frame.setVisible(true);
        frame.repaint();
    }
}
