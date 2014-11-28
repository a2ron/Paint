package Formas;

import java.awt.geom.Line2D;

public class Linea extends Forma{

    public Linea(){
        super();
        la_forma = new Line2D.Float(0,0,0,0);
        tipo_forma = tipoForma.linea;
    }

    public Linea(float x1, float x2, float y1, float y2){
        super();
        la_forma = new Line2D.Float(x1, y1, x2, y2);
        tipo_forma = tipoForma.linea;
    }
}
