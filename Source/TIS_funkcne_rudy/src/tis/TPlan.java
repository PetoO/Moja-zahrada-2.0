/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RowFilter.Entry;
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
    public HashMap<String, ArrayList<String>> excel = new HashMap<String, ArrayList<String>>();

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
    
    
    //peto
    // bod ktorym sa zobrazia kolizie
    TBod kolizia;
    
    //vsetky kolizie
    //ArrayList<TBod> kolizie;
    HashMap<TBod, ArrayList<TBod>> kolizie;
    
    public boolean zobrazKolizie;

    public boolean textura;
    
    public int sadenie=0;
    
    public boolean mriezka;
    
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

    TPrvok edited = null;
    private boolean deleteEdited = false;

    private BufferedImage ruz, ruz1, ruz2, ruz3, ruz4;
    private BufferedImage poz;
    private BufferedImage leg;

    final int METERS_TO_PIXELS = 100;
    
    DecimalFormat df = new DecimalFormat("#0.#");

    /**
     * konštruktor triedy TPlan, nepotrebuje parametre, všetko potrebné sa nastaví priamo v ňom
     * vytvoria sa v nom potrebne datove struktury a nacitaju s a v nom obrazkz
     */
    
    public TPlan() {

        prvky = new ArrayList<TPrvok>();
        historia = new ArrayList<ByteArrayOutputStream>();
        scale = 1;
        mriezka=true;

        aktivnyBod = new TBod(0, 0, null, Color.red, 2, 1, 2, sadenie);
        aktivnaCiara = new TCiara(0, 0, 0, 0, null, Color.red, 0, 0);
        aktivnaPlocha = new TPlocha(0, 0, 0, 0, null, Color.red, 2, 2);
        aktivnyPrvokInt = 0;
        
        kolizie = new HashMap<TBod, ArrayList<TBod>>();
        
        zobrazKolizie = true; // false

        change(); 
        
        textura=true;

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
        if(mriezka){
            paintGrid(g);
        }
        
        for (TPrvok prvok : prvky) {
            prvok.draw((Graphics2D) g);
            //just testing
            if(prvok instanceof TBod){
                TBod bod= (TBod) prvok;
                //TBod bood= new TBod(0, 0, null, Color.BLACK, 2, 1,(int) bod.velkost+bod.hrubka/2+30);
                //bood.presun((int)(bod.x - (bod.velkost/2) - bod.hrubka + 15), (int) (bod.y- (bod.velkost/2) - bod.hrubka + 15));  
                //kolizie.add(bod);
            }
        }
        nastavKolizie();

        //cvaky
        if (aktivnyBod != null) {
            aktivnyBod.draw((Graphics2D) g);
            //System.out.println("kreslim aktivny bod");
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

        kresliMierku((Graphics2D) g);
        
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
    
    public void zobrazLegendu(){
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

    public void zobrazTabulku() {
        JFrame frame = new TabulkaPrvkov(prvky, excel);
        frame.setVisible(true);
    }

    /**
     * prida prvok do plochy
     * @param prvok prvok ktorý sa vklada do plochy
     * @return vrati true ak sa podarilo pridanie prvku, ak nie vrati false
     */
    public boolean addPrvok(TPrvok prvok) {
        //zobrazKolizie = true;
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
            
            nastavKolizie();
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
            //textura=false;
            if(textura){
                Graphics2D g2 = (Graphics2D) g;
                Rectangle r = new Rectangle(0, 0, poz.getWidth(), poz.getHeight());
                g2.setPaint(new TexturePaint(poz, r));
                Rectangle rect = new Rectangle(0, 0, (int) (rozmerX * scale) + 1, (int) (rozmerY * scale) + 1);
                g2.fill(rect);
            } else {
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(poz, 0, 0, getWidth() - 1, getHeight() - 1, 0, 0, poz.getWidth() - 1, poz.getHeight() - 1, null);
            }
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
                    TIS.vr.setInformationLabel(prvok.meno);
                    zobrazKolizie = true;                                       // OZNACIL SOM PRVOK, ZOBRAZIM KOLIZIE
                    edited.edited = true;
                    System.out.println((bod.hrubka+bod.velkost));
                    nastavSadenie(bod.sadenie);
                    
                    if (e.getClickCount() == 2) {
                        showInfo(prvok.meno);
                    }
                    break;
                }
                zobrazKolizie = false;
            }

            if (prvok instanceof TPlocha) {
                TPlocha plocha = (TPlocha) prvok;
                System.out.print(prvok.meno);
                if (plocha.isNear(e.getX() - MovedX, e.getY() - MovedY)) {

                    if (edited != null) {
                        edited.edited = false;
                    }
                    edited = prvok;
                    TIS.vr.setInformationLabel(prvok.meno+" "  +df.format(plocha.getArea()/(scale*scale)) + " m2");      //vypise rozlohu pri kliknuti na plcohu
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
                    TIS.vr.setInformationLabel(prvok.meno+" "+ df.format(ciara.getLength()/scale) + " m");      //po kliknuti na ciaru sa ukaze jej dlzka
                    edited.edited = true;
                    if (e.getClickCount() == 2) {
                        showInfo(prvok.meno);
                    }
                    break;
                }
            }
            if (edited != null) {
                edited.edited = false;
                TIS.vr.setInformationLabel( "info"); // po kliknuti mimo prvku sa ukaze info text
                zobrazKolizie = false;                                                          // odznacim prvok zobrazi zrusim kolizie
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
                zruskoliziu(edited);
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
                // sem pojde sadenie,, a pripajanaie bodov na dane body ..boomba  
                TBod prvok = ((TBod) edited);
                if (kliknutyBod > 0) { 
                    TIS.vr.setInformationLabel(prvok.meno); 
                    if(sadenie==0){
                        e.getX();
                        //prvok.presun((int) x1 + e.getX() - x - MovedX, (int) y1 + e.getY() - y - MovedY);
                        prvok.presun((int) x1 + e.getXOnScreen() - x - MovedX, (int) y1 + e.getYOnScreen() - y - MovedY);
                    } else{
                        pripoj(prvok,MovedX,MovedY, e.getXOnScreen(),e.getYOnScreen(),x,y);
                    }
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
                    TIS.vr.setInformationLabel(prvok.meno+" "  + df.format(prvok.getArea()/scale) + " m2");    // vpise rozlohu pri meneni plochy
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
                   //System.out.println(kliknutyBod + " z " + prvok.body.size());
                    TIS.vr.setInformationLabel( prvok.meno+" "  +df.format(prvok.getLength()/scale) + " m");                                        // rata automaticky dlzku kreslenej ciary
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
            //oos.writeObject(new ArrayList<BufferedImage>().add(poz));
            //oos.writeObject(textura);
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
            //ArrayList<BufferedImage> pozz = (ArrayList<BufferedImage>) ois.readObject();
            //poz = pozz.get(0);
            //textura = (boolean ) ois.readObject();
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
                   //System.out.println(meno + list);
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
        JInfo info = new JInfo(prvky);
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
            zruskoliziu(edited);
            delPrvok(edited);
            TIS.vr.repaint();
        }
        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    //peto
    
    
     /**
     * nakresli mierku v metroch 
     */
    private void kresliMierku(Graphics2D g) {
            int iw = TIS.vr.getWidth();
            int ih = TIS.vr.getHeight();
            int y=ih-100;
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Color.BLACK);
            g.drawLine(120, y, iw-200 , y);
            double m= 100*scale;
            int i=0;
            g.setFont(new Font("Serif", Font.PLAIN, 18) );
            
            double c=120;
            g.drawString("meter" ,(int) c , y-10);
            
            while(c<=iw-200){
                g.drawLine((int) c, y-5, (int) c , y+5);
                g.drawString(i+"" ,(int) c-4 , y+20);
                c+=m;
                i++;
            };
            
            //g.drawImage(ruz, 0, TIS.vr.getHeight() - ruz.getHeight() - 55, iw, ih, TIS.G);
            //ruzica bude vlavo dole
    }
    
     /**
     * zavola dialog na zmenu pozadia 
     */
    public void zmenPozadie(){
        
        ZmenaPozadia frame = new ZmenaPozadia(this);
        frame.setVisible(true);
        frame.repaint();
    }
    
     /**
     * zmeni pozadia, nastavi velkost planika 
     */
    public void zmenPozadieNa(String cesta, double sirka, double vyska, boolean textura){
        try {
            
            TIS.planik.nastavXY((int) (sirka * 100), (int) (vyska * 100));
            TIS.vr.setDimensionText(Double.toString(sirka),Double.toString(vyska));
            this.textura=textura;
            poz = ImageIO.read(new File(cesta));
        } 
        catch (Exception e) {
            JFrame frame= new JFrame();
            JOptionPane.showMessageDialog(frame, "Zly typ obrazka");
        }
        
    }
        
     /**
     * nastavi kolizie pre neznasanlivost, a nedostatok miesta
     */
    public void nastavKolizie (){
        for(int i=0; i < prvky.size(); i++){
            for(int j=0; j < prvky.size(); j++){
                if(i!=j){
                    if((prvky.get(i) instanceof TBod) && (prvky.get(j) instanceof TBod) ){
                        TBod bod1 = (TBod) prvky.get(i);
                        TBod bod2 = (TBod) prvky.get(j);    
                        
                        if(!kolizie.containsKey(bod1)){
                            kolizie.put(bod1, new ArrayList<TBod>());
                        }
                        
                        if(!kolizie.containsKey(bod2)){
                            kolizie.put(bod2, new ArrayList<TBod>());
                        }
                        //System.out.println(bod1.meno+" "+bod2.meno+" "+Math.sqrt(Math.pow((double)bod1.x-bod2.x, 2)+Math.pow((double)bod1.y-bod2.y, 2))+" "+(bod1.velkost+bod1.hrubka+bod2.velkost+bod2.hrubka));
                        // treba dorobit, konstanta 30 je len pre stromy
                        if((((bod1.velkost+bod1.hrubka+bod2.velkost+bod2.hrubka)/2)+30*(scale))>=Math.sqrt(Math.pow((double)bod1.x-bod2.x, 2)+Math.pow((double)bod1.y-bod2.y, 2))){
                            //zistim ci su kolizie medzi kermi a stromami inak zistujem kolizie medzi neznasanlivymi rastlinami
                            if(bod1.sadenie!=0 && bod2.sadenie!=0 && bod1.sadenie==bod2.sadenie){
                                break;
                            }
                            
                            if(
                            excel.containsKey(bod1.meno) && excel.containsKey(bod2.meno)&&(
                            excel.get(bod1.meno).get(2).equals("strom") || 
                            excel.get(bod1.meno).get(2).equals("ker") ) && (
                            excel.get(bod2.meno).get(2).equals("strom") || 
                            excel.get(bod2.meno).get(2).equals("ker")  )
                            ){                 
                                if(!kolizie.get(bod1).contains(bod2)){
                                    kolizie.get(bod1).add(bod2);
                                }
                                if(!kolizie.get(bod2).contains(bod1)){
                                    kolizie.get(bod2).add(bod1);
                                }
                            }
                            //zistujem neznasanlivost 
                            else{
                                if(excel.containsKey(bod1.meno) && excel.containsKey(bod2.meno)){
                                    if(excel.get(bod1.meno).get(12).toLowerCase().contains(excel.get(bod2.meno).get(1).toLowerCase())){
                                        if(!kolizie.get(bod1).contains(bod2)){
                                            kolizie.get(bod1).add(bod2);
                                        }
                                        if(!kolizie.get(bod2).contains(bod1)){
                                            kolizie.get(bod2).add(bod1);
                                        }
                                        break;
                                    }
                                    if(excel.get(bod2.meno).get(12).toLowerCase().contains(excel.get(bod1.meno).get(1).toLowerCase())){
                                        if(!kolizie.get(bod1).contains(bod2)){
                                            kolizie.get(bod1).add(bod2);
                                        }
                                        if(!kolizie.get(bod2).contains(bod1)){
                                            kolizie.get(bod2).add(bod1);
                                        }
                                        break;
                                    }

                                }    
                            }    
                            
                        } else {
                            // ak su v kolizii, vyhodim
                            if(kolizie.get(bod1).contains(bod2)){
                                kolizie.get(bod1).remove(bod2);
                            }
                            if(kolizie.get(bod2).contains(bod1)){
                                kolizie.get(bod2).remove(bod1);
                            }
                        }
                    }
                }
            }
        }
    }                  
    
     /**
     * nastavi sadenie ktore sa aplikuje na nove prvky pridane do planika
     */
    public void nastavSadenie(int sadenie){
        this.sadenie=sadenie;
        System.out.println("zmena sadenia na: "+ sadenie);
        TIS.vr.nastavSadenie(sadenie);
    }
    
     /**
     * zrusi kolizie medzi prvkami z ktorych nejaky uz neexistuje
     */

    public void zruskoliziu(TPrvok edited) {
        if(!(edited instanceof TBod)){
            return;
        }
        
        System.out.println("zrus");
        if(kolizie.containsKey(edited)){
            kolizie.remove(edited);
        }
        for (Map.Entry<TBod, ArrayList<TBod>> entry : kolizie.entrySet()) {
            TBod key = entry.getKey();
            ArrayList<TBod> value = entry.getValue();
            if(value.contains(edited)){
                value.remove(edited);
                System.out.println("removed" + edited.meno);
            }
        }           
    }

     /**
     * podla druhu sadenia pomaha nastavit zrovnanie oznaceneho prvku a najblizsieho prvku rovnakeho druhu a sadenia 
     */
    public void pripoj(TBod prvok, int MovedX, int MovedY, int xOnScreen, int yOnScreen, int x, int y) {
        //xxx a yyy: kde by sa mal prvok posunut 
        int xxx=(int) x1 + xOnScreen - x - MovedX; 
        int yyy=(int) y1 + yOnScreen - y - MovedY;
        int distance=1000000;
        TBod prvok2=null;
        TBod prvok3;
        int dd;
        System.out.println(xxx +" : " + yyy);
        // najdem najblizzsi prvok rovnakeho sadenia
        for (TPrvok prvokk : prvky){
            if(!(prvokk.equals(prvok)) && (prvokk instanceof TBod)){
                System.out.println("mam TBod nerovny s povodnym");
                prvok3 = (TBod) prvokk;
                if(prvok3.sadenie!=prvok.sadenie || prvok3.velkost!=prvok.velkost){
                    System.out.println(prvok3.meno+" "+prvok3.sadenie +" a "+ prvok.meno+" "+prvok.sadenie+  " nemaju rovnake sadenie");
                     continue;
                }
                dd = (int) Math.sqrt(Math.pow((double)prvok.x-prvok3.x, 2)+Math.pow((double)prvok.y-prvok3.y, 2));
                if(dd<(prvok3.hrubka+prvok3.velkost+36*scale) && dd<distance){
                    System.out.println("vzdialenost prvkov je mensia ako 20 a ako najmensia distance");
                    distance=(int)dd;
                    prvok2=prvok3;
                }
            }
        }
        System.out.println(prvok2!=null);
  
        if(prvok2==null){
            System.out.println("prvok2 je null ");
            prvok.presun(xxx,yyy);
            return;
        }
        
        if(sadenie==1){
            System.out.println("vsetko ok, prvok by sa mal presunut");
            //4 body na ktore sa bude lepit
            //0 x - 1 y
            int[][] body = new int[4][2];
            body[0][0]= (int) prvok2.x;
            body[0][1]= (int) (prvok2.y-(prvok2.velkost+prvok2.hrubka+30));
            
            body[1][0]= (int) (prvok2.x+prvok2.velkost+prvok2.hrubka+30);
            body[1][1]= (int) (prvok2.y);
            
            body[2][0]= (int) prvok2.x;
            body[2][1]= (int) (prvok2.y+prvok2.velkost+prvok2.hrubka+30);
            
            body[3][0]= (int) (prvok2.x-(prvok2.velkost+prvok2.hrubka+30));
            body[3][1]= (int) (prvok2.y);
            
            int[] dist = new int[4];
            
            int naj=-5;
            int najpom=1000000;
            for(int i=0; i<dist.length; i++){      
                dist[i]= (int) Math.sqrt(Math.pow((double)body[i][0]-xxx, 2)+Math.pow((double)body[i][1]-yyy, 2));
                if(dist[i]< najpom){
                    najpom=dist[i];
                    naj=i;
                }
            }

            if(dist[naj]>(prvok2.hrubka+prvok2.velkost+16*scale)){
                System.out.println("xxxx"+dist[naj]);
                prvok.presun(xxx,yyy);
            }else{
                System.out.println("yyyyy");
                prvok.presun((int)body[naj][0],(int)body[naj][1]);
            }
            return;
        }
        if(sadenie==2){
            System.out.println("vsetko ok, prvok by sa mal presunut");
            //4 body na ktore sa bude lepit
            //0 x - 1 y
            int[][] body = new int[6][2];
            body[0][0]= (int) (prvok2.x+(prvok2.velkost+prvok2.hrubka+30)/2);
            body[0][1]= (int) (prvok2.y-(prvok2.velkost+prvok2.hrubka+30));
            
            body[1][0]= (int) (prvok2.x+prvok2.velkost+prvok2.hrubka+30);
            body[1][1]= (int) (prvok2.y);
            
            body[2][0]= (int) (prvok2.x+(prvok2.velkost+prvok2.hrubka+30)/2);
            body[2][1]= (int) (prvok2.y+prvok2.velkost+prvok2.hrubka+30);
            
            body[3][0]= (int) (prvok2.x-(prvok2.velkost+prvok2.hrubka+30)/2);
            body[3][1]= (int) (prvok2.y+prvok2.velkost+prvok2.hrubka+30);
            
            body[4][0]= (int) (prvok2.x-(prvok2.velkost+prvok2.hrubka+30));
            body[4][1]= (int) (prvok2.y);
            
            body[5][0]= (int) (prvok2.x-(prvok2.velkost+prvok2.hrubka+30)/2);
            body[5][1]= (int) (prvok2.y-(prvok2.velkost+prvok2.hrubka+30));
            
            int[] dist = new int[6];
            
            int naj=-5;
            int najpom=1000000;
            for(int i=0; i<dist.length; i++){      
                dist[i]= (int) Math.sqrt(Math.pow((double)body[i][0]-xxx, 2)+Math.pow((double)body[i][1]-yyy, 2));
                if(dist[i]< najpom){
                    najpom=dist[i];
                    naj=i;
                }
            }

            if(dist[naj]>(prvok2.hrubka+prvok2.velkost+20*scale)){
                System.out.println("xxxx"+dist[naj]);
                prvok.presun(xxx,yyy);
            }else{
                System.out.println("yyyyy");
                prvok.presun((int)body[naj][0],(int)body[naj][1]);
            }
            return;
        }
        
        prvok.presun(xxx,yyy);
    }
       
}