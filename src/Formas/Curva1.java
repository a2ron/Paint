package Formas;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;


public class Curva1 extends Forma{


    public Curva1(){
        super();
        la_forma = new QuadCurve2D.Float(0, 0, 0, 0, 0, 0);
        tipo_forma = tipoForma.curva1;
    }

    public void setPuntoControl(Point2D p){
        Point2D p1 = ((QuadCurve2D)la_forma).getP1();
        Point2D p2 = ((QuadCurve2D)la_forma).getP2();
        ((QuadCurve2D)la_forma).setCurve(p1,p,p2);
    }
}