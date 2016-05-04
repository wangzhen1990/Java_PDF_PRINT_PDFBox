package com.pierreantoineguillaume;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class SimpleA4PDFPrinter {

    public void process(String... args) {
        try {
            if (args.length != 2) //// TODO: 29/04/2016 push exception
                //needs:
                // 1) String Filename (should be accessible)
                // 2) String Printername
                throw new RuntimeException("Not enough Arguments");

            PDDocument pdf = PDDocument.load(new File(args[0]));
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();

            attr_set.add(MediaSizeName.ISO_A4);
            attr_set.add(Sides.ONE_SIDED);


            PDFPageable p = new PDFPageable(pdf);
            PDFPrintable printable = new PDFPrintable(pdf, Scaling.SCALE_TO_FIT);

            job.setPageable(p);
            job.setPrintable(printable);




            PrintService ps = null;
            for  (PrintService i : PrintServiceLookup.lookupPrintServices(null,attr_set)) {
                if (i.getName().equals(args[1])) {
                    ps = i;
                }
            }

            if (ps == null) {
                try {
                    throw new RuntimeException("No Printer Found");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            else
            {

                job.setPrintService(ps);
                job.print(attr_set);

            }

        } catch (IOException | PrinterException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
    }
}

