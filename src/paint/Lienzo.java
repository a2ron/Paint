package paint;

import Formas.*;
import Formas.Forma.tipoForma;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.awt.image.ShortLookupTable;

public class Lienzo extends javax.swing.JPanel {

    private final static float media[]={0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f};
    private final static float binomial[] = {0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,  0.0625f, 0.125f, 0.0625f};
    private final static float realce[] = {0.0f, -1.0f, 0.0f,-1.0f,  5.0f, -1.0f,0.0f, -1.0f, 0.0f};
    private final static float laplaciano[] = {1.0f, 1.0f,  1.0f, 1.0f, -8.0f, 1.0f,1.0f, 1.0f,  1.0f};
    private final static float repujar[] = {-2.0f, 1.0f,  0.0f, -1.0f, 1.0f, 1.0f,0.0f, 1.0f,  2.0f};
    private final static float dibujo[] = {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                                            -1.0f,-2.0f,-2.0f, -2.0f,  -1.0f,
                                            -1.0f,-2.0f,34.0f, -2.0f,  -1.0f,
                                            -1.0f,-2.0f,-2.0f, -2.0f,  -1.0f,
                                            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
    private static short ltN[],ltL[], ltG[],ltC[];

    public static enum Convolucion{tmedia,tbinomial,trealce,tlaplaciano,trepujar,tdibujo};
    public static enum Transformacion{zoom,rotar,estirar,trasladar};
    public static enum Degradado{none,horizontal,vertical,diagonal1,diagonal2};
    public static enum Funcion{negativo,gamma,contraste,logaritmo};
    private static enum SpecialPoint{none,formaIzqArriba,formaDerAbajo,formaCentro};

    //DECLARACION VARIABLES
    private BufferedImage imagenOrig,imagenMasFormas,imagen;
    private Graphics2D g2dImg;
    private Point ini,fin;
    private Boolean b_derecho,drageao;
    private int curvar;
    private Forma[] formas;
    private tipoForma tipo_forma;
    private short index,selection;
    private short tope;
    private short max;
    private Color color1,color2;
    private float trazo;
    private Boolean relleno,puntea;
    private Degradado degradado;
    private Rectangulo marco,_marco,formaResizeIzqArriba,formaResizeDerAbajo,formaMove;
    private SpecialPoint sPoint;
    private Boolean editForma;
    private String texto;

    public void Constructor(){
        ini = new Point(0,0);  fin = new Point(0,0);
        b_derecho = false;
        drageao=false;
        tipo_forma = tipoForma.trazo_libre;
        relleno=false;
        puntea = false;
        curvar = 0;
        degradado=Degradado.none;
        editForma=false;
        trazo=1.0f;

        sPoint = SpecialPoint.none;
        _marco = new Rectangulo();
        float dash[] = {4.0f,4.0f};
        _marco.set(new BasicStroke(2.0f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f));
        color1 = new Color(153,153,255);
        formaResizeIzqArriba = new Rectangulo();
        formaResizeDerAbajo = new Rectangulo();
        formaMove = new Rectangulo();
        formaResizeIzqArriba.set(color1); formaResizeDerAbajo.set(color1);
        _marco.set(color1); formaMove.set(color1);
        marco = null;

        color1=new Color(0,0,0);
        color2=new Color(255,255,255);
        texto = "";

        double f = 0;
        double K1 = 255.0/Math.log(256.0);
        double K2 = 255.0/Math.pow(255.0,0.40);
        double K3 = 255.0 / (   1.0 / (  1.0  +  Math.pow( 128.0/255.0 , 2.0)));
        ltN = new short[256];
        ltL = new short[256];
        ltG = new short[256];
        ltC = new short[256];
        for (int j=0; j<256; j++) {
            ltN[j] = (short)(256-j-1); // Negativo
            ltL[j] = (byte)(K1*Math.log(1.0+(double)j)); // Logaritmo
            ltG[j] = (byte)(K2*Math.pow((double)j,0.40));// Gamma
            if(j==0) ltC[j]=0;
            else {
                f = 1.0 / (1.0 + Math.pow(128.0 / (float) j, 2.0));  // Contraste
                ltC[j] = (short)(K3*f);
            }
        }
    }

    public Lienzo() {
        initComponents();
        Constructor();
        setImagen((short)300,(short)300);
        clearFormas();
    }

    public Lienzo(BufferedImage img) {
        initComponents();
        Constructor();
        setImagen(img);
        clearFormas();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FormListener formListener = new FormListener();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(0, 0));
        addMouseListener(formListener);
        addMouseMotionListener(formListener);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
        FormListener() {}
        public void mouseClicked(java.awt.event.MouseEvent evt) {
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
        }

        public void mousePressed(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == Lienzo.this) {
                Lienzo.this.formMousePressed(evt);
            }
        }

