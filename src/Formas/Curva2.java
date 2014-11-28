package Formas;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;


public class Curva2 extends Forma{


    public Curva2(){
        super();
        la_forma = new CubicCurve2D.Float(0,0,0,0,0,0,0,0);
        tipo_forma = tipoForma.curva2;
    }

    public void setPuntoControl1(Point2D p){
        Point2D p1 = ((CubicCurve2D)la_forma).getP1();
        Point2D p2 = ((CubicCurve2D)la_forma).getP2();
        Point2D pc2 = ((CubicCurve2D)la_forma).getCtrlP2();
        ((CubicCurve2D)la_forma).setCurve(p1,p,pc2,p2);
    }

    public void setPuntoControl2(Point2D p){
        Point2D p1 = ((CubicCurve2D)la_forma).getP1();
        Point2D p2 = ((CubicCurve2D)la_forma).getP2();
        Point2D pc1 = ((CubicCurve2D)la_forma).getCtrlP1();
        ((CubicCurve2D)la_forma).setCurve(p1,pc1,p,p2);
    }
}