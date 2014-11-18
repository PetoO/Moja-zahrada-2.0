/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tis;

import java.awt.Color;
import java.awt.Graphics2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jana
 */
public class TPlochaTest {
    
    public TPlochaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    TPlocha plocha = new TPlocha(10, 10.1, 99.9, 99.8, "plocha", Color.black, 2.3, 3);
    TPlocha plocha1 = new TPlocha(0, 0, 5, 5, "plocha", Color.BLACK, 2.3, 3);
    
    @Test
    public void testPlocha() {
        System.out.println("presun");
        
        assertTrue(null != plocha);
        
        assertEquals(plocha.meno, "plocha");
        assertEquals(plocha.hrubka, 2.3, 0.0000001);
        assertEquals(plocha.farba, Color.black);
        assertEquals(plocha.styl, 3);
        assertEquals(plocha.body.get(0).x, 10, 0.000001); 
        assertEquals(plocha.body.get(0).y, 10.1, 0.000001);
        assertEquals(plocha.body.get(1).x, 99.9, 0.000001);
        assertEquals(plocha.body.get(1).y, 99.8, 0.000001);     
        
    }
    
    @Test
    public void testPresun() {
        System.out.println("presun");
        
        plocha.presun(31.4, 23.5);
        
        assertEquals(plocha.body.get(0).x, 31.4, 0.000001);
        assertEquals(plocha.body.get(0).y, 23.5, 0.000001);
        assertEquals(plocha.body.get(1).x, 121.3, 0.000001);
        assertEquals(plocha.body.get(1).y, 113.2, 0.000001);
        
    }

    /**
     * Test of zmenVelkost method, of class TPlocha.
     */
    @Test
    public void testZmenVelkost() {
        System.out.println("zmenVelkost");
       
        assertTrue(plocha1.zmenVelkost(0, 1, 2));
    assertEquals(plocha1.body.size(), 2);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(2 == plocha1.body.get(0).y);
    assertTrue(5 == plocha1.body.get(1).x);
    assertTrue(5 == plocha1.body.get(1).y);
    
    assertTrue(plocha1.zmenVelkost(0, 1, 2));
    assertEquals(plocha1.body.size(), 2);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(2 == plocha1.body.get(0).y);
    assertTrue(5 == plocha1.body.get(1).x);
    assertTrue(5 == plocha1.body.get(1).y);
    
    assertTrue(plocha1.zmenVelkost(1, 1, 2));
    assertEquals(plocha1.body.size(), 2);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(2 == plocha1.body.get(0).y);
    assertTrue(1 == plocha1.body.get(1).x);
    assertTrue(2 == plocha1.body.get(1).y);
    
    assertTrue(plocha1.zmenVelkost(1, 6, 6));
    assertEquals(plocha1.body.size(), 2);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(2 == plocha1.body.get(0).y);
    assertTrue(6 == plocha1.body.get(1).x);
    assertTrue(6 == plocha1.body.get(1).y);
    
    plocha1.novyBod(7, 3);
    assertEquals(plocha1.body.size(), 3);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(2 == plocha1.body.get(0).y);
    assertTrue(6 == plocha1.body.get(1).x);
    assertTrue(6 == plocha1.body.get(1).y);
    assertTrue(7 == plocha1.body.get(2).x);
    assertTrue(3 == plocha1.body.get(2).y);
    
    assertTrue(plocha1.zmenVelkost(2, 6, 6));
    assertEquals(plocha1.body.size(), 3);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(2 == plocha1.body.get(0).y);
    assertTrue(6 == plocha1.body.get(1).x);
    assertTrue(6 == plocha1.body.get(1).y);
    assertTrue(6 == plocha1.body.get(2).x);
    assertTrue(6 == plocha1.body.get(2).y);
    }

    /**
     * Test of isNear method, of class TPlocha.
     */
    @Test
    public void testIsNear() {
        System.out.println("isNear");
       
        plocha.novyBod(10, 97.5); //lebo polygon
        
        assertTrue(plocha.isNear(25.6, 33.1));
        assertTrue(plocha.isNear(20.9, 62.8));
        assertFalse(plocha.isNear(105, 100.4));
        assertFalse(plocha.isNear(60.9, 30.4));
        
    }

    /**
     * Test of pointNear method, of class TPlocha.
     */
    @Test
    public void testPointNear() {
        System.out.println("pointNear");
       
        assertEquals(plocha.pointNear(9, 10), 0); //10, 10.1, 99.9, 99.8,
        assertEquals(plocha.pointNear(11, 11), 0);
        assertEquals(plocha.pointNear(100, 99), 1);
        assertEquals(plocha.pointNear(100, 100), 1);
        assertEquals(plocha.pointNear(105, 100), -1);
        assertEquals(plocha.pointNear(60, 30), -1);
        
    }

    /**
     * Test of novyBod method, of class TPlocha.
     */
    @Test
    public void testNovyBod() {
        System.out.println("novyBod");
       
         plocha1.novyBod(0.0, 0.0);
    assertEquals(plocha1.body.size(), 2);
    assertTrue(0 == plocha1.body.get(0).x);
    assertTrue(0 == plocha1.body.get(0).y);
    assertTrue(5 == plocha1.body.get(1).x);
    assertTrue(5 == plocha1.body.get(1).y);
    
    plocha1.novyBod(10.3, 9); 
    assertEquals(plocha1.body.size(), 3);
    assertTrue(0 == plocha1.body.get(0).x);
    assertTrue(0 == plocha1.body.get(0).y);
    assertTrue(5 == plocha1.body.get(1).x);
    assertTrue(5 == plocha1.body.get(1).y);
    assertTrue(10.3 == plocha1.body.get(2).x);
    assertTrue(9 == plocha1.body.get(2).y);
    
    }

