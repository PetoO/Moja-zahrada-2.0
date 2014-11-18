/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tis;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Branislav Heriban
 */

public class TPlan implements MouseListener, MouseMotionListener, KeyListener {

    //tabulka do ktorej sa načítajú údaje z excellu 
    private HashMap<String, ArrayList<String>> excel = new HashMap<String, ArrayList<String>>();

    //prvy vložené v ploche
    private ArrayList<TPrvok> prvky;
    
    //história zmien pre Undo, Redo a jej index
    private ArrayList<ByteArrayOutputStream> historia;
    private int historiaI = -1;

    //cvaky
    TBod aktivnyBod;
    public int aktivnyPrvokInt;
    public TCiara aktivnaCiara;
    public TPlocha aktivnaPlocha;
    //cvaky

    //rozmery su rozmery pozemku v pixeloch cize cm, x,y a x1,y1 su pomocne premenne vyuzivane pri kliknuti, 
    //moved premenne sa menie pri tahani plochy po obrazovke
    int rozmerX = 1000;
    int rozmerY = 600;
    int x = 1, MovedX = 0;
    int y = 1, MovedY = 0;
    int width =0, heigth =0; //sirka a vyska GPanelu
    private double x1 = 0.0, y1 = 0.0;

    boolean showGrass = true;
    private boolean draging = false;

    private int uhol = 1, kliknutyBod = -1;
    double scale;

    private TPrvok edited = null;
    private boolean deleteEdited = false;

    private BufferedImage ruz, ruz1, ruz2, ruz3, ruz4;
    private BufferedImage poz;
    private BufferedImage leg;

    final int METERS_TO_PIXELS = 100;

    /**
     * konštruktor triedy TPlan, nepotrebuje parametre, všetko potrebné sa nastaví priamo v ňom
     * vytvoria sa v nom potrebne datove struktury a nacitaju s a v nom obrazkz
     */
    
