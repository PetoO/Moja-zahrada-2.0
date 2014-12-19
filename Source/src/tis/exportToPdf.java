
package tis;

import javax.swing.JFrame;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author Rudolf cvacho
 */
public class exportToPdf {

    public exportToPdf() {
        
    }
 /**
  * Tlač plániku - výstup na tlačiareň
  * @param frame plánik
  */
    public void PrintFrameToPDF(JFrame frame) throws PrinterException {
        JFrame yourComponent = frame;
        PrinterJob pjob = PrinterJob.getPrinterJob();
        PageFormat preformat = pjob.defaultPage();
        preformat.setOrientation(PageFormat.LANDSCAPE);
        PageFormat postformat = pjob.pageDialog(preformat);
//If user does not hit cancel then print.
        if (preformat != postformat) {
            //Set print component
            pjob.setPrintable(new Printer(yourComponent), postformat);
            if (pjob.printDialog()) {
                pjob.print();
            }
        }
    }
}