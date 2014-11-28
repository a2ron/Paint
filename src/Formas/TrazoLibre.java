package Formas;

import java.awt.Point;
import java.awt.geom.GeneralPath;

public class TrazoLibre extends Forma{

    public TrazoLibre(){
        super();
        la_forma = new GeneralPath.Float(GeneralPath.WIND_NON_ZERO);
        tipo_forma = tipoForma.trazo_libre;
    }

    public void lineTo(Point p){
        ((GeneralPath.Float)la_forma).lineTo(p.x,p.y);
    }

    public void moveTo(Point p){
        ((GeneralPath.Float)la_forma).moveTo(p.x,p.y);
    }
}
