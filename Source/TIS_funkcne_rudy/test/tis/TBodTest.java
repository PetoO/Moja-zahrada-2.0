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
public class TBodTest {
    
    public TBodTest() {
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
     * Test of presun method, of class TBod.
     */
    
    TBod bodik1 = new TBod(11.2, 23.4, "bodik1", Color.black, 3.4, 2, 7.5);
    TBod bodik2 = null;
        
    @Test
    public void testPresun() {
        System.out.println("presun");  
       
        assertTrue(null != bodik1);
        assertTrue(null == bodik2);
        
        assertTrue(11.2 == bodik1.x);
        assertTrue(23.4 == bodik1.y);
        assertTrue("bodik1" == bodik1.meno);
        assertTrue(Color.black == bodik1.farba);
        assertTrue(3.4 == bodik1.hrubka);
        assertTrue(2 == bodik1.styl);
        assertTrue(7.5 == bodik1.velkost);
        
        assertTrue(bodik1.presun(8, 10));
        
        assertTrue(8 == bodik1.x);
        assertTrue(10 == bodik1.y);
        assertTrue("bodik1" == bodik1.meno);
        assertTrue(Color.black == bodik1.farba);
        assertTrue(3.4 == bodik1.hrubka);
        assertTrue(2 == bodik1.styl);
        assertTrue(7.5 == bodik1.velkost);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isNear method, of class TBod.
     */
    @Test
    public void testIsNear() {
        System.out.println("isNear");
        
        assertTrue(bodik1.isNear(11.2, 23.4));
        assertTrue(bodik1.isNear(12.0, 24.0));
        assertTrue(bodik1.isNear(13.0, 25.0));
        assertTrue(bodik1.isNear(20.0, 40.0));
        assertTrue(bodik1.isNear(21.0, 41.0));
        assertFalse(bodik1.isNear(22.0, 42.0));
        assertFalse(bodik1.isNear(25.0, 45.0));
        assertFalse(bodik1.isNear(30.0, 50.0));
        assertFalse(bodik1.isNear(55, 78));
        
    }
    
}