    public TPlan() {

        prvky = new ArrayList<TPrvok>();
        historia = new ArrayList<ByteArrayOutputStream>();
        scale = 1;
        

        aktivnyBod = new TBod(0, 0, null, Color.red, 2, 1, 2);
        aktivnaCiara = new TCiara(0, 0, 0, 0, null, Color.red, 0, 0);
        aktivnaPlocha = new TPlocha(0, 0, 0, 0, null, Color.red, 2, 2);
        aktivnyPrvokInt = 0;
        

        change();
        

        //nacitat obrazok pozadia, ruzice a legnedy
        try {
            poz = ImageIO.read(new File("src/background.bmp"));
            ruz = ImageIO.read(new File("src/ruzica1.png"));
            ruz1 = ImageIO.read(new File("src/ruzica1.png"));
            ruz2 = ImageIO.read(new File("src/ruzica2.png"));
            ruz3 = ImageIO.read(new File("src/ruzica3.png"));
            ruz4 = ImageIO.read(new File("src/ruzica4.png"));
            //leg = ImageIO.read(new File("legenda.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(TPlan.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metóda určená na vykreslenie plochy
     * @param g grafika plochy, volá sa z metódy paint prvku JPanel
     * vykreslí canvas, jednotlivé prvky v ňom, mriežku, smerovú ružicu, mierku a taktiež aj legendu
     */
    public void paint(Graphics2D g) {
        paintBackgroundColor(g);
        if (MovedX < 20 && MovedX >= 0) {
            MovedX = 0;
        }
        if (MovedY < 100 && MovedY >= 80) {
            MovedY = 80;
        }
        
   
        g.translate(MovedX, MovedY);

        
        
        paintBackground(g);
        paintGrid(g);
        for (TPrvok prvok : prvky) {
            prvok.draw((Graphics2D) g);

        }

        //cvaky
        if (aktivnyBod != null) {
            aktivnyBod.draw((Graphics2D) g);
        }
        if (aktivnaCiara != null) {
            aktivnaCiara.draw((Graphics2D) g);
        }
        if (aktivnaPlocha != null) {
            aktivnaPlocha.draw((Graphics2D) g);
        }

        //cvaky
        g.translate(-MovedX, -MovedY);

        paintRuz(g);

    }
    /**
     * Funkcia na zoomovanie plochy
     * @param plus booleanovsky parameter ktory oznacuje zoom in (ked je true) alebo zoom out (ked false)
     */

    public void zoom(boolean plus) {
        double factor;
        double centerX = MovedX+(rozmerX*scale)/2;
        double centerY = MovedY+(rozmerY*scale)/2;
        if (plus) {
            
            scale *= 1.25;
            factor = 1.25;
        } else {
           
            scale /= 1.25;
            factor = 1 / 1.25;
        }

        MovedX -= MovedX+(rozmerX*scale)/2 - centerX;
        MovedY -= MovedY+(rozmerY*scale)/2 - centerY;
            
        for (TPrvok prvok : prvky) {
            if (prvok instanceof TBod) {
                TBod aPrvok = ((TBod) prvok);
                aPrvok.velkost *= factor;
                aPrvok.presun((int) (factor * aPrvok.x), (int) (factor * aPrvok.y));
            }

            if (prvok instanceof TCiara) {
                TCiara aPrvok = ((TCiara) prvok);
                int i = 0;
                for (TCiara.Bod bod : aPrvok.body) {
              
                    aPrvok.presun(i, (factor * bod.x), (factor * bod.y));
                    i++;
                }
            }

            if (prvok instanceof TPlocha) {
                TPlocha aPrvok = ((TPlocha) prvok);
                for (int i = 0; i < aPrvok.body.size(); i++) {
                    aPrvok.zmenVelkost(i, (factor * aPrvok.body.get(i).x), (factor * aPrvok.body.get(i).y));
                }              
            }
        }
    }

    /**
     * otočí plánik o 90° v smere alebo proti smeru hodinových ručičiek 
     * @param clockws ak true tak v smere hodinovych ruciciak, ak false tak proti smeru
     */
    public void otoc(boolean clockws) {
        uhol++;
        double centerX = MovedX+(rozmerX*scale)/2;
        double centerY = MovedY+(rozmerY*scale)/2;
        if (uhol == 5) {
            uhol = 1;
        }

        switch (uhol) {
            case 1:
                ruz = ruz1;
                break;
            case 2:
                ruz = ruz2;
                break;
            case 3:
                ruz = ruz3;
                break;
            case 4:
                ruz = ruz4;
                break;
            default:
                ruz = ruz1;
                break;
        }

        if (clockws) {
            int pomY = (int) (rozmerY * scale);

            for (TPrvok prvok : prvky) {
                if (prvok instanceof TBod) {
                    TBod aPrvok = ((TBod) prvok);
                    aPrvok.presun((int) (pomY - (aPrvok.y)), (int) (aPrvok.x));
                }

                if (prvok instanceof TCiara) {
                    TCiara aPrvok = ((TCiara) prvok);
                    int i = 0;
                    for (TCiara.Bod bod : aPrvok.body) {
                        aPrvok.presun(i, pomY - (bod.y), (bod.x));
                        i++;
                    }
                }

                if (prvok instanceof TPlocha) {
                    TPlocha aPrvok = ((TPlocha) prvok);
                    for (int i = 0; i < aPrvok.body.size(); i++) {
                        aPrvok.zmenVelkost(i, (pomY - aPrvok.body.get(i).y), (aPrvok.body.get(i).x));
                    }
                   
                }
            }
            int pom = rozmerY;
            rozmerY = rozmerX;
            rozmerX = pom;
        } else {
            uhol--;
            otoc(true);
            otoc(true);
            otoc(true);
        }
        MovedX -= MovedX+(rozmerX*scale)/2 - centerX;
        MovedY -= MovedY+(rozmerY*scale)/2 - centerY;
    }
    
    /**
     * zobrazí popup tabuľku s prvkami na ploche, ich meno a obrazok (legendu)
     */

    public void zobrazTabulku() {
        JFrame frame = new JFrame();
        frame.setSize(300, 500);
        
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);        
        
        JTabulka G = new JTabulka(prvky, scrollPane);
        
        
        frame.setContentPane(G);
        
        
     
        frame.setVisible(true);
        frame.repaint();
    }

    /**
     * prida prvok do plochy
     * @param prvok prvok ktorý sa vklada do plochy
     * @return vrati true ak sa podarilo pridanie prvku, ak nie vrati false
     */
    public boolean addPrvok(TPrvok prvok) {
        if (prvok != null) {
            boolean result = prvky.add(prvok);
            change();
            return result;
        }
        return false;
    }

    /**
     * vymaze prvok z provkov na ploche
     * @param prvok prvok sa ma vymazat z plochy
     * @return vrati true ak sa podarilo vymazanie prvku, ak nie vrati false
     */
    public boolean delPrvok(TPrvok prvok) {
        if (prvok != null) {
            edited = null;
            boolean result = prvky.remove(prvok);
            change();
            return result;
        }
        return false;
    }

    /**
     * @param index
     * @return vrati smernik na prvok s danym indexom
     */
    public TPrvok getPrvok(int index) {
        return prvky.get(index);
    }

    /**
     * @return vráti zoznam prvkov na ploche
     */
    public ArrayList<TPrvok> getList() {
        return prvky;
    }

    /**
     * @return vrati aktualny zoom
     */
    public double getScale() {
        return scale;
    }

    /**
     * nakreslí do pozadia tapetu
     * @param g grafika 
     */
    private void paintBackground(Graphics g) {
        if (poz != null && showGrass) {
            Graphics2D g2 = (Graphics2D) g;
            Rectangle r = new Rectangle(0, 0, poz.getWidth(), poz.getHeight());
            g2.setPaint(new TexturePaint(poz, r));
            Rectangle rect = new Rectangle(0, 0, (int) (rozmerX * scale) + 1, (int) (rozmerY * scale) + 1);
            g2.fill(rect);
        } else {
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, (int) (rozmerX * scale) + 1, (int) (rozmerY * scale) + 1);
            g.setColor(Color.gray);
        }
   }

