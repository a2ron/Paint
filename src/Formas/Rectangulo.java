package Formas;

import java.awt.geom.Rectangle2D;

public class Rectangulo extends Forma{

    public Rectangulo(){
        super();
        la_forma = new Rectangle2D.Float(0,0,0,0);
        tipo_forma = tipoForma.rectangulo;
    }

    public Rectangulo(float x, float y, float w, float h){
        super();
        la_forma = new Rectangle2D.Float(x, y, w, h);
        tipo_forma = tipoForma.rectangulo;
    }
}
