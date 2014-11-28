package Formas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public abstract class Forma{

    public static enum tipoForma{none,linea,rectangulo,elipse,trazo_libre,round_rect,curva1,curva2,texto};

    public static Boolean isPolygon(tipoForma t){
        return !(t==tipoForma.curva1 || t==tipoForma.curva2 || t==tipoForma.linea 
                || t==tipoForma.trazo_libre || t==tipoForma.texto);
    }

    protected BasicStroke trazo;
    protected Paint color;
    protected Color fondo;
    protected Boolean relleno;
    protected tipoForma tipo_forma;
    protected Shape la_forma;


    public Forma(){
        color = new Color(0,0,0);
        fondo = Color.red;
        relleno = false;
        trazo = new BasicStroke(1.0f);
        tipo_forma=tipoForma.none;
    }

    public void set(Paint color) {
        this.color = color;
    }

    public void set(Boolean relleno) {
        if(!isPolygon(tipo_forma))
            this.relleno = false;
        else
            this.relleno = relleno;
    }

    public void set(BasicStroke trazo) {
        this.trazo = trazo;
    }

    public void set(Point ini, Point fin){
        int v[] = new int[4];
        getBounds(ini,fin,v);
        Point cp = new Point(v[0]+v[2]/2,v[1]+v[3]/2);
        switch(tipo_forma){
            case linea: ((Line2D.Float)la_forma).setLine(ini,fin);
            case trazo_libre: return;
            case rectangulo: ((Rectangle2D)la_forma).setRect(v[0],v[1],v[2],v[3]); break;
            case round_rect: ((RoundRectangle2D)la_forma).setFrame(v[0],v[1],v[2],v[3]); break;
            case elipse: ((Ellipse2D)la_forma).setFrame(v[0],v[1],v[2],v[3]); break;
            case curva1: ((QuadCurve2D)la_forma).setCurve(ini, cp, fin); break;
            case curva2: ((CubicCurve2D)la_forma).setCurve(ini, cp,cp, fin); break;
        }
    }

    public void set(int x, int y, int width, int height){
        Point ini=new Point();
        Point fin = new Point();
        ini.x=x;
        ini.y=y;
        fin.x=x+width;
        fin.y=y+height;
        set(ini,fin);
    }

    public void setFondo(Color c){
        fondo=c;
    }

    public tipoForma getTipo(){
        return tipo_forma;
    }

    public void pintate(Graphics2D brocha){
        if(tipo_forma==tipoForma.none)
            return;
        brocha.setPaint(color);
        brocha.setStroke(trazo);
        if(relleno){
            if(brocha.getPaint().toString().contains("java.awt.GradientPaint") || 
                    fondo.equals(color))
                brocha.fill(la_forma);
            else{
                brocha.setPaint(fondo);
                brocha.fill(la_forma);
                brocha.setPaint(color);
                brocha.draw(la_forma);
            }
        }
        else brocha.draw(la_forma);
    }

    public double getX(){
        return la_forma.getBounds().getX();
    }

    public double getY(){
        return la_forma.getBounds().getY();
    }

    public double getCenterX(){
        return la_forma.getBounds().getCenterX();
    }

    public  double getCenterY(){
        return la_forma.getBounds().getCenterY();
    }

    public double getWidth(){
        return la_forma.getBounds().getWidth();
    }

    public double getHeight(){
        return la_forma.getBounds().getHeight();
    }

    public Paint getColor(){
        return color;
    }
    
    public Boolean contains(Point p){
        return la_forma.contains(p);
    }


/**
 * @brief dados 2 puntos de inicio y fin, nos devuelve en un vector de enteros
 *  la informacion de coordenada de inicio, ancho y alto para pintar
 * @param ini, punto inicio
 * @param fin, punto fin
 * @param v     v[0] -> coordenada x de inicio
 *              v[1] -> coordenada y de inicio
 *              v[2] -> ancho
 *              v[3] -> alto
 */
    public static final void getBounds(Point ini,Point fin, int v[]){
        if(ini.x<fin.x && ini.y<fin.y){
            v[0] = ini.x;
            v[1] = ini.y;
            v[2] = fin.x-ini.x;
            v[3] = fin.y-ini.y;
        }
        else if(ini.x>fin.x && ini.y<fin.y){
            v[0] = fin.x;
            v[1] = ini.y;
            v[2] = ini.x-fin.x;
            v[3] = fin.y-ini.y;
        }
        else if(ini.x<fin.x && ini.y>fin.y){
            v[0] = ini.x;
            v[1] = fin.y;
            v[2] = fin.x-ini.x;
            v[3] = ini.y-fin.y;
        }
        else{
            v[0] = fin.x;
            v[1] = fin.y;
            v[2] = ini.x-fin.x;
            v[3] = ini.y-fin.y;
        }
    }
}