    /**
     * Test of novyBodZ method, of class TPlocha.
     */
    @Test
    public void testNovyBodZ() {
        System.out.println("novyBodZ");
      
        plocha1.novyBodZ(1, 1);
    assertEquals(plocha1.body.size(), 3);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(1 == plocha1.body.get(0).y);
    assertTrue(0 == plocha1.body.get(1).x);
    assertTrue(0 == plocha1.body.get(1).y);
    assertTrue(5 == plocha1.body.get(2).x);
    assertTrue(5 == plocha1.body.get(2).y);
    
    plocha1.novyBodZ(1, 1);
    assertEquals(plocha1.body.size(), 4);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(1 == plocha1.body.get(0).y);
    assertTrue(1 == plocha1.body.get(1).x);
    assertTrue(1 == plocha1.body.get(1).y);
    assertTrue(0 == plocha1.body.get(2).x);
    assertTrue(0 == plocha1.body.get(2).y);
    assertTrue(5 == plocha1.body.get(3).x);
    assertTrue(5 == plocha1.body.get(3).y);
        
    }

    /**
     * Test of vymazK method, of class TPlocha.
     */
    @Test
    public void testVymazK() {
        System.out.println("vymazK");
        
        assertEquals(plocha.body.size(), 2);
        plocha.vymazK();
        
        assertEquals(plocha.body.get(0).x, 10, 0.000001); 
        assertEquals(plocha.body.get(0).y, 10.1, 0.000001);
        assertEquals(plocha.body.get(1).x, 99.9, 0.000001);
        assertEquals(plocha.body.get(1).y, 99.8, 0.000001);    
        assertEquals(plocha.body.size(), 2);
        
        plocha.novyBodZ(10, 97.5);
        
        assertEquals(plocha.body.get(0).x, 10, 0.000001); 
        assertEquals(plocha.body.get(0).y, 97.5, 0.000001);
        assertEquals(plocha.body.get(1).x, 10, 0.000001); 
        assertEquals(plocha.body.get(1).y, 10.1, 0.000001);
        assertEquals(plocha.body.get(2).x, 99.9, 0.000001);
        assertEquals(plocha.body.get(2).y, 99.8, 0.000001);  
        assertEquals(plocha.body.size(), 3);
        
        plocha.vymazK();
        
        assertEquals(plocha.body.size(), 2);
        assertEquals(plocha.body.get(0).x, 10, 0.000001); 
        assertEquals(plocha.body.get(0).y, 97.5, 0.000001);
        assertEquals(plocha.body.get(1).x, 10, 0.000001); 
        assertEquals(plocha.body.get(1).y, 10.1, 0.000001);
        
        plocha.novyBod(8, 97.5);
        assertEquals(plocha.body.get(0).x, 10, 0.000001); 
        assertEquals(plocha.body.get(0).y, 97.5, 0.000001);
        assertEquals(plocha.body.get(1).x, 10, 0.000001); 
        assertEquals(plocha.body.get(1).y, 10.1, 0.000001);      
        assertEquals(plocha.body.get(2).x, 8, 0.000001); 
        assertEquals(plocha.body.get(2).y, 97.5, 0.000001);
        assertEquals(plocha.body.size(), 3);
    }

    /**
     * Test of vymazZ method, of class TPlocha.
     */
    @Test
    public void testVymazZ() {
        System.out.println("vymazZ");
        
        plocha1.novyBodZ(2, 2); 
    plocha1.novyBodZ(1, 1);
    assertEquals(plocha1.body.size(), 4);
    assertTrue(1 == plocha1.body.get(0).x);
    assertTrue(1 == plocha1.body.get(0).y);
    assertTrue(2 == plocha1.body.get(1).x);
    assertTrue(2 == plocha1.body.get(1).y);
    assertTrue(0 == plocha1.body.get(2).x);
    assertTrue(0 == plocha1.body.get(2).y);
    assertTrue(5 == plocha1.body.get(3).x);
    assertTrue(5 == plocha1.body.get(3).y);
    
    plocha1.vymazZ();
    assertEquals(plocha1.body.size(), 3);
    assertTrue(2 == plocha1.body.get(0).x);
    assertTrue(2 == plocha1.body.get(0).y);
    assertTrue(0 == plocha1.body.get(1).x);
    assertTrue(0 == plocha1.body.get(1).y);
    assertTrue(5 == plocha1.body.get(2).x);
    assertTrue(5 == plocha1.body.get(2).y);
    
    plocha1.vymazZ();
    assertEquals(plocha1.body.size(), 2);
    assertTrue(0 == plocha1.body.get(0).x);
    assertTrue(0 == plocha1.body.get(0).y);
    assertTrue(5 == plocha1.body.get(1).x);
    assertTrue(5 == plocha1.body.get(1).y);
    
    plocha1.vymazZ();
    assertEquals(plocha1.body.size(), 2);
    assertTrue(0 == plocha1.body.get(0).x);
    assertTrue(0 == plocha1.body.get(0).y);
    assertTrue(5 == plocha1.body.get(1).x);
    assertTrue(5 == plocha1.body.get(1).y);
        
    }
    
}
