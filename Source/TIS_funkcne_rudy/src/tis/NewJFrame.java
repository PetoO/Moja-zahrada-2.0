/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.ScrollPane;
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import javax.swing.JOptionPane;

/**
 *
 * @author Rudolf Cvacho
 * Tento JFrame slúži na vykreslenie celej aplikácie
 * Skladá sa z Paletky, plániku a pravého bloku na ovládanie plániku
 */
public class NewJFrame extends javax.swing.JFrame {

    final JFileChooser fc = new JFileChooser();
    public final JPanel panel = new JPanel();
    JScrollPane scrollPane = new JScrollPane(panel);
 
    TPaleta paleta;
    boolean ciaraNeVytvorena;
    boolean plochaNeVytvorena;
    int marginLeft;
    int widthButton;
    int prvokI;
    int marginbutton;
  
  /**
     * 
     * Painter slúźi na prekreslovanie paletky po každej zmene.
     *Napríklad pri pridávaní alebo vymazávaní prvkov
     */
   
    public void painter() {
        try {
            paletaKresli();
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 /**
     * procedúra paletaKresli sa stará o vykreslenie celej paletky. 
     * Vygeneruje obrázky, pod nimi názvy prvkov a po kliknutí na prvok menu na zmazanie alebo editovanie prvku.
    *  
     */
    public void paletaKresli() throws IOException {
        panel.removeAll();
        for (int i = 0; i < paleta.paletaPrvky.size(); ++i) {
//            System.out.println("nacita vsetky");
            prvokI = i;
            paleta.paletaPrvky.get(i).setNum(i);
            final TPaletaPrvok prvok = paleta.paletaPrvky.get(i);
            final JButton b = new JButton("");
            Image img;
            if (prvok.typ == 3) {
                img = ImageIO.read(new File("src/srafy" + prvok.styl + ".png")).getScaledInstance(60, 50, 50);
            } else {
                img = ImageIO.read(new File(prvok.img));
            }
            b.setIcon(new ImageIcon(img));
            b.setBounds(marginLeft + i * widthButton + i * 10, 0, widthButton, 50);
            b.setMaximumSize(new Dimension(20, 30));
            b.setToolTipText(prvok.name);
            b.setBorder(BorderFactory.createEmptyBorder());
            b.setBorder(new EmptyBorder(1, 1, 1, 1));
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setVerticalTextPosition(SwingConstants.BOTTOM);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setText(prvok.name);
            b.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            b.setHorizontalAlignment(SwingConstants.LEFT);

            final javax.swing.JMenuBar jMenuBar;

            jMenuBar = new javax.swing.JMenuBar();
            jMenuBar.removeAll();
            javax.swing.JMenu jMenu;
            jMenu = new javax.swing.JMenu();
            jMenu.setText("menu");

            final javax.swing.JMenuItem jMenuItemName;
            jMenuItemName = new javax.swing.JMenuItem();
            jMenuItemName.setText("Zmenit nazov");
            jMenuItemName.setBackground(Color.gray);
            jMenuItemName.setForeground(Color.white);
            jMenuItemName.setMargin(new Insets(0, 0, 10, 0));
            jMenuItemName.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("zmenit nazov");
                    String name = JOptionPane.showInputDialog(jMenuItemName, "Zadajte novy nazov polozky", null);
                    prvok.name = name;
                    painter();
                    paleta.Save(paleta.paletaPrvky);
                }
            });
            javax.swing.JMenuItem jMenuItemDel;
            jMenuItemDel = new javax.swing.JMenuItem();
            jMenuItemDel.setText("Zmazat " + prvok.name);
            jMenuItemDel.setBackground(Color.gray);
            jMenuItemDel.setForeground(Color.white);
            jMenuItemDel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("zmazal");
                    if (prvok.typ == 1) {
                        paleta.G.volneBody.add(new TPaletaPrvok(prvok.num, prvok.typ, prvok.name, prvok.img, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost));
                    } else if (prvok.typ == 2) {
                        paleta.G.volneCiary.add(new TPaletaPrvok(prvok.num, prvok.typ, prvok.name, prvok.img, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost));
                    } else if (prvok.typ == 3) {
                        paleta.G.volnePlochy.add(new TPaletaPrvok(prvok.num, prvok.typ, prvok.name, prvok.img, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost));
                    }
                    paleta.paletaPrvky.remove(prvok);
                    jMenuBar.setVisible(false);
                    painter();
                }
            });
            jMenuBar.setBorderPainted(true);

            jMenuBar.add(jMenuItemName);
            jMenuBar.add(jMenuItemDel);
            jMenuBar.setVisible(false);
            final LayoutManager grid = new GridLayout(0, 1);
            jMenuBar.setLayout(grid);
            panel.add(jMenuBar, BorderLayout.EAST);

            b.addMouseListener(new java.awt.event.MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.isControlDown()) {
                        System.out.println(e.getX() + " " + prvok.name);
                        jMenuBar.setVisible(true);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseReleased(MouseEvent e) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                }

                @Override
                public void mouseEntered(MouseEvent e) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseExited(MouseEvent e) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            });
            b.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    if (prvok.typ == 1) {
                        TBod bod = new TBod(evt.getX() + marginLeft + prvok.num * widthButton + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue(), evt.getY() - TIS.planik.MovedY, prvok.name, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost, TIS.planik.sadenie);
                        TIS.planik.aktivnyBod = bod;
                        TIS.vr.setInformationLabel(bod.meno);
                    } else if (prvok.typ == 2) {
                        TBod bod = new TBod(evt.getX() + marginLeft + prvok.num * widthButton + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue(), evt.getY() - TIS.planik.MovedY, prvok.name, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost, TIS.planik.sadenie);
                        TIS.planik.aktivnyBod = bod;
                        TIS.vr.setInformationLabel(bod.meno);
                    } else if (prvok.typ == 3) {
                        TBod bod = new TBod(evt.getX() + marginLeft + prvok.num * widthButton + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue(), evt.getY() - TIS.planik.MovedY, prvok.name, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost, TIS.planik.sadenie);
                        TIS.planik.aktivnyBod = bod;
                        TIS.vr.setInformationLabel(bod.meno);
                    }

                    TIS.vr.repaint();
                }
            });
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    if (!(evt.isControlDown())) {

                        if (prvok.typ == 1) {
                            TBod boda = new TBod(evt.getX() + marginLeft + prvok.num * widthButton + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue(), evt.getY() - TIS.planik.MovedY, prvok.name, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost, TIS.planik.sadenie);
                            TIS.planik.addPrvok(boda);
                            TBod bod = new TBod(-9999, 0, prvok.name, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost,TIS.planik.sadenie);
                            TIS.planik.aktivnyBod = bod;
                        } else if (prvok.typ == 2) {
//                            if (TIS.planik.aktivnaCiara.body.get(0).x == -9999) {
                            //TCiara ciara = new TCiara(evt.getX() + prvok.num * widthButton + marginLeft + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue(), evt.getY() - TIS.planik.MovedY, evt.getX() + marginLeft + prvokI * widthButton, evt.getY(), prvok.name, prvok.farba, prvok.hrubka, prvok.styl);
                            double xax = evt.getX() + prvok.num * widthButton + marginLeft + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue();
                            TCiara ciara = new TCiara(xax, evt.getY() - TIS.planik.MovedY, xax, evt.getY() - TIS.planik.MovedY, prvok.name, prvok.farba, prvok.hrubka, prvok.styl);
                            TIS.planik.aktivnaCiara = ciara;
                            //TIS.planik.aktivnyPrvokInt = 1;
                            TBod bod = new TBod(-9999, 0, prvok.name, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost,TIS.planik.sadenie);
                            TIS.planik.aktivnyBod = bod;
                            ciaraNeVytvorena = true;
                        } else if (prvok.typ == 3) {
//                           if (TIS.planik.aktivnaPlocha.body.get(0).x == -9999) {
                            // TPlocha plocha = new TPlocha(evt.getX() + prvok.num * widthButton + marginLeft + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue(), evt.getY() - TIS.planik.MovedY, evt.getX() + prvok.num * widthButton + marginLeft + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue(), evt.getY() - TIS.planik.MovedY, prvok.name, prvok.farba, prvok.hrubka, prvok.styl);
                            double xax = evt.getX() + prvok.num * widthButton + marginLeft + prvok.num * marginbutton - TIS.planik.MovedX - scrollPane.getHorizontalScrollBar().getValue();
                            TPlocha plocha = new TPlocha(xax, evt.getY() - TIS.planik.MovedY, xax, evt.getY() - TIS.planik.MovedY, prvok.name, prvok.farba, prvok.hrubka, prvok.styl);
                            TIS.planik.aktivnaPlocha = plocha;
                            TBod bod = new TBod(-9999, 0, prvok.name, prvok.farba, prvok.hrubka, prvok.styl, prvok.velkost,TIS.planik.sadenie);
                            TIS.planik.aktivnyBod = bod;
//                            }
                            plochaNeVytvorena = true;
                        }
                    }
                    TIS.vr.repaint();
                }
            });

           
            TIS.G.addMouseMotionListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseMoved(java.awt.event.MouseEvent evt) {
                    if (ciaraNeVytvorena) {
                       //TIS.planik.aktivnaCiara.presun((TIS.planik.aktivnaCiara.body.size() - 1), (evt.getX() - TIS.planik.MovedX), (evt.getY() - TIS.planik.MovedY));
                       TIS.planik.aktivnaCiara.body.get(TIS.planik.aktivnaCiara.body.size() - 1).x = (evt.getX() - TIS.planik.MovedX);
                       TIS.planik.aktivnaCiara.body.get(TIS.planik.aktivnaCiara.body.size() - 1).y = (evt.getY() - TIS.planik.MovedY);
                    }
                    if (plochaNeVytvorena) {
                        TIS.planik.aktivnaPlocha.body.get(TIS.planik.aktivnaPlocha.body.size() - 1).x = evt.getX() - TIS.planik.MovedX;
                        TIS.planik.aktivnaPlocha.body.get(TIS.planik.aktivnaPlocha.body.size() - 1).y = evt.getY() - TIS.planik.MovedY;

                    }

                    TIS.vr.repaint();
                }
            });
            TIS.G.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {

                    if (ciaraNeVytvorena) {
                       TIS.planik.aktivnaCiara.novyBod(evt.getX() - TIS.planik.MovedX, evt.getY() - TIS.planik.MovedY);
                        if (evt.getClickCount() == 2) {
                            ciaraNeVytvorena = false;
                            TIS.planik.aktivnaCiara.vymazK();
//                            TIS.planik.aktivnaCiara.vymazZ();
                            TIS.planik.addPrvok(TIS.planik.aktivnaCiara);
                            prvok.typ = -1;
                           TIS.planik.aktivnaCiara = new TCiara(-9999, 0, -9999, 0, prvok.name, prvok.farba, prvok.hrubka, prvok.styl);
                        }
                        TIS.vr.repaint();
                       
                    }
                    if (plochaNeVytvorena) {
                        TIS.planik.aktivnaPlocha.novyBod(evt.getX() - TIS.planik.MovedX, evt.getY() - TIS.planik.MovedY);
                        if (evt.getClickCount() == 2) {
                            plochaNeVytvorena = false;
                            TIS.planik.aktivnaPlocha.spojene = true;
                            TIS.planik.aktivnaPlocha.vymazK();
                            TIS.planik.aktivnaPlocha.vymazZ();
                            TIS.planik.addPrvok(TIS.planik.aktivnaPlocha);
                            TIS.planik.aktivnaPlocha = new TPlocha(-9999, 0, -9999, 0, prvok.name, prvok.farba, prvok.hrubka, prvok.styl);
                        }
                        TIS.vr.repaint();
                    }
                }
            });
           panel.add(b, BorderLayout.WEST);
          
        }

        panel.updateUI();

