/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tis;

import java.awt.Color;
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
public class TPaletaPrvokTest {
    
    public TPaletaPrvokTest() {
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

    
     TPaletaPrvok prvok = new TPaletaPrvok(2, 1, "prvok1", "bod1", Color.CYAN, 2.2, 3, 2.3); 
    
    @Test
    public void testPaletaPrvok() {
        System.out.println("PaletaPrvok");
       
        assertTrue(prvok.img == "bod1");
        assertTrue(prvok.name == "prvok1");
        assertTrue(prvok.farba == Color.CYAN);
        assertTrue(prvok.num == 2);
        assertTrue(prvok.typ == 1);
        assertTrue(prvok.hrubka == 2.2);
        assertTrue(prvok.styl == 3);
        assertTrue(prvok.velkost == 2.3);
        
    }
    
    
    @Test
    public void testSetNum() {
        System.out.println("setNum");
       
       prvok.setNum(5);
        assertEquals(5, prvok.num);
        
    }

    /**
     * Test of setTypPrvku method, of class TPaletaPrvok.
     */
    @Test
    public void testSetTypPrvku() {
        System.out.println("setTypPrvku");
       
        TBod bodicek = new TBod(2.1, 3.2, "kukuk", Color.white, 3.4, 2, 3);
        prvok.setTypPrvku(bodicek);
        
        assertTrue(prvok.typPrvku == bodicek);
        //keine ahnung wie soll ich das machen
        //prvok.setTypPrvku(null);
        
    }
    
}
