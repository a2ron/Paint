package Formas;

import java.awt.geom.Ellipse2D;

public class Elipse extends Forma{


    public Elipse(){
        super();
        la_forma = new Ellipse2D.Float(0,0,0,0);
        tipo_forma = tipoForma.elipse;
    }

    public Elipse(float x,float y, float w, float h){
        super();
        la_forma = new Ellipse2D.Float(x,y,w,h);
        tipo_forma = tipoForma.elipse;
    }
}