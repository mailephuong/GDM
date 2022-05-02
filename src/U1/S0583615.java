package U1;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
//erste Uebung (elementare Bilderzeugung)

public class S0583615 implements PlugIn {

    final static String[] choices = {
            "Schwarzes Bild",
            "Gelbes Bild",
            "Niederlande Fahne",
            "Tuerkis/Gelb Verlauf",
            "Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf",
            "Bhutan Fahne",
            "Groenland Fahne"
    };

    private String choice;

    public static void main(String args[]) {
        ImageJ ij = new ImageJ(); // neue ImageJ Instanz starten und anzeigen
        ij.exitWhenQuitting(true);

        S0583615 imageGeneration = new S0583615();
        imageGeneration.run("");
    }

    public void run(String arg) {

        int width  = 566;  // Breite
        int height = 400;  // Hoehe

        // RGB-Bild erzeugen
        ImagePlus imagePlus = NewImage.createRGBImage("GLDM_U1", width, height, 1, NewImage.FILL_BLACK);
        ImageProcessor ip = imagePlus.getProcessor();

        // Arrays fuer den Zugriff auf die Pixelwerte
        int[] pixels = (int[])ip.getPixels();

        dialog();

        ////////////////////////////////////////////////////////////////
        // Hier bitte Ihre Aenderungen / Erweiterungen

        if ( choice.equals("Schwarzes Bild") ) {
            generateBlackImage(width, height, pixels);
        }
        else if ( choice.equals("Gelbes Bild") ) {
            generateYellowImage(width, height, pixels);
        }
        else if ( choice.equals("Niederlande Fahne") ) {
            generateNiederlandeImage(width, height, pixels);
        }
        else if ( choice.equals("Tuerkis/Gelb Verlauf") ) {
            generateTuerkisGelbImage(width, height, pixels);
        }
        else if ( choice.equals("Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf") ) {
            generateSchwarzRotImage(width, height, pixels);
        }
        else if ( choice.equals("Bhutan Fahne") ) {
            generateBhutanImage(width, height, pixels);
        }
        else if ( choice.equals("Groenland Fahne") ) {
            generateGroenlandImage(width, height, pixels);
        }

        ////////////////////////////////////////////////////////////////////

        // neues Bild anzeigen
        imagePlus.show();
        imagePlus.updateAndDraw();
    }

    private void generateBlackImage(int width, int height, int[] pixels) {
        // Schleife ueber die y-Werte
        for (int y=0; y<height; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = 0;
                int g = 0;
                int b = 0;

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }
    private void generateYellowImage(int width, int height, int[] pixels) {
        // Schleife ueber die y-Werte
        for (int y=0; y<height; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = 255;
                int g = 255;
                int b = 0;

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }}
    private void generateNiederlandeImage(int width, int height, int[] pixels) {
        // Schleife ueber die y-Werte
        for (int y=0; y<height/3; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = 136;
                int g = 0;
                int b = 0;

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
        for (int y=height/3; y<(height/3)*2; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = 255;
                int g = 255;
                int b = 255;

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
        for (int y=(height/3)*2; y<height; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = 0;
                int g = 0;
                int b = 136;

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }
    private void generateTuerkisGelbImage(int width, int height, int[] pixels) {
        // Schleife ueber die y-Werte
        for (int y=0; y<height; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = (255*x+64*(width-1-x))/(width-1);
                int g = (255*x+224*(width-1-x))/(width-1);
                int b = (0*x+208*(width-1-x))/(width-1);

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }}
    private void generateSchwarzRotImage(int width, int height, int[] pixels) {
        // Schleife ueber die y-Werte
        for (int y=0; y<height; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = ((127*x+0*(width-1-x))/(width-1)*y + (255*x+0*(width-1-x))/(width-1)*(height-1-y))/(height-1);
                int g = ((0*x+0*(width-1-x))/(width-1)*y + (0*x+0*(width-1-x))/(width-1)*(height-1-y))/(height-1);
                int b = ((255*x+255*(width-1-x))/(width-1)*y + (0*x+0*(width-1-x))/(width-1)*(height-1-y))/(height-1);

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }}
    private void generateBhutanImage(int width, int height, int[] pixels) {
        // Schleife ueber die y-Werte
        for (int y=0; y<height; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen
                int r = 0;
                int g = 0;
                int b = 0;
                if(width-1-x>y*width/height) {
                    r = 255;
                    g = 255;
                    b = 0;
                } else {
                    r = 255;
                    g = 127;
                    b = 0;
                }

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }}
    private void generateGroenlandImage(int width, int height, int[] pixels) {
        // Schleife ueber die y-Werte
        for (int y=0; y<height; y++) {
            // Schleife ueber die x-Werte
            for (int x=0; x<width; x++) {
                int pos = y*width + x; // Arrayposition bestimmen

                int r = 0;
                int g = 0;
                int b = 0;
                if(y<height/2) {
                    if(Math.pow(y-height/2,2)+Math.pow(x-width/3,2)<Math.pow(height/4,2)){
                        r = 255;
                        g = 0;
                        b = 0;
                    } else {
                        r = 255;
                        g = 255;
                        b = 255;
                    }
                } else {
                    if(Math.pow(y-height/2,2)+Math.pow(x-width/3,2)<Math.pow(height/4,2)){
                        r = 255;
                        g = 255;
                        b = 255;
                    } else {
                        r = 255;
                        g = 0;
                        b = 0;
                    }
                }

                // Werte zurueckschreiben
                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }}



    private void dialog() {
        // Dialog fuer Auswahl der Bilderzeugung
        GenericDialog gd = new GenericDialog("Bildart");

        gd.addChoice("Bildtyp", choices, choices[0]);


        gd.showDialog();	// generiere Eingabefenster

        choice = gd.getNextChoice(); // Auswahl uebernehmen

        if (gd.wasCanceled())
            System.exit(0);
    }
}
