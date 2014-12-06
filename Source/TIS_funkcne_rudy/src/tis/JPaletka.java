/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tis;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


 /**
     * @author Rudolf Cvacho
     * Panel , ktorý slúži na pridanie prvku do paletky
     */
public class JPaletka extends JPanel {

    public Graphics graph;
    Canvas canv;
    TPlan planik;
  public  ArrayList<TPaletaPrvok> volneBody;
    ArrayList<TPaletaPrvok> volneCiary;
    ArrayList<TPaletaPrvok> volnePlochy;
    public  TPaleta paleta;

    public JPaletka(TPaleta paleta) {

        this.paleta = paleta;
        vygenerujPrvky();

    }
    /**
     * slúži na kreslenie a refreshovanie paletky
     * kreslí názov prvku, obrázok a potrebné checkboxy s buttonom
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {
        removeAll();
        g.setColor(Color.black);
        final JTextField name = new JTextField();
        name.setBounds(80, 90, 100, 30);
        add(name);
        g.drawString("Nazov ", 40, 110);
        JButton odoslat = new JButton("Pridat");
        odoslat.setBounds(80, 130, 100, 50);

        Image imgBod = null;
        Image imgCiara = null;
        Image imgPlocha = null;

        try {
            if (volneBody.size() > 0) {
                imgBod = ImageIO.read(new File(volneBody.get(volneBody.size() - 1).img));
            } else {
                imgBod = ImageIO.read(new File("src/noimg.png"));
            }
            JLabel bodLabel = new JLabel(new ImageIcon(imgBod));
            bodLabel.setBounds(16, 10, 60, 50);
            add(bodLabel);
        } catch (IOException ex) {
            Logger.getLogger(JPaletka.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
//            System.out.println("pocet ciar" + volneCiary.size()+ volneCiary.get(volneCiary.size()-1).img);
            if (volneCiary.size() > 0) {
                imgCiara = ImageIO.read(new File(volneCiary.get(volneCiary.size() - 1).img));
            } else {
                imgCiara = ImageIO.read(new File("src/noimg.png"));
            }
            JLabel ciaraLabel = new JLabel(new ImageIcon(imgCiara));
            ciaraLabel.setBounds(106, 10, 60, 50);
            add(ciaraLabel);
        } catch (IOException ex) {
            Logger.getLogger(JPaletka.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (volnePlochy.size() > 0) {
                imgPlocha = ImageIO.read(new File(volnePlochy.get(volnePlochy.size() - 1).img));
            } else {
                imgPlocha = ImageIO.read(new File("src/noimg.png"));
            }
            JLabel PlochaLabel = new JLabel(new ImageIcon(imgPlocha));
            PlochaLabel.setBounds(200, 10, 60, 50);
            add(PlochaLabel);
        } catch (IOException ex) {
            Logger.getLogger(JPaletka.class.getName()).log(Level.SEVERE, null, ex);
        }

//            System.out.println(volneBody.size()+"velkosttttt");
        final ButtonGroup group = new ButtonGroup();
        final JRadioButton bod = new JRadioButton("bod");
        bod.setBounds(30, 70, 60, 20);
        bod.setActionCommand("bod");
        bod.setSelected(true);
        add(bod);
        group.add(bod);
        JRadioButton ciara = new JRadioButton("ciara");
        ciara.setBounds(106, 70, 60, 20);
        ciara.setActionCommand("ciara");
        add(ciara);
        group.add(ciara);
        JRadioButton plocha = new JRadioButton("plocha");
        plocha.setBounds(196, 70, 80, 20);
        plocha.setActionCommand("plocha");
        add(plocha);
        group.add(plocha);
        odoslat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(name.getText());

                if (group.getSelection().getActionCommand().contentEquals("bod") && volneBody.size() > 0) {
                    TPaletaPrvok bodPrvok = volneBody.get(volneBody.size() - 1);
                    bodPrvok.name = name.getText();
                    paleta.paletaPrvky.add(bodPrvok);
                    TIS.vr.painter();
                    volneBody.remove(bodPrvok);
                } else if (group.getSelection().getActionCommand().contentEquals("ciara") && volneBody.size() > 0) {
                    TPaletaPrvok ciaraPrvok = volneCiary.get(volneCiary.size() - 1);
                    ciaraPrvok.name = name.getText();
                    paleta.paletaPrvky.add(ciaraPrvok);
                    TIS.vr.painter();
                    volneCiary.remove(ciaraPrvok);
                } else if (group.getSelection().getActionCommand().contentEquals("plocha") && volneBody.size() > 0) {
                    TPaletaPrvok plochaPrvok = volnePlochy.get(volnePlochy.size() - 1);
                    plochaPrvok.name = name.getText();
                    paleta.paletaPrvky.add(plochaPrvok);
                    TIS.vr.painter();
                    volnePlochy.remove(plochaPrvok);
                }
                repaint();
            }
        });

        add(odoslat);
        JButton ulozit = new JButton("Ulozit paletku");
        ulozit.setBounds(130, 190, 130, 50);
        ulozit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paleta.Save(paleta.paletaPrvky);
                
//                        Collections.sort(paleta.paletaPrvky, new Comparator<TPaletaPrvok>);
            }
        });
        add(ulozit);
        graph = g;
    }

    public void vygenerujPrvky() {
        volneBody = new ArrayList<TPaletaPrvok>();
        TPaletaPrvok bod1 = new TPaletaPrvok(1, 1, "bod1", "src/bod1.png", Color.decode("#fc1d2b"), 7.0, 0, 10.0);
        TPaletaPrvok bod2 = new TPaletaPrvok(1, 1, "bod2", "src/bod2.png", Color.decode("#8c9932"), 6.0, 0, 10.0);
        TPaletaPrvok bod3 = new TPaletaPrvok(1, 1, "bod3", "src/bod3.png", Color.decode("#d71a63"), 6.0, 0, 15.0);
        TPaletaPrvok bod4 = new TPaletaPrvok(1, 1, "bod4", "src/bod4.png", Color.decode("#0055d4"), 15.0, 0, 8.0);
        TPaletaPrvok bod5 = new TPaletaPrvok(1, 1, "bod5", "src/bod5.png", Color.decode("#E12922"), 6.0, 0, 8.0);
        TPaletaPrvok bod6 = new TPaletaPrvok(1, 1, "bod6", "src/bod6.png", Color.decode("#4a3ca4"), 6.0, 0, 15.0);
        TPaletaPrvok bod7 = new TPaletaPrvok(1, 1, "bod7", "src/bod7.png", Color.decode("#88aa00"), 15.0, 0, 8.0);
        TPaletaPrvok bod8 = new TPaletaPrvok(1, 1, "bod8", "src/bod8.png", Color.decode("#ffd42a"), 10.0, 0, 4.0);
        TPaletaPrvok bod9 = new TPaletaPrvok(1, 1, "bod9", "src/bod9.png", Color.decode("#f4fc2e"), 6.0, 0, 15.0);
        TPaletaPrvok bod10 = new TPaletaPrvok(1, 1, "bod10", "src/bod10.png", Color.decode("#aa4400"), 10.0, 0, 14.0);
        TPaletaPrvok bod11 = new TPaletaPrvok(1, 1, "bod11", "src/bod11.png", Color.decode("#c073a8"), 10.0, 0, 14.0);
        TPaletaPrvok bod12 = new TPaletaPrvok(1, 1, "bod12", "src/bod12.png", Color.decode("#24d6ec"), 10.0, 0, 14.0);
        TPaletaPrvok bod13 = new TPaletaPrvok(1, 1, "bod13", "src/bod13.png", Color.decode("#00002b"), 10.0, 0, 14.0);
        volneBody.add(bod1);
        volneBody.add(bod2);
        volneBody.add(bod4);
        volneBody.add(bod3);
        volneBody.add(bod5);
        volneBody.add(bod6);
        volneBody.add(bod7);
        volneBody.add(bod8);
        volneBody.add(bod9);
        volneBody.add(bod10);
        volneBody.add(bod11);
        volneBody.add(bod12);
        volneBody.add(bod13);

        TPaletaPrvok ciara1 = new TPaletaPrvok(1, 2, "ciara1", "src/ciara1.png", Color.decode("#000000"), 2.0, 0, 1.0);
        TPaletaPrvok ciara2 = new TPaletaPrvok(1, 2, "ciara2", "src/ciara2.png", Color.decode("#CCCCCC"), 2.0, 1, 1.0);
        TPaletaPrvok ciara3 = new TPaletaPrvok(1, 2, "ciara3", "src/ciara3.png", Color.decode("#0000FF"), 2.0, 2, 1.0);
        TPaletaPrvok ciara4 = new TPaletaPrvok(1, 2, "ciara4", "src/ciara4.png", Color.decode("#FF0000"), 2.0, 3, 1.0);
        TPaletaPrvok ciara5 = new TPaletaPrvok(1, 2, "ciara5", "src/ciara5.png", Color.decode("#FFFFFF"), 2.0, 4, 1.0);
        TPaletaPrvok ciara6 = new TPaletaPrvok(1, 2, "ciara6", "src/ciara6.png", Color.decode("#FF00FF"), 2.0, 5, 1.0);
        TPaletaPrvok ciara7 = new TPaletaPrvok(1, 2, "ciara7", "src/ciara7.png", Color.decode("#00FF00"), 2.0, 6, 1.0);
        TPaletaPrvok ciara8 = new TPaletaPrvok(1, 2, "ciara8", "src/ciara8.png", Color.decode("#FFFF00"), 2.0, 7, 1.0);
        TPaletaPrvok ciara9 = new TPaletaPrvok(1, 2, "ciara9", "src/ciara9.png", Color.decode("#00FFFF"), 2.0, 8, 1.0);
        TPaletaPrvok ciara10 = new TPaletaPrvok(1, 2, "ciara10", "src/ciara10.png", Color.decode("#FF99FF"), 2.0, 9, 1.0);
        TPaletaPrvok ciara11 = new TPaletaPrvok(1, 2, "ciara11", "src/ciara11.png", Color.decode("#99CCFF"), 2.0, 10, 1.0);

        volneCiary = new ArrayList<TPaletaPrvok>();
        volneCiary.add(ciara1);
        volneCiary.add(ciara2);
        volneCiary.add(ciara3);
        volneCiary.add(ciara4);
        volneCiary.add(ciara5);
        volneCiary.add(ciara6);
        volneCiary.add(ciara7);
        volneCiary.add(ciara8);
        volneCiary.add(ciara9);
        volneCiary.add(ciara10);
        volneCiary.add(ciara11);

        volnePlochy = new ArrayList<TPaletaPrvok>();
        TPaletaPrvok plocha1 = new TPaletaPrvok(1, 3, "plocha1", "src/srafy1.png", Color.decode("#340000"), 1.0, 9, 1.0);
        TPaletaPrvok plocha2 = new TPaletaPrvok(1, 3, "plocha2", "src/srafy2.png", Color.decode("#340000"), 2.0, 9, 2.0);
        TPaletaPrvok plocha3 = new TPaletaPrvok(1, 3, "plocha3", "src/srafy3.png", Color.decode("#340000"), 3.0, 9, 3.0);
        TPaletaPrvok plocha4 = new TPaletaPrvok(1, 3, "plocha4", "src/srafy4.png", Color.decode("#340000"), 4.0, 9, 4.0);
        TPaletaPrvok plocha5 = new TPaletaPrvok(1, 3, "plocha5", "src/srafy5.png", Color.decode("#340000"), 5.0, 9, 5.0);
        TPaletaPrvok plocha6 = new TPaletaPrvok(1, 3, "plocha6", "src/srafy6.png", Color.decode("#340000"), 6.0, 9, 6.0);
        TPaletaPrvok plocha7 = new TPaletaPrvok(1, 3, "plocha7", "src/srafy7.png", Color.decode("#340000"), 7.0, 9, 7.0);
        TPaletaPrvok plocha8 = new TPaletaPrvok(1, 3, "plocha8", "src/srafy8.png", Color.decode("#340000"), 8.0, 9, 8.0);
        TPaletaPrvok plocha9 = new TPaletaPrvok(1, 3, "plocha9", "src/srafy9.png", Color.decode("#340000"), 9.0, 9, 9.0);
        TPaletaPrvok plocha10 = new TPaletaPrvok(1, 3, "plocha10", "src/srafy10.png", Color.decode("#340000"), 10.0, 10, 1.0);
        TPaletaPrvok plocha11 = new TPaletaPrvok(1, 3, "plocha11", "src/srafy11.png", Color.decode("#340000"), 11.0, 11, 1.0);
        TPaletaPrvok plocha12 = new TPaletaPrvok(1, 3, "plocha12", "src/srafy12.png", Color.decode("#340000"), 12.0, 12, 1.0);

        volnePlochy.add(plocha1);
        volnePlochy.add(plocha2);
        volnePlochy.add(plocha3);
        volnePlochy.add(plocha4);
        volnePlochy.add(plocha5);
        volnePlochy.add(plocha6);
        volnePlochy.add(plocha7);
        volnePlochy.add(plocha8);
        volnePlochy.add(plocha9);
        volnePlochy.add(plocha10);
        volnePlochy.add(plocha11);
        volnePlochy.add(plocha12);

        for (int i = 0; i < volneBody.size(); i++) {
            for (int j = 0; j < paleta.paletaPrvky.size(); j++) {
                if ((paleta.paletaPrvky.get(j).img).toLowerCase().contains(volneBody.get(i).img.toLowerCase())) {
                    TPaletaPrvok zmazat = volneBody.get(i);
                    System.out.println("Ma zmazat" + volneBody.get(i).img + " == " + paleta.paletaPrvky.get(j).img);
                    volneBody.get(i).setNum(999);
                }
            }
        }

        Iterator itr = volneBody.iterator();
        while (itr.hasNext()) {

            TPaletaPrvok ss;
            ss = (TPaletaPrvok) itr.next();
            if (ss.num == 999) {
                itr.remove();
            }
        }

        for (int i = 0; i < volneCiary.size(); i++) {
            for (int j = 0; j < paleta.paletaPrvky.size(); j++) {
                if ((paleta.paletaPrvky.get(j).img).toLowerCase().contains(volneCiary.get(i).img.toLowerCase())) {
                    TPaletaPrvok zmazat = volneCiary.get(i);
                    System.out.println("Ma zmazat" + volneCiary.get(i).img + " == " + paleta.paletaPrvky.get(j).img);
                    volneCiary.get(i).setNum(999);
                }
            }
        }

        Iterator itr1 = volneCiary.iterator();
        while (itr1.hasNext()) {

            TPaletaPrvok ss1;
            ss1 = (TPaletaPrvok) itr1.next();
            if (ss1.num == 999) {
                itr1.remove();
            }
        }

        for (int i = 0; i < volnePlochy.size(); i++) {
            for (int j = 0; j < paleta.paletaPrvky.size(); j++) {
                if ((paleta.paletaPrvky.get(j).img).toLowerCase().contains(volnePlochy.get(i).img.toLowerCase())) {
                    TPaletaPrvok zmazat = volnePlochy.get(i);
                    System.out.println("Ma zmazat" + volnePlochy.get(i).img + " == " + paleta.paletaPrvky.get(j).img);
                    volnePlochy.get(i).setNum(999);
                }
            }
        }

        Iterator itr2 = volnePlochy.iterator();
        while (itr2.hasNext()) {

            TPaletaPrvok ss1;
            ss1 = (TPaletaPrvok) itr2.next();
            if (ss1.num == 999) {
                itr2.remove();
            }
        }
    }
 /**
     * 
     * @return  vráti vykreslenú grafiku
     */
    public Graphics getGraph() {
        return graph;
    }
}
