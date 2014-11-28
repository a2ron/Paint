package Formas;

import java.awt.geom.RoundRectangle2D;

public class Round_Rectangulo extends Forma{

    public Round_Rectangulo(){
        super();
        la_forma = new RoundRectangle2D.Float(0,0,0,0,10,10);
        tipo_forma = tipoForma.round_rect;
    }

    public Round_Rectangulo(float x, float y, float w, float h){
        super();
        la_forma = new RoundRectangle2D.Float(x, y, w, h,10,10);
        tipo_forma = tipoForma.round_rect;
    }

}
