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
public class TCiaraTest {
    
    public TCiaraTest() {
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

    /**
     * Test of novyBod method, of class TCiara.
     */
    
    TCiara ciara1 = new TCiara(22.3 , 5.2, 14.7, 34.9, "ciara1", Color.white, 9.7, 4);
    TCiara ciara2 = null;
    
    //@janak
    @Test
    public void testCiara(){
    
        assertTrue(ciara1 != null);
        assertTrue(ciara1.getName() == "ciara1");
        assertTrue(ciara1.farba == Color.white);
        assertTrue(ciara1.hrubka == 9.7);
        assertTrue(ciara1.styl == 4);
        assertTrue(ciara1.body.size() == 2);
        assertTrue(22.3 == ciara1.body.get(0).x);
        assertTrue(5.2 == ciara1.body.get(0).y);
        assertTrue(14.7 == ciara1.body.get(1).x);
        assertTrue(34.9 == ciara1.body.get(1).y);
               
        assertTrue(ciara2 == null);
        
    }
    
    @Test
    public void testNovyBod() {
        System.out.println("novyBod");
        
        assertFalse(ciara1.novyBod(22.3, 5.2)); //warum, warum??
        assertTrue(ciara1.novyBod(10.3, 9));
               
        assertTrue(22.3 == ciara1.body.get(0).x);
        assertTrue(5.2 == ciara1.body.get(0).y);
        assertTrue(14.7 == ciara1.body.get(1).x);
        assertTrue(34.9 == ciara1.body.get(1).y);
        assertTrue(10.3 == ciara1.body.get(2).x);
        assertTrue(9 == ciara1.body.get(2).y);
        
    }

    @Test
    public void testNovyBodZ() {
        System.out.println("novyBodZ");
        
        assertTrue(ciara1.novyBodZ(10.3, 9));
          
        assertTrue(10.3 == ciara1.body.get(0).x);
        assertTrue(9 == ciara1.body.get(0).y);        
        assertTrue(22.3 == ciara1.body.get(1).x);
        assertTrue(5.2 == ciara1.body.get(1).y);
        assertTrue(14.7 == ciara1.body.get(2).x);
        assertTrue(34.9 == ciara1.body.get(2).y);

        assertTrue(ciara1.novyBodZ(3, -1));
        
        assertTrue(3 == ciara1.body.get(0).x);
        assertTrue(-1 == ciara1.body.get(0).y);
        assertTrue(10.3 == ciara1.body.get(1).x);
        assertTrue(9 == ciara1.body.get(1).y);        
        assertTrue(22.3 == ciara1.body.get(2).x);
        assertTrue(5.2 == ciara1.body.get(2).y);
        assertTrue(14.7 == ciara1.body.get(3).x);
        assertTrue(34.9 == ciara1.body.get(3).y);
        
    }
    
    //@mata
    
    @Test
    public void testPresun() {
        System.out.println("presun");
    
        assertTrue(22.3 == ciara1.body.get(0).x); 
        assertTrue(5.2 == ciara1.body.get(0).y); 
        assertTrue(14.7 == ciara1.body.get(1).x); 
        assertTrue(34.9 == ciara1.body.get(1).y); 
   
        ciara1.presun(0, 40.4, 50.6); 
        ciara1.presun(1, 7.4, 6.8);   
    
        assertTrue(40.4 == ciara1.body.get(0).x); 
        assertTrue(50.6 == ciara1.body.get(0).y);   
        assertTrue(7.4 == ciara1.body.get(1).x); 
        assertTrue(6.8 == ciara1.body.get(1).y); 
    
        ciara1.presun(0, 0, 0); ciara1.presun(1, 1, 1); 
    
        assertTrue(0 == ciara1.body.get(0).x); 
        assertTrue(0 == ciara1.body.get(0).y); 
        assertTrue(1 == ciara1.body.get(1).x); 
        assertTrue(1 == ciara1.body.get(1).y); 
    
        ciara1.presun(0, 0, 0); 
        ciara1.presun(1, 0, 0); 
    
        assertTrue(0 == ciara1.body.get(0).x); 
        assertTrue(0 == ciara1.body.get(0).y); 
        assertTrue(0 == ciara1.body.get(1).x); 
        assertTrue(0 == ciara1.body.get(1).y);
    }
    //@mata
    @Test
    public void testIsNear() {
        TCiara ciara2 = new TCiara(0 , 0, 2, 2, "ciara1", Color.white, 9.7, 4); 
        assertTrue(ciara2.isNear(1, 1));
        assertFalse(ciara2.isNear(1, 9));
    }

    //@janka
    @Test
    public void testPointNear() {
        System.out.println("pointNear");
       
        assertTrue(ciara1.pointNear(22, 5) == 0);
        assertTrue(ciara1.pointNear(23, 4) == 0);
        assertTrue(ciara1.pointNear(23, 6) == 0);
        assertTrue(ciara1.pointNear(14, 37) == 1);
        
        //System.out.println(ciara1.pointNear(32, 10));
        assertTrue(ciara1.pointNear(32, 10) == -1);
        
        
    }

    @Test
    public void testPosunCiaru() {
        System.out.println("posunCiaru");
       
        ciara1.posunCiaru(10.1 , 9);
        
        assertTrue(10.1 == ciara1.body.get(0).x);
        assertTrue(9 == ciara1.body.get(0).y);

        assertEquals(2.5 , ciara1.body.get(1).x, 0.00001);
        assertEquals(38.7, ciara1.body.get(1).y, 0.00001);
        
    }
    
//@mata
    @Test
    public void testVymazK() {
        ciara1.vymazK();
               
        assertTrue(22.3 == ciara1.body.get(0).x);
        assertTrue(5.2 == ciara1.body.get(0).y);
        assertTrue(14.7 == ciara1.body.get(1).x);
        assertTrue(34.9 == ciara1.body.get(1).y);
        
      
        assertTrue(ciara1.novyBodZ(10.3, 9));
        
        assertTrue(10.3 == ciara1.body.get(0).x);
        assertTrue(9 == ciara1.body.get(0).y);        
        assertTrue(22.3 == ciara1.body.get(1).x);
        assertTrue(5.2 == ciara1.body.get(1).y);
        assertTrue(14.7 == ciara1.body.get(2).x);
        assertTrue(34.9 == ciara1.body.get(2).y);
  
        ciara1.vymazK();
        
         assertTrue(10.3 == ciara1.body.get(0).x);
        assertTrue(9 == ciara1.body.get(0).y);        
        assertTrue(22.3 == ciara1.body.get(1).x);
        assertTrue(5.2 == ciara1.body.get(1).y);
    }
//@mata
    @Test
    public void testVymazZ() {
        
        ciara1.vymazZ();
               
        assertTrue(22.3 == ciara1.body.get(0).x);
        assertTrue(5.2 == ciara1.body.get(0).y);
        assertTrue(14.7 == ciara1.body.get(1).x);
        assertTrue(34.9 == ciara1.body.get(1).y);
        
      
        assertTrue(ciara1.novyBodZ(10.3, 9));
        
        assertTrue(10.3 == ciara1.body.get(0).x);
        assertTrue(9 == ciara1.body.get(0).y);        
        assertTrue(22.3 == ciara1.body.get(1).x);
        assertTrue(5.2 == ciara1.body.get(1).y);
        assertTrue(14.7 == ciara1.body.get(2).x);
        assertTrue(34.9 == ciara1.body.get(2).y);
  
        ciara1.vymazZ();
        
        assertTrue(22.3 == ciara1.body.get(0).x);
        assertTrue(5.2 == ciara1.body.get(0).y);
        assertTrue(14.7 == ciara1.body.get(1).x);
        assertTrue(34.9 == ciara1.body.get(1).y);
        
        
        
    }
   
}