        public void mouseReleased(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == Lienzo.this) {
                Lienzo.this.formMouseReleased(evt);
            }
        }

        public void mouseDragged(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == Lienzo.this) {
                Lienzo.this.formMouseDragged(evt);
            }
        }

        public void mouseMoved(java.awt.event.MouseEvent evt) {
        }
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
       if(evt.getButton()!=MouseEvent.BUTTON1 || esperaConfirmacionOp())
           return;
       ini = evt.getPoint();
       if(editForma){
           selectForma(ini);
           editaForma();
           return;
       }
       b_derecho = true;
       if(curvar>0){
           curvar=-curvar;
           deshacer();
           formMouseDragged(evt);
           return;
       }
       max=tope;//actualizo limite de 'rehacer'
       
       //comprueba si se ha pinchado en alguna esquina para editar la ultima forma
       getSpecialPoint(ini);
       //si no se ha pinchado en ninguna esquina para editar...
       if(sPoint==SpecialPoint.none){
           marco=null;
           switch (tipo_forma){
               case trazo_libre://crear la nueva forma a pintar
                   formas[index]=new TrazoLibre();
                   ((TrazoLibre)formas[index]).moveTo(ini);
                   ((TrazoLibre)formas[index]).lineTo(ini);
                   formMouseDragged(evt);  break;
               case linea: formas[index]=new Linea(); break;
               case rectangulo: formas[index]=new Rectangulo(); break;
               case round_rect: formas[index]=new Round_Rectangulo();
                   break;
               case elipse: formas[index]=new Elipse(); break;
               case curva1: formas[index]= new Curva1();break;
               case curva2: formas[index]= new Curva2();break;
               case texto: formas[index] = new Texto(); break;
           }
           formas[index].set(relleno);
           setTrazo();
           setColor();
           if(formas[index].getTipo()==tipoForma.texto){
               ((Texto)formas[index]).set(this.getFont());
               ((Texto)formas[index]).set(texto);
               formMouseDragged(evt);
           }
        }
       else{
           deshacer();
           formMouseDragged(evt);
       }
    }//GEN-LAST:event_formMousePressed

    private void newForma(){
        ++index;
        ++tope;
        if(index>=formas.length){
            Forma [] aux = new Forma[(int)((formas.length)*1.5)];
            short i;
            for(i=0;i<index;++i)
                aux[i]=formas[i];
            formas = aux;
        }
        //formas[index] = new Linea(0,0,0,0);
    }

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
       if(!b_derecho)
           return;
       b_derecho=false;
       repaint();
       if(drageao){
           formas[index].pintate(g2dImg);
           if(curvar>0)
               curvar--;
           else if(formas[index].getTipo() == tipoForma.curva1)
               curvar = 1;
           else if(formas[index].getTipo() == tipoForma.curva2)
               curvar = 2;
           newForma();
           if(curvar==0 && sPoint==SpecialPoint.none)
               ++max;
           drageao=false;
       }
    }//GEN-LAST:event_formMouseReleased

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if(!b_derecho)
            return;
        drageao=true;
        if(curvar>0){
            if(formas[index].getTipo()==tipoForma.curva1)
                ((Curva1)formas[index]).setPuntoControl(evt.getPoint());
            else if(curvar==2)
                ((Curva2)formas[index]).setPuntoControl1(evt.getPoint());
            else
                 ((Curva2)formas[index]).setPuntoControl2(evt.getPoint());
        }
        else{
            if(sPoint==SpecialPoint.formaIzqArriba)
                ini = evt.getPoint();
            else if(sPoint==SpecialPoint.formaCentro){
                Point p = evt.getPoint();
                int despX=(p.x-(int)formaMove.getCenterX());
                int despY=(p.y-(int)formaMove.getCenterY());
                ini.x+= despX; ini.y+= despY;
                fin.x+=despX; fin.y+=despY;
            }
            else fin = evt.getPoint();
            if(!Forma.isPolygon(formas[index].getTipo())){
                marco=null;//no hay marco para editar
                if(formas[index].getTipo()==tipoForma.trazo_libre)
                    ((TrazoLibre)formas[index]).lineTo(fin);
                else if(formas[index].getTipo()==tipoForma.texto)
                    ((Texto)formas[index]).set(fin);
                else
                    formas[index].set(ini, fin);
            }
            else{
                int v[] = new int[4];
                Forma.getBounds(ini,fin,v);
                marco = _marco;
                formas[index].set(ini, fin);
                //actualizo marco para editar
                formaResizeIzqArriba.set(v[0]-15,v[1]-15,10, 10);
                formaResizeDerAbajo.set(v[0]+v[2]+5,v[1]+v[3]+5,10, 10);
                formaMove.set(v[0]+v[2]/2-5,v[1]+v[3]/2-5,10, 10);
                marco.set(v[0]-10,v[1]-10, v[2]+20, v[3]+20);
            }
        }
        if(degradado!=degradado.none)
            setColor();
        this.repaint();
    }//GEN-LAST:event_formMouseDragged

    @Override
    public void paint(java.awt.Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if(imagen!=null){
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(imagen,0,0,this);
            g2d.setClip(new Rectangle2D.Float(0.0f,0.0f,imagen.getWidth(),imagen.getHeight()));
            if(drageao)
                formas[index].pintate(g2d);
            if(!b_derecho && marco!=null){
                marco.pintate(g2d);
                formaResizeIzqArriba.pintate(g2d);
                formaResizeDerAbajo.pintate(g2d);
                formaMove.pintate(g2d);
            }
        }
    }

    public void setText(String texto){
        this.texto = texto;
    }

    private void setColor(){
        formas[index].setFondo(new Color(color2.getRed(),color2.getGreen(),color2.getBlue(),color2.getAlpha()));
        switch (degradado){
            case none: formas[index].set(new Color(color1.getRed(), color1.getGreen(),color1.getBlue(),color1.getAlpha())); break;
            case horizontal:
                formas[index].set(new GradientPaint(
                        (float) formas[index].getX(),
                        (float) formas[index].getCenterY(),
                        color1,
                        (float) (formas[index].getX()+formas[index].getWidth()),
                        (float) formas[index].getCenterY(),
                        color2));
                break;
            case vertical:
                formas[index].set(new GradientPaint(
                        (float) formas[index].getCenterX(),
                        (float) formas[index].getY(),
                        color1,
                        (float) formas[index].getCenterX(),
                        (float) (formas[index].getY()+formas[index].getHeight()),
                        color2));
                break;
            case diagonal1:
                 formas[index].set(new GradientPaint(
                        (float) formas[index].getX(),
                        (float) formas[index].getY(),
                        color1,
                        (float) (formas[index].getX()+formas[index].getWidth()),
                        (float) (formas[index].getY()+formas[index].getHeight()),
                        color2));
                break;
            case diagonal2:
                 formas[index].set(new GradientPaint(
                        (float) formas[index].getX(),
                        (float) (formas[index].getY()+formas[index].getHeight()),
                        color1,
                        (float) (formas[index].getX()+formas[index].getWidth()),
                        (float) formas[index].getY(),
                        color2));
                break;
        }
    }

    private void setTrazo(){
        float dash[] = {5.0f,5.0f};
        int cap=BasicStroke.CAP_ROUND;
        if(formas[index].getTipo()==tipoForma.rectangulo || formas[index].getTipo()==tipoForma.linea)
            cap=BasicStroke.CAP_BUTT;
        if(puntea)
            formas[index].set(new BasicStroke(trazo,cap,cap,10.0f,dash,0.0f));
        else
            formas[index].set(new BasicStroke(trazo,cap,cap));
    }

    private void editaForma(){
        short aux = max;
        max = tope;
        editForma = false;
        if(!deshacerTodo()){
            editForma = true;
            max = aux;
            return;
        }
        editForma = true;
        index = selection;
        if(formas[index].getTipo()==tipoForma.texto)
            ((Texto)formas[index]).set(this.getFont());
        formas[index].set(relleno);
        setColor();
        setTrazo();
        rehacerTodo();
        max = aux;
    }

    public void setColor1(Color color){
        color1=color;
        if(editForma) editaForma();
    }

    public void setDegradado(Degradado deg){
        marco=null;
        repaint();
        degradado=deg;
        if(editForma) editaForma();
    }

    public void setColor2(Color color){
        color2=color;
        if(editForma) editaForma();
    }

    public void setForma(tipoForma f) {
        editForma=false;
        tipo_forma = f;
    }

    public void setRellenar(Boolean rellenar) {
        relleno=rellenar;
        if(editForma) editaForma();
    }

    public void setGrosor(float f){
        trazo = f;
        if(editForma) editaForma();
    }

    public void setPunteo(Boolean b){
        puntea = b;
        if(editForma) editaForma();
    }

    public void set(Font fuente){
        super.setFont(fuente);
        if(editForma) editaForma();
    }

    public void setImagen(short ancho,short alto){
        _setImagen(null,ancho,alto);
    }

    public void setImagen(BufferedImage img){
        short a = 0;
        _setImagen(img,a,a);
    }

    private void _setImagen(BufferedImage img,short ancho,short alto){
        if(img==null){
            imagenOrig = new BufferedImage(ancho,alto,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagenOrig.createGraphics();
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0,0,imagenOrig.getWidth(),imagenOrig.getHeight());
        }
        else imagenOrig = img;

        imagenMasFormas = new BufferedImage(imagenOrig.getWidth(),imagenOrig.getHeight(),BufferedImage.TYPE_INT_RGB);
        g2dImg = imagenMasFormas.createGraphics();
        g2dImg.drawImage(imagenOrig, 0, 0, null);
        imagen = imagenMasFormas;
        g2dImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension d = new Dimension(imagen.getWidth(), imagen.getHeight());
        this.setPreferredSize(d);
        this.setSize(d);

        this.repaint();
    }

    public BufferedImage getImagen(){
        return imagen;
    }

    public Boolean esperaConfirmacionOp(){
        return imagen!=imagenMasFormas;
    }

    public Boolean deshacer(){
        if(tope<=0 || esperaConfirmacionOp())
            return false;
        if(curvar<0)
            curvar=-curvar;
        else curvar=0;
        tope--;
        index--;
        marco=null;
        setImagen(imagenOrig);
        short i;
        for(i=0;i<tope;++i)
            formas[i].pintate(g2dImg);
        if(editForma) selectForma(null);
        return true;
    }

    private Boolean deshacerTodo(){
        if(deshacer()){
            while(deshacer()){}
            return true;
        }
        else return false;
    }

    public Boolean rehacer(){
        if(tope==max || esperaConfirmacionOp())
            return false;
        newForma();
        formas[index-1].pintate(g2dImg);
        repaint();
        return true;
    }

    private void rehacerTodo(){
       index=0;
       while(rehacer()){}
    }

    public void setEditar(Boolean b){
        editForma=b;
        marco=null;
        repaint();
        selectForma(null);
    }

    private void selectForma(Point p){
        int i = (index - 1);
        selection = (short) i;
        if(index>0 && p!=null){
            for(;i>=0;--i){
                if(formas[i].contains(p)){
                    selection = (short) i;
                    break;
                }
            }
            
        }
    }
    public void setBrillo(float aumento){
        marco=null;
        RescaleOp rop = new RescaleOp(1.0F,aumento,null);
        imagen = rop.filter(imagenMasFormas, null);
        repaint();
    }

    public void setConvolucion(Convolucion c){
        marco=null;
        Kernel k = null;
        switch (c){
            case tmedia: k = new Kernel(3,3,media); break;
            case tbinomial: k = new Kernel(3,3,binomial); break;
            case trealce: k = new Kernel(3,3,realce); break;
            case tlaplaciano: k = new Kernel(3,3,laplaciano); break;
            case trepujar: k = new Kernel(3,3,repujar);break;
            case tdibujo: k = new Kernel(5,5,dibujo);break;
        }
        ConvolveOp cop = new ConvolveOp(k);
        imagen = cop.filter(imagenMasFormas,null);
        repaint();
    }

    public void setAffineTransform(Transformacion t,float a, float b){
        marco=null;
        AffineTransform at=null;
        switch(t){
            case zoom:  at = AffineTransform.getScaleInstance(a,b); break;
            case rotar: at = AffineTransform.getRotateInstance(Math.toRadians(a)
                                ,imagenOrig.getWidth()/2,imagenOrig.getHeight()/2);break;
            case estirar: at = AffineTransform.getShearInstance(a,b); break;
            case trasladar: at = AffineTransform.getTranslateInstance(a,b); break;
        }
        AffineTransformOp atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
        imagen = atop.filter( imagenMasFormas, null);
        redimensionar(imagen.getWidth(),imagen.getHeight());
        repaint();
    }

    public void setLookUpOp(Funcion func){
        ShortLookupTable slt = null;
        switch ( func ) {
            case negativo: slt = new ShortLookupTable(0, ltN); break;
            case logaritmo: slt = new ShortLookupTable(0, ltL); break;
            case gamma: slt = new ShortLookupTable(0, ltG); break;
            case contraste: slt = new ShortLookupTable(0, ltC); break;
        }
        LookupOp lop = new LookupOp(slt, null);
        imagen = lop.filter( imagen , null);
        repaint();
    }

    public void toBYN(){
        ICC_Profile ip = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
        ColorSpace cs = new ICC_ColorSpace(ip);
        ColorConvertOp ccop = new ColorConvertOp(cs,null);
        imagen = ccop.filter(imagen,null);
        repaint();
    }

    private void clearFormas(){
        index=0;tope=0;max=0;
        formas=new Forma[10];
    }

    public void aplicar_operacion(){
       if(imagenMasFormas!=imagen){
            setImagen(imagen);
            clearFormas();
       }
    }

    public void deshacer_operacion(){
        if(imagenMasFormas==imagen)
            return;
        imagen = imagenMasFormas;
        Dimension d = new Dimension(imagen.getWidth(), imagen.getHeight());
        this.setPreferredSize(d);
        this.setSize(d);
        repaint();
    }

    public void redimensionar(int width,int height){
        marco=null;
        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2dImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.white);
        g2d.fillRect(0,0,img.getWidth(),img.getHeight());
        g2d.setPaint(color2);
        g2d.fillRect(0,0,img.getWidth(),img.getHeight());
        g2d.drawImage(imagen, 0, 0,this);
        imagen = img;
        Dimension d = new Dimension(imagen.getWidth(), imagen.getHeight());
        this.setPreferredSize(d);
        this.setSize(d);
        repaint();
    }


    private void getSpecialPoint(Point p){
       if(marco!=null){
           if(formaResizeIzqArriba.contains(ini)){
               sPoint=SpecialPoint.formaIzqArriba;
               fin.x = (int)((Rectangulo)formaResizeDerAbajo).getX()-5;
               fin.y = (int)((Rectangulo)formaResizeDerAbajo).getY()-5;
           }
           else if(formaResizeDerAbajo.contains(ini)){
               sPoint=SpecialPoint.formaDerAbajo;
               ini.x = (int)((Rectangulo)formaResizeIzqArriba).getX()+15;
               ini.y = (int)((Rectangulo)formaResizeIzqArriba).getY()+15;
           }
           else if(formaMove.contains(ini)){
               sPoint=SpecialPoint.formaCentro;
               ini.x = (int)((Rectangulo)formaResizeIzqArriba).getX()+15;
               ini.y = (int)((Rectangulo)formaResizeIzqArriba).getY()+15;
               fin.x = (int)((Rectangulo)formaResizeDerAbajo).getX()-5;
               fin.y = (int)((Rectangulo)formaResizeDerAbajo).getY()-5;
           }
           else
               sPoint=SpecialPoint.none;
       }
       else sPoint = SpecialPoint.none;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


}