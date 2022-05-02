package U2;

import ij.IJ;
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

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 Opens an image window and adds a panel below the image
 */
public class U2_S583615 implements PlugIn {

    ImagePlus imp; // ImagePlus object
    private int[] origPixels;
    private int width;
    private int height;


    public static void main(String args[]) {
        //new ImageJ();
        //IJ.open("/users/barthel/applications/ImageJ/_images/orchid.jpg");
       // IJ.open("C:\\Benutzer\\mailephuong\\Downloads\\orchid.jpg");
        IJ.open("/Users/mailephuong/Downloads/orchid.jpg");

        U2_S583615 pw = new U2_S583615();
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


    class CustomWindow extends ImageWindow implements ChangeListener {

        private JSlider jSliderBrightness;
        private JSlider Kontrast;
        private JSlider Saettigung;
        private JSlider Hue;
        private double brightness;
        private double kontrast;
        private double saettigung;
        private double hue;

        CustomWindow(ImagePlus imp, ImageCanvas ic) {
            super(imp, ic);
            addPanel();
        }

        void addPanel() {
            //JPanel panel = new JPanel();
            Panel panel = new Panel();

            panel.setLayout(new GridLayout(4, 1));
            jSliderBrightness = makeTitledSilder("Helligkeit", 0, 256, 100);
            Kontrast = makeTitledSilder("Kontrast", 0, 10, 1);
            Saettigung = makeTitledSilder("Sättigung", 0, 5, 1);
            Hue = makeTitledSilder("Hue", 0, 360, 1);
            panel.add(jSliderBrightness);
            panel.add(Kontrast);
            panel.add(Saettigung);
            panel.add(Hue);

            add(panel);

            pack();
        }

        private JSlider makeTitledSilder(String string, int minVal, int maxVal, int val) {

            JSlider slider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, val );
            Dimension preferredSize = new Dimension(width, 50);
            slider.setPreferredSize(preferredSize);
            TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(),
                    string, TitledBorder.LEFT, TitledBorder.ABOVE_BOTTOM,
                    new Font("Sans", Font.PLAIN, 11));
            slider.setBorder(tb);
            slider.setMajorTickSpacing((maxVal - minVal)/10 );
            slider.setPaintTicks(true);
            slider.addChangeListener(this);

            return slider;
        }

        private void setSliderTitle(JSlider slider, String str) {
            TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(),
                    str, TitledBorder.LEFT, TitledBorder.ABOVE_BOTTOM,
                    new Font("Sans", Font.PLAIN, 11));
            slider.setBorder(tb);
        }

        public void stateChanged( ChangeEvent e ){
            JSlider slider = (JSlider)e.getSource();

            if (slider == jSliderBrightness) {
                brightness = slider.getValue()-128;
                String str = "Helligkeit " + brightness;
                setSliderTitle(jSliderBrightness, str);
            }

            if (slider == Kontrast) {
                kontrast = slider.getValue();
                String str = "Sättigung " + kontrast;
                setSliderTitle(Kontrast, str);
            }
            if (slider == Saettigung) {
                saettigung = slider.getValue();
                String str = "Sättigung " + saettigung;
                setSliderTitle(Saettigung, str);
            }
            if (slider == Hue) {
                hue = slider.getValue();
                String str = "Hue " + hue;
                setSliderTitle(Hue, str);
            }

            changePixelValues(imp.getProcessor());

            imp.updateAndDraw();
        }


        private void changePixelValues(ImageProcessor ip) {

            // Array fuer den Zugriff auf die Pixelwerte
            int[] pixels = (int[])ip.getPixels();

            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {
                    int pos = y*width + x;
                    int argb = origPixels[pos];  // Lesen der Originalwerte

                    int r = (argb >> 16) & 0xff;
                    int b =  argb        & 0xff;
                    int g = (argb >>  8) & 0xff;


                    // anstelle dieser drei Zeilen später hier die Farbtransformation durchführen,
                    double y1 = 0.299*r+0.587*g+0.144*b;
                    double u = (b-y1)*0.493;
                    double v = (r-y1)*0.877;

                    r =(int)(y1 + v/0.877);
                    b= (int)( y1 + u/0.493);
                    g= (int)(1/0.587 * y1 - 0.299/0.587*r - 0.114/0.587 * b);

                    // die Y Cb Cr -Werte verändern und dann wieder zurücktransformieren
                    int rn = (int) (r + brightness);
                    if (rn>255){
                        rn = 255;
                    }else if ( rn<0){
                        rn=0;
                    }
                    int gn = (int) (g + brightness);
                    if (gn>255){
                        gn = 255;
                    }else if ( gn<0){
                        gn=0;
                    }
                    int bn = (int) (b + brightness);
                    if (bn>255){
                        bn = 255;
                    }else if ( bn<0){
                        bn=0;
                    }

                    // Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

                    pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
                }
            }
        }

    } // CustomWindow inner class
}