//        scrollPane.removeAll();
//        scrollPane = new JScrollPane(panel);
//        add(scrollPane);
    }
    JPanel contentPane;
/**
 *
 * Konštruktor volá funkciu paletaKresli a vloží ju do scrollPanelu
 * Vloží plánik do Jframe
 */
    public NewJFrame() throws IOException {
        System.out.println("NewJframe");
        TIS.planik.MovedY = 100;
        marginLeft = 0;
        marginbutton = 17;
        widthButton = 50;
        setLayout(new java.awt.GridLayout(2, 2));
        paleta = new TPaleta();

        if (paleta.paletaPrvky == null) {
            try {
                InputStream is = new FileInputStream(new File("paleta")); 
                ObjectInputStream ois = new ObjectInputStream(is);
                paleta.paletaPrvky = (ArrayList<TPaletaPrvok>) ois.readObject();

            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewJFrame.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        painter();

        initComponents();
        setVisible(true);
        setExtendedState(NewJFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        add(scrollPane);
        add(TIS.G);
        jButton5.setSize(200, 30);
        jButton6.setSize(200, 30);
        jToggleButton1.setSize(200, 30);
        jTextField1.setSize(100, 30);
        jTextField2.setSize(100, 30);
        jButton7.setSize(200, 30);
        jButton5.move(getWidth() - jButton5.getWidth() - 5, 0);
        jButton6.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight());
        
        jToggleButton1.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight() + jButton6.getHeight());
        jButton7.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight() + jButton6.getHeight() + jToggleButton1.getHeight() + jTextField1.getHeight());
        jButton8.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight() + 2*jButton6.getHeight() + jToggleButton1.getHeight() + jTextField1.getHeight());
        jButton9.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight() + 3*jButton6.getHeight() + jToggleButton1.getHeight() + jTextField1.getHeight());
       
        jTextField1.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight() + jButton6.getHeight() + jToggleButton1.getHeight());
        jTextField2.move(getWidth() - jButton5.getWidth() + jTextField1.getWidth() - 5, jButton5.getHeight() + jButton6.getHeight() + jToggleButton1.getHeight());
        try {
            Image img = ImageIO.read(new File("src/right.png"));
            jButton3.setOpaque(false);
            jButton3.setContentAreaFilled(false);
            jButton3.setBorderPainted(false);
            jButton3.setSize(img.getWidth(rootPane), img.getWidth(rootPane));
            jButton3.move(getWidth() - jButton3.getWidth() - 5, getHeight() - jButton3.getHeight() - 55);
            jButton3.setIcon(new ImageIcon(img));

            Image imgl = ImageIO.read(new File("src/left.png"));
            jButton4.setOpaque(false);
            jButton4.setContentAreaFilled(false);
            jButton4.setBorderPainted(false);
            jButton4.setSize(imgl.getWidth(rootPane), imgl.getWidth(rootPane));
            jButton4.move(getWidth() - jButton4.getWidth() - jButton3.getWidth() - 5, getHeight() - jButton4.getHeight() - 55);
            jButton4.setIcon(new ImageIcon(imgl));

            Image img2 = ImageIO.read(new File("src/zoom+.png"));
            jButton1.setOpaque(false);
            jButton1.setContentAreaFilled(false);
            jButton1.setBorderPainted(false);
            jButton1.setSize(img2.getWidth(rootPane), img2.getWidth(rootPane));
            jButton1.move(getWidth() - jButton1.getWidth() - 5, getHeight() - jButton1.getHeight() - jButton4.getHeight() - 55);
            jButton1.setIcon(new ImageIcon(img2));

            Image img3 = ImageIO.read(new File("src/zoom-.png"));
            jButton2.setOpaque(false);
            jButton2.setContentAreaFilled(false);
            jButton2.setBorderPainted(false);
            jButton2.setSize(img3.getWidth(rootPane), img3.getWidth(rootPane));
            jButton2.move(getWidth() - jButton2.getWidth() - jButton1.getWidth() - 5, getHeight() - jButton2.getHeight() - jButton4.getHeight() - 55);
            jButton2.setIcon(new ImageIcon(img3));

        } catch (IOException ex) {
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas1 = new java.awt.Canvas();
        jPanel1 = new javax.swing.JPanel();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        popupMenu1 = new java.awt.PopupMenu();
        popupMenu2 = new java.awt.PopupMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        jMenuItem4.setText("jMenuItem4");

        jMenuItem5.setText("jMenuItem5");

        popupMenu1.setLabel("popupMenu1");

        popupMenu2.setLabel("popupMenu2");

        jMenuItem9.setText("jMenuItem9");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jButton1.setText("+");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("-");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("doprava");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton4.setText("dolava");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jButton5.setText("Legenda");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Vycentrovať");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jToggleButton1.setText("Čisté pozadie");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton7.setText("Nastaviť rozmery v metroch");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Pridat do paletky");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Uloz do PDF");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel1.setText("Info Panel");
        jLabel1.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        jButton10.setText("Tabuľka prvkov");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sadenie1.png"))); // NOI18N
        jToggleButton2.setSelected(true);
        jToggleButton2.setToolTipText("");
        jToggleButton2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/sadenie1.png"))); // NOI18N
        jToggleButton2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sadenie1.png"))); // NOI18N
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sadenie2.png"))); // NOI18N
        jToggleButton3.setToolTipText("");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sadenie3.png"))); // NOI18N
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Zobraz mriežku");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem10.setText("New");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuItem6.setText("Save");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Load");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);
        jMenu1.add(jSeparator1);

        jMenuItem8.setText("Exit");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Undo");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem12.setText("Redo");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuItem13.setLabel("Change background");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem13);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton5)
                .addGap(8, 8, 8)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addGap(28, 28, 28)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jToggleButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)))
        );

        jButton8.getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        TIS.planik.zoom(true);
        repaint();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        TIS.planik.zoom(false);
        repaint();
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        TIS.planik.otoc(true);
        repaint();
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        TIS.planik.otoc(false);
        repaint();
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        TIS.planik.zobrazLegendu();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        TIS.planik.MovedY = (int) (getHeight() / 2 - (TIS.planik.rozmerY * TIS.planik.scale) / 2);
        TIS.planik.MovedX = (int) ((getWidth() - 200) / 2 - (TIS.planik.rozmerX * TIS.planik.scale) / 2);
        repaint();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if (TIS.planik.showGrass) {
            TIS.planik.showGrass = false;
        } else {
            TIS.planik.showGrass = true;
        }
        repaint();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        jButton5.move(getWidth() - jButton5.getWidth() - 5, 0);
        jButton6.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight());
        jToggleButton1.move(getWidth() - jButton5.getWidth() - 5, jButton5.getHeight() + jButton6.getHeight());
        jButton4.move(getWidth() - jButton4.getWidth() - jButton3.getWidth() - 5, getHeight() - jButton4.getHeight() - 55);
        jButton3.move(getWidth() - jButton3.getWidth() - 5, getHeight() - jButton3.getHeight() - 55);
        jButton1.move(getWidth() - jButton1.getWidth() - 5, getHeight() - jButton1.getHeight() - jButton4.getHeight() - 55);
        jButton2.move(getWidth() - jButton2.getWidth() - jButton1.getWidth() - 5, getHeight() - jButton2.getHeight() - jButton4.getHeight() - 55);
        scrollPane.setBounds(0, 0, getWidth() - 208, 100);
    }//GEN-LAST:event_formComponentResized

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            TIS.planik.Load(fc.getSelectedFile().toString());
        }
        repaint();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            TIS.planik.Save(fc.getSelectedFile().toString());
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        exit(0);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        TIS.planik.New();
        repaint();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        TIS.planik.Undo();
        repaint();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        TIS.planik.Redo();
        repaint();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            String t1 = jTextField1.getText().replaceAll(",", ".");
            String t2 = jTextField2.getText().replaceAll(",", ".");
            double c1 = Double.parseDouble(t1);
            double c2 = Double.parseDouble(t2);
            TIS.planik.nastavXY((int) (c1 * 100), (int) (c2 * 100));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Zlý číselný formát");
        }
        repaint();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        paleta.pridajDoPaletky();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        exportToPdf export = new exportToPdf();
        try {
            export.PrintFrameToPDF(this);
        } catch (PrinterException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        TIS.planik.zmenPozadie();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        TIS.planik.zobrazTabulku();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
        TIS.planik.nastavSadenie(0);
        nastavSadenie(0);
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        TIS.planik.nastavSadenie(1);
        nastavSadenie(1);
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // TODO add your handling code here:
        TIS.planik.nastavSadenie(2);
        nastavSadenie(2);
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        TIS.planik.mriezka=jCheckBox1.isSelected();
        
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new NewJFrame().setVisible(true);
                    System.out.println("novy jframe");

                } catch (IOException ex) {
                    Logger.getLogger(NewJFrame.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void setGraph() {
       
    }

    public void setDimension() {
        jTextField1.setText("" + TIS.planik.rozmerX / 100);
        jTextField2.setText("" + TIS.planik.rozmerY / 100);
    }
    
    public void setInformationLabel(String s) {
        jLabel1.setText(s);
   }
    
    //peto
    
    /**
     * nastavi text na jTextField2 a jTextField1
     */
    public void setDimensionText(String s1, String s2) {
        jTextField1.setText(s1);
        jTextField2.setText(s2);
    }
    

    public String getDimensionText1() {
        return jTextField1.getText();
        
    }

    public String getDimensionText2() {
        return jTextField2.getText();
        
    }
    
        /**
     * zmeni zobrazenie tlacidiel sadenia
     */
    
    public void nastavSadenie(int i) {
        if(i==0){
            jToggleButton2.setSelected(true);
            jToggleButton4.setSelected(false);
            jToggleButton3.setSelected(false);
        }
        if(i==1){
            jToggleButton2.setSelected(false);
            jToggleButton4.setSelected(false);
            jToggleButton3.setSelected(true);
        }
        if(i==2){
            jToggleButton2.setSelected(false);
            jToggleButton4.setSelected(true);
            jToggleButton3.setSelected(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Canvas canvas1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private java.awt.PopupMenu popupMenu1;
    private java.awt.PopupMenu popupMenu2;
    // End of variables declaration//GEN-END:variables

}