    /**
     * vyfarbí pozadie pracovnej plochy na sivo
     * @param g grafika
     */
    private void paintBackgroundColor(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.gray);
        g2.fill(TIS.vr.getBounds());
    }

    /**
     * nakreslí mriežku do plochy
     * @param g grafika
     */
    private void paintGrid(Graphics2D g) {
        float[] patt = new float[4];
        patt[0] = 10.0f;
        patt[1] = 1 + (float) 5;
        patt[2] = 1 * (float) 2.5;
        patt[3] = patt[1];
        g.setStroke(new BasicStroke(1, // Width
                BasicStroke.CAP_SQUARE, // End cap
                BasicStroke.JOIN_MITER, // Join style
                10.0f, // Miter limit
                patt, // Dash pattern
                0.0f)
        );
        g.setColor(Color.gray);

        for (int i = 0, j = 0; j < rozmerY * scale - scale * METERS_TO_PIXELS; i++) {
            j = (int) (i * scale * METERS_TO_PIXELS);
            g.drawLine(0, j, (int) (rozmerX * scale), j);
        }
        for (int i = 0, j = 0; j < rozmerX * scale - scale * METERS_TO_PIXELS; i++) {
            j = (int) (i * scale * METERS_TO_PIXELS);
            g.drawLine(j, 0, j, (int) (rozmerY * scale));
        }
    }

    /**
     * nakreslí ružicu do plochy
     * @param g grafika
     */
    private void paintRuz(Graphics g) {
        if (ruz != null) {
            //int w = canvas.getWidth(); toto netreba ani
            int iw = ruz.getWidth();
            int ih = ruz.getHeight();
            g.drawImage(ruz, 0, TIS.vr.getHeight() - ruz.getHeight() - 55, iw, ih, TIS.G);
            //ruzica bude vlavo dole
        }
    }

    /**
     * nakreslí legendu  do plochy
     * @param g grafika
       
        }
    }*/

    /**
     * @return vráti aktuálnu výśku (po zoome)
     */
    int getHeight() {
        return (int) (rozmerY * scale);
    }

    /**
     * @return vráti aktuálnu Šírku (po zoome)
     */
    int getWidth() {
        return (int) (rozmerX * scale);
    }

    /**
     * prepne kliknutý prvok do edit módu, alebo vyne edit mod pri kliknuti do plochy
     * @param e 
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        for (TPrvok prvok : prvky) {
            if (prvok instanceof TBod) {
                TBod bod = (TBod) prvok;
                if (bod.isNear(e.getX() - MovedX, e.getY() - MovedY)) {
                    if (edited != null) {
                        edited.edited = false;
                    }
                    edited = prvok;
                    edited.edited = true;
                    if (e.getClickCount() == 2) {
                        showInfo(prvok.meno);
                    }
                    break;
                }
            }

            if (prvok instanceof TPlocha) {
                TPlocha plocha = (TPlocha) prvok;
                System.out.print(prvok.meno);
                if (plocha.isNear(e.getX() - MovedX, e.getY() - MovedY)) {

                    if (edited != null) {
                        edited.edited = false;
                    }
                    edited = prvok;
                    edited.edited = true;
                    if (e.getClickCount() == 2) {
                        showInfo(prvok.meno);
                    }
                    break;
                }
            }
            if (prvok instanceof TCiara) {
                TCiara ciara = (TCiara) prvok;
                if (ciara.isNear(e.getX() - MovedX, e.getY() - MovedY)) {
                    if (edited != null) {
                        edited.edited = false;
                    }
                    edited = prvok;
                    edited.edited = true;
                    if (e.getClickCount() == 2) {
                        showInfo(prvok.meno);
                    }
                    break;
                }
            }
            if (edited != null) {
                edited.edited = false;
            }
            edited = null;
        }

        TIS.vr.repaint();
    }

    /**
     * zistí sa na ktorý bod prvku sa kliklo, prápadne či sa nekliklo mimo prvku
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {
        TIS.G.requestFocus();
        x = e.getXOnScreen() - MovedX;
        y = e.getYOnScreen() - MovedY;

        if (edited != null) {
            if (edited instanceof TBod) {
                TBod prvok = ((TBod) edited);
                x1 = prvok.x;
                y1 = prvok.y;
                kliknutyBod = 1;
                double distance = Math.sqrt(Math.pow((e.getX() - MovedX - x1), 2) + Math.pow((e.getY() - MovedY - y1), 2));
                if ((distance > (prvok.velkost/2 + 8)) && (distance < (prvok.velkost/2 + 12))) {
                    kliknutyBod = 0;
                    prvok.presun((int)(x1),(int)(y1));
                }
                return;
            }
            if (edited instanceof TPlocha) {
                TPlocha prvok = ((TPlocha) edited);
                x1 = prvok.body.get(0).x;
                y1 = prvok.body.get(0).y;

                kliknutyBod = prvok.pointNear(e.getX() - MovedX, e.getY() - MovedY);

                if (kliknutyBod > -1) {
                    x1 = prvok.body.get(kliknutyBod).x;
                    y1 = prvok.body.get(kliknutyBod).y;
                }

                if ((kliknutyBod == prvok.body.size() - 1) && (e.isAltDown())) {
                    prvok.vymazK();
                    kliknutyBod--;
                }
                if ((kliknutyBod == 0) && (e.isAltDown())) {
                    prvok.vymazZ();
                }
                if ((kliknutyBod == prvok.body.size() - 1) && (e.isControlDown())) {
                    prvok.novyBod(e.getX() - MovedX, e.getY() - MovedY);
                    kliknutyBod++;
                }
                if ((kliknutyBod == 0) && (e.isControlDown())) {
                    prvok.novyBodZ(e.getX() - MovedX, e.getY() - MovedY);
                }
                return;
            }

            if (edited instanceof TCiara) {
                TCiara prvok = ((TCiara) edited);
                x1 = prvok.body.get(0).x;
                y1 = prvok.body.get(0).y;
                kliknutyBod = prvok.pointNear(e.getX() - MovedX, e.getY() - MovedY);

                if (kliknutyBod > -1) {
                    x1 = prvok.body.get(kliknutyBod).x;
                    y1 = prvok.body.get(kliknutyBod).y;
                }

                if ((kliknutyBod == (prvok.body.size() - 1)) && (e.isAltDown())) {
                    prvok.vymazK();
                    kliknutyBod--;
                }
                if ((kliknutyBod == 0) && e.isAltDown()) {
                    prvok.vymazZ();
                }
                if ((kliknutyBod == (prvok.body.size() - 1)) && e.isControlDown()) {
                    prvok.novyBod(e.getX() - MovedX, e.getY() - MovedY);
                    kliknutyBod++;
                }
                if ((kliknutyBod == 0) && e.isControlDown()) {
                    prvok.novyBodZ(e.getX() - MovedX, e.getY() - MovedY);
                }
                return;
            }
            kliknutyBod = -1;
        }
    }
    

    /**
     * ak bol prvok pusteny mimo plochu tak sa vymaze
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (draging) {
            if (deleteEdited) {
                delPrvok(edited);
                deleteEdited = false;
            } else {
                change();
            }
            draging = false;
        }
        TIS.vr.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * bud posunie bod prvku alebo cely prvok, v pripade bodu ho zoomuje
     * @param e 
     */
    @Override
    public void mouseDragged(MouseEvent e) {
       TIS.vr.repaint();
        if (edited == null) {
            MovedX = e.getXOnScreen() - x;
            MovedY = e.getYOnScreen() - y;
        } else {
            if (edited instanceof TBod) {
                TBod prvok = ((TBod) edited);
                if (kliknutyBod > 0) {
                    prvok.presun((int) x1 + e.getXOnScreen() - x - MovedX, (int) y1 + e.getYOnScreen() - y - MovedY);
                    if (jeMimoPlochy(x1 + e.getXOnScreen() - x - MovedX, y1 + e.getYOnScreen() - y - MovedY)) {
                        deleteEdited = true;
                    }
                }
                if (kliknutyBod == 0) {
                    double distance = Math.sqrt(Math.pow((e.getX() - MovedX - (x1 + prvok.velkost) ), 2) + Math.pow((e.getY() - MovedY - (y1 + prvok.velkost)), 2));
                    prvok.velkost = distance / 2;
                    //prvok.presun((int) (x1 - prvok.velkost), (int) (y1 - prvok.velkost));
                }
            }
            if (edited instanceof TPlocha) {
                TPlocha prvok = ((TPlocha) edited);
                if (kliknutyBod >= 0) {
                    prvok.zmenVelkost(kliknutyBod, e.getX() - MovedX, e.getY() - MovedY);
                } else {
                    prvok.presun(x1 + e.getXOnScreen() - x - MovedX, y1 + e.getYOnScreen() - y - MovedY);
                    if (jeMimoPlochy(x1 + e.getXOnScreen() - x - MovedX, y1 + e.getYOnScreen() - y - MovedY)) {
                        deleteEdited = true;
                    }
                }
            }
            if (edited instanceof TCiara) {
                TCiara prvok = ((TCiara) edited);
                if (kliknutyBod >= 0) {
                    prvok.presun(kliknutyBod, x1 + e.getXOnScreen() - x - MovedX, y1 + e.getYOnScreen() - y - MovedY);
                    System.out.println(kliknutyBod + " z " + prvok.body.size());
                } else {
                    prvok.posunCiaru(x1 + e.getXOnScreen() - x - MovedX, y1 + e.getYOnScreen() - y - MovedY);
                    if (jeMimoPlochy(x1 + e.getXOnScreen() - x - MovedX, y1 + e.getYOnScreen() - y - MovedY)) {
                        deleteEdited = true;
                    }
                }
            }
        }
        draging = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    /**
     * uloží rozpracovaný plánik do súboru s názvom nazov
     * @param name nazov ulozenia
     */
    public void Save(String name) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(name)));
            oos.writeObject(prvky);
            oos.writeObject(rozmerX);
            oos.writeObject(rozmerY);
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * načíta rozpracovaný plánik zo súboru
     * @param name nazov nacitaneho suboru
     */
    public void Load(String name) {
        ObjectOutputStream oos;
        try {
            InputStream is = new FileInputStream(new File(name)); //moyno je to len string
            ObjectInputStream ois = new ObjectInputStream(is);
            prvky = (ArrayList<TPrvok>) ois.readObject();
            rozmerX = (int) ois.readObject();
            rozmerY = (int) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * nova plocha, t.j. vymaze prvky z plochy
     */
    public void New() {
        prvky = new ArrayList<TPrvok>();
    }
    
    /**
     * vráti dozadu posledný krok
     */
    public void Undo() {
        historiaI--;
        if (historiaI > -1) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(historia.get(historiaI).toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                prvky = (ArrayList<TPrvok>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(TPlan.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * obnoví posledný krok
     */
    public void Redo() {
        historiaI++;
        if (historiaI < historia.size()) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(historia.get(historiaI).toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                prvky = (ArrayList<TPrvok>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(TPlan.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * updatne historiu pre potreby undo a redo
     */
    private void change() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(prvky);
            historia.add(baos);
        } catch (IOException ex) {
            Logger.getLogger(TPlan.class.getName()).log(Level.SEVERE, null, ex);
        }
        historiaI++;
    }

    /**
     * zisti ci je prvok mimo plochy
     * @param x1 x prvku
     * @param y1 z prvku
     * @return vrati true ak je prvok mimo plochy, inak false
     */
    private boolean jeMimoPlochy(double x1, double y1) {
        if (x1 < getWidth() + 50 && y1 < getHeight() + 50 && x1 > -50 && y1 > -50) {
            return false;
        }
        return true;
    }

    /**
     * nastavi rozmery plochy na x a y
     * @param x
     * @param y 
     */
    public void nastavXY(int x, int y) {
        rozmerX = x;
        rozmerY = y;
    }

    /**
     * @author Lucia Budinská 
     * načíta excelovskú tabuľku do hashmapy - vo formáte kľúč = meno prvku a hodnota = arraylist stringov z celého riadku
     * ak nejaká bunka v tabuľke nie je vyplnená, do hashmapy sa zapíše prázdny string
     * @param file názov súboru, ktorý ideme čítať 
     */
    public void nacitaj(String file) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            int rows; //pocet riadkov
            rows = sheet.getPhysicalNumberOfRows();
            int cols = 0; // pocet stlpcov
            int tmp = 0;
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) {
                        cols = tmp;
                    }
                }
            }

            for (int r = 0; r < rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    String meno = "";
                    ArrayList<String> list = new ArrayList<String>();
                    for (int c = 0; c < cols; c++) {
                        cell = row.getCell(c);
                        if (cell != null) {
                            if (c == 0) {
                                meno = cell.getStringCellValue();
                            }
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    list.add("" + cell.getNumericCellValue());
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    list.add(cell.getStringCellValue());
                                    break;
                                default:
                                    list.add("");
                                    break;         
                            }
                        }
                    }
                    excel.put(meno, list); //do hashmapy priradí celý riadok tabuľku s kľúčom meno prvku   
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

    }
    
    /**
     * zobrazi tabulku s informáciami o označenom prvku (tie sa načítajú z excelu)
     * @param meno menu vybraného prvku
     */
    void showInfo(String meno) {
        JInfo info = new JInfo();
        info.setVisible(true);
        info.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if (excel.get(meno) != null) {
            info.setInfo(excel.get(meno));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /**
     * pri kliknutí delete sa vymaze oznaceny prvok
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 127) {
            delPrvok(edited);
            TIS.vr.repaint();
        }
        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
