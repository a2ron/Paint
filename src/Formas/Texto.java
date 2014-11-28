package Formas;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Texto extends Forma{

    private String texto;
    private Point2D p;
    private Font fuente;

    public Texto(){
        super();
        texto = "";
        p = new Point2D.Float(0,0);
        la_forma = new Rectangle2D.Double(0,0,0,0);
        tipo_forma = tipoForma.texto;
    }

    public void set(String texto){
        this.texto = texto;
    }

    public void set(Point2D punto){
        p.setLocation(punto);
    }

    public void set(Font fuente){
        this.fuente = new Font(fuente.getFontName(),fuente.getStyle(),fuente.getSize());
    }

    @Override
    public void pintate(Graphics2D brocha){
        super.pintate(brocha);
        brocha.setFont(fuente);
        brocha.drawString(texto,(float)p.getX(),(float)p.getY());
    }
}