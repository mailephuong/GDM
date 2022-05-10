package U3;


    import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

    /**
     Opens an image window and adds a panel below the image
     */
    public class U3_S583615 implements PlugIn {

        ImagePlus imp; // ImagePlus object
        private int[] origPixels;
        private int width;
        private int height;

        String[] items = {"Original", "Rot-Kanal", "Negativ", "Graustufenbild", "Binaer", "Binaer-3","Binaer-7",  "Diffusion", "Sepia","8Farben"};


        public static void main(String args[]) {

            IJ.open("/Users/mailephuong/Downloads/Bear.jpg");
            //IJ.open("Z:/Pictures/Beispielbilder/orchid.jpg");

            U3_S583615 pw = new U3_S583615();
            pw.imp = IJ.getImage();
            pw.run("");
        }

        public void run(String arg) {
            if (imp==null)
                imp = WindowManager.getCurrentImage();
            if (imp==null) {
                return;
            }
            CustomCanvas cc = new CustomCanvas(imp);

            storePixelValues(imp.getProcessor());

            new CustomWindow(imp, cc);
        }


        private void storePixelValues(ImageProcessor ip) {
            width = ip.getWidth();
            height = ip.getHeight();

            origPixels = ((int []) ip.getPixels()).clone();
        }


        class CustomCanvas extends ImageCanvas {

            CustomCanvas(ImagePlus imp) {
                super(imp);
            }

        } // CustomCanvas inner class


        class CustomWindow extends ImageWindow implements ItemListener {

            private String method;

            CustomWindow(ImagePlus imp, ImageCanvas ic) {
                super(imp, ic);
                addPanel();
            }

            void addPanel() {
                //JPanel panel = new JPanel();
                Panel panel = new Panel();

                JComboBox cb = new JComboBox(items);
                panel.add(cb);
                cb.addItemListener(this);

                add(panel);
                pack();
            }

            public void itemStateChanged(ItemEvent evt) {

                // Get the affected item
                Object item = evt.getItem();

                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Selected: " + item.toString());
                    method = item.toString();
                    changePixelValues(imp.getProcessor());
                    imp.updateAndDraw();
                }

            }


            private void changePixelValues(ImageProcessor ip) {

                // Array zum Zurückschreiben der Pixelwerte
                int[] pixels = (int[])ip.getPixels();

                if (method.equals("Original")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;

                            pixels[pos] = origPixels[pos];
                        }
                    }
                }

                if (method.equals("Rot-Kanal")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            //int g = (argb >>  8) & 0xff;
                            //int b =  argb        & 0xff;

                            int rn = r;
                            int gn = 0;
                            int bn = 0;


                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }
                if (method.equals("Negativ")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;

                            int rn = 255-r;
                            int gn = 255-g;
                            int bn = 255-b;

                            if (rn>255){
                                rn = 255;
                            }else if ( rn<0){
                                rn=0;
                            }
                            if (gn>255){
                                gn = 255;
                            }else if ( gn<0){
                                gn=0;
                            }
                            if (bn>255){
                                bn = 255;
                            }else if ( bn<0){
                                bn=0;
                            }

                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }
                if (method.equals("Graustufenbild")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;

                            int rn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int gn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int bn = (int)((0.3*r)+(0.59*g)+(0.11*b));

                            // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden
                            if (rn>255){
                                rn = 255;
                            }else if ( rn<0){
                                rn=0;
                            }
                            if (gn>255){
                                gn = 255;
                            }else if ( gn<0){
                                gn=0;
                            }
                            if (bn>255){
                                bn = 255;
                            }else if ( bn<0){
                                bn=0;
                            }
                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }
                if (method.equals("Binaer")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;


                            int rn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int gn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int bn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            if (rn<128){
                                rn=0;
                            }else {rn=255;}

                            if (gn<128){
                                gn=0;
                            }else {gn=255;}

                            if (bn<128){
                                bn=0;
                            }else {bn=255;}

                            // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden
                            if (rn>255){
                                rn = 255;
                            }else if ( rn<0){
                                rn=0;
                            }
                            if (gn>255){
                                gn = 255;
                            }else if ( gn<0){
                                gn=0;
                            }
                            if (bn>255){
                                bn = 255;
                            }else if ( bn<0){
                                bn=0;
                            }
                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }
                if (method.equals("Binaer-3")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;


                            int rn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int gn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int bn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            if (rn<85){
                                rn=0;
                            }else if (rn>170){
                                rn=255;
                            }else {rn=128;}

                            if (gn<85){
                                gn=0;
                            }else if (gn>170){
                                gn=255;
                            }else {gn=128;}

                            if (bn<85){
                                bn=0;
                            }else if (bn>170){
                                bn=255;
                            }else {bn=128;}

                            // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }
                if (method.equals("Binaer-7")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;


                            int rn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int gn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int bn = (int)((0.3*r)+(0.59*g)+(0.11*b));

                            if (rn<255/7){
                                rn=0;
                            }
                            else if (rn<255/7*2){
                                rn=255/6;
                            }
                            else if (rn<255/7*3){
                                rn=255/6*2;
                            }
                            else if (rn<255/7*4){
                                rn=255/6*3;
                            }
                            else if (rn<255/7*5){
                                rn=255/6*4;
                            }
                            else if (rn<255/7*6){
                                rn=255/6*5;
                            }
                            else if (rn<255/7*7){
                                rn=255;
                            }

                            if (gn<255/7){
                                gn=0;
                            }
                            else if (gn<255/7*2){
                                gn=255/6;
                            }
                            else if (gn<255/7*3){
                                gn=255/6*2;
                            }
                            else if (gn<255/7*4){
                                gn=255/6*3;
                            }
                            else if (gn<255/7*5){
                                gn=255/6*4;
                            }
                            else if (gn<255/7*6){
                                gn=255/6*5;
                            }
                            else if (gn<255/7*7){
                                gn=255;
                            }

                            if (bn<255/7){
                                bn=0;
                            }
                            else if (bn<255/7*2){
                                bn=255/6;
                            }
                            else if (bn<255/7*3){
                                bn=255/6*2;
                            }
                            else if (bn<255/7*4){
                                bn=255/6*3;
                            }
                            else if (bn<255/7*5){
                                bn=255/6*4;
                            }
                            else if (bn<255/7*6){
                                bn=255/6*5;
                            }
                            else if (bn<255/7*7){
                                bn=255;
                            }

                            // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }


                if (method.equals("Diffusion")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;

                            int rn = (r+b+g)/255;
                            int gn = (r+b+g)/255;
                            int bn = (int)((r+b+g)/255);

                            // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden
                            if (rn>255){
                                rn = 255;
                            }else if ( rn<0){
                                rn=0;
                            }
                            if (gn>255){
                                gn = 255;
                            }else if ( gn<0){
                                gn=0;
                            }
                            if (bn>255){
                                bn = 255;
                            }else if ( bn<0){
                                bn=0;
                            }
                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }
                if (method.equals("Sepia")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;

                            int rn=(int)((0.393*r)+(0.769*g)+(0.189*b));
                            int gn=(int)((0.349*r)+(0.686*g)+(0.168*b));
                            int bn=(int)((0.272*r)+(0.534*g)+(0.131*b));

                            // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden
                            if (rn>255){
                                rn = 255;
                            }else if ( rn<0){
                                rn=0;
                            }
                            if (gn>255){
                                gn = 255;
                            }else if ( gn<0){
                                gn=0;
                            }
                            if (bn>255){
                                bn = 255;
                            }else if ( bn<0){
                                bn=0;
                            }

                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }
                if (method.equals("8Farben")) {

                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            int pos = y*width + x;
                            int argb = origPixels[pos];  // Lesen der Originalwerte

                            int r = (argb >> 16) & 0xff;
                            int g = (argb >>  8) & 0xff;
                            int b =  argb        & 0xff;

                            int rn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int gn = (int)((0.3*r)+(0.59*g)+(0.11*b));
                            int bn = (int)((0.3*r)+(0.59*g)+(0.11*b));

                            // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden
                            if (rn>255){
                                rn = 255;
                            }else if ( rn<0){
                                rn=0;
                            }
                            if (gn>255){
                                gn = 255;
                            }else if ( gn<0){
                                gn=0;
                            }
                            if (bn>255){
                                bn = 255;
                            }else if ( bn<0){
                                bn=0;
                            }
                            pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                        }
                    }
                }

            }


        } // CustomWindow inner class
    }

