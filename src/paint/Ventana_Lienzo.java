package paint;

import java.awt.image.BufferedImage;
public class Ventana_Lienzo extends javax.swing.JInternalFrame {

    public Ventana_Lienzo() {
        initComponents();
        this.setTitle("Nuevo");
    }

    public Ventana_Lienzo(short ancho,short alto) {
        this();
        lienzo.setImagen(ancho, alto);
    }

    public Ventana_Lienzo(BufferedImage img,String name) {
        this();
        lienzo.setImagen(img);
        this.setTitle(name);
    }

    public Lienzo getLienzo(){
        return lienzo;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll_panel = new javax.swing.JScrollPane();
        lienzo = new paint.Lienzo();

        setClosable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(200, 200));
        setPreferredSize(new java.awt.Dimension(400, 400));
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        javax.swing.GroupLayout lienzoLayout = new javax.swing.GroupLayout(lienzo);
        lienzo.setLayout(lienzoLayout);
        lienzoLayout.setHorizontalGroup(
            lienzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        lienzoLayout.setVerticalGroup(
            lienzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );

        scroll_panel.setViewportView(lienzo);

        getContentPane().add(scroll_panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private paint.Lienzo lienzo;
    private javax.swing.JScrollPane scroll_panel;
    // End of variables declaration//GEN-END:variables

}
