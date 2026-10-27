/*
 * CIPA 9 - Comidas Rápidas
 * Universidad del Tolima - Ingeniería de Sistemas 2026B
 * Integrantes Edgar Andrés Wilches Hernández 0854001282026 - Nicolle Camila Romero Espinosa 085401342026
 * Formulario (interfaz y eventos). Los cálculos están en Calculos.java
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class frmcomidas extends JFrame {

    Calculos calc = new Calculos();

    // --- datos declarativos ---
    static final String[] PRODUCTOS = {"Hamburguesa","Papas","Gaseosa","Pizza","Nugghets","Ensalada","Yogourth","Agua"};
    static final int[]    PRECIOS   = {35000,15000,12000,70000,20000,30000,15000,8000};
    static final String[] COMBOS    = {"Hamburguesa, Papas y Gaseosa","Pizza, Nugghets y Gaseosa",
                                       "Ensalada, Yogourth y Agua","Otras Opciones"};
    static final int[][]  CANT_COMBO = {{1,1,1,0,0,0,0,0},{0,0,1,1,1,0,0,0},
                                        {0,0,0,0,0,1,1,1},{0,0,0,0,0,0,0,0}};

    JTextField[]   cantidades = new JTextField[PRODUCTOS.length];
    JTextField     pagoField;
    JLabel         lblsubtotal, lbliva, lbltotal, lblcambio;
    JRadioButton[] combos = new JRadioButton[COMBOS.length];
    ButtonGroup    bg     = new ButtonGroup();

    public frmcomidas() { initComponents(); }

    private void initComponents() {
        setTitle("Comidas Rápidas - UT Ing Sistemas 2026B CIPA 9");
        setSize(640, 520);
        setLayout(null);
        setResizable(false);                       //ventana no maximizable
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JLabel titulo = crearLabel("Comidas Rápidas", 40, 15, 300);
        titulo.setFont(new java.awt.Font("Tahoma", 0, 28));
        titulo.setSize(300, 40);

        //DocumentListener compartido: recalcula al escribir en cualquier campo
        DocumentListener auto = new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { recalcular(); }
            public void removeUpdate(DocumentEvent e)  { recalcular(); }
            public void changedUpdate(DocumentEvent e) { recalcular(); }
        };

        //productos: etiqueta con precio + campo de cantidad
        for (int i = 0; i < PRODUCTOS.length; i++) {
            int y = 20 + i * 42;
            crearLabel(PRODUCTOS[i] + ": $" + PRECIOS[i], 420, y, 200);
            cantidades[i] = new JTextField("0");
            cantidades[i].setBounds(320, y, 50, 28);
            configurarCampoNumerico(cantidades[i]);
            cantidades[i].getDocument().addDocumentListener(auto);
            add(cantidades[i]);
        }

        //combos: evento Mouse Click con MouseListener
        for (int i = 0; i < COMBOS.length; i++) {
            final int idx = i;
            combos[i] = new JRadioButton(COMBOS[i]);
            combos[i].setBounds(20, 70 + i * 30, 260, 25);
            combos[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { aplicarCombo(idx); }
            });
            bg.add(combos[i]);
            add(combos[i]);
        }

        //resultados
        crearLabel("Subtotal:", 20, 380, 80);       lblsubtotal = crearLabel("0.0", 100, 380, 120);
        crearLabel("IVA 19%:", 20, 410, 80);        lbliva      = crearLabel("0.0", 100, 410, 120);
        crearLabel("Total a pagar:", 230, 380, 100); lbltotal   = crearLabel("0.0", 340, 380, 150);
        crearLabel("Pago:", 280, 410, 50);
        crearLabel("Cambio:", 270, 450, 60);        lblcambio   = crearLabel("0.0", 340, 450, 150);

        pagoField = new JTextField("0");
        pagoField.setBounds(340, 410, 150, 28);
        pagoField.getDocument().addDocumentListener(auto);   //cambio en vivo al digitar pago
        add(pagoField);

        crearBoton("Pago", 395, e -> calcularCambio());
        crearBoton("Salir", 435, e -> salir());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { salir(); }
        });
    }

    //helpers para crear y agregar componentes repetitivos
    private JLabel crearLabel(String text, int x, int y, int w) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, w, 25);
        add(l);
        return l;
    }

    private void crearBoton(String text, int y, ActionListener accion) {
        JButton b = new JButton(text);
        b.setBounds(520, y, 90, 30);
        b.addActionListener(accion);
        add(b);
    }

    //Centra el texto y selecciona todo su contenido al hacer click o al recibir el foco.
    private void configurarCampoNumerico(final JTextField campo) {
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { SwingUtilities.invokeLater(() -> campo.selectAll()); }
        });
        campo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { campo.selectAll(); }
        });
    }

    //FUNCIONES DEFINIDAS

    //Rellena las cantidades de cada producto y limpia pago/cambio.
    public void ponerCantidades(int... nuevas) {
        for (int i = 0; i < cantidades.length; i++)
            cantidades[i].setText(String.valueOf(nuevas[i]));
        pagoField.setText("0");
        lblcambio.setText("0.0");
    }

    //Aplica el combo seleccionado y actualiza los totales.
    private void aplicarCombo(int idx) {
        ponerCantidades(CANT_COMBO[idx]);
        recalcular();
    }

    //Reinicia todo el formulario a su estado inicial.
    private void reiniciar() {
        aplicarCombo(COMBOS.length - 1);   //cantidades, pago, subtotal, IVA, total y cambio en cero
        bg.clearSelection();               //desmarca el combo seleccionado
    }

    //Calcula y muestra subtotal, IVA y total. Devuelve el total.
    public double mostrarTotales() {
        int[] c = new int[PRODUCTOS.length];
        for (int i = 0; i < c.length; i++) {
            String s = cantidades[i].getText().trim();
            c[i] = s.isEmpty() ? 0 : Integer.parseInt(s);   //vacío = 0, no rompe al borrar
        }
        double subtotal = calc.calcularSubtotal(c[0],c[1],c[2],c[3],c[4],c[5],c[6],c[7]);
        double iva      = calc.calcularIva(subtotal);
        double total    = calc.calcularTotal(subtotal, iva);

        lblsubtotal.setText(String.valueOf(subtotal));
        lbliva.setText(String.valueOf(iva));
        lbltotal.setText(String.valueOf(total));
        return total;
    }

    //Recalcula silenciosamente (autocompletar cambio).
    private void recalcular() {
        try {
            double total = mostrarTotales();
            String p = pagoField.getText().trim();
            if (!p.isEmpty()) {
                double pago = Double.parseDouble(p);
                lblcambio.setText(pago >= total ? String.valueOf(calc.calcularCambio(pago, total)) : "0.0");
            }
        } catch (NumberFormatException ex) { }
    }

    //Botón "Pago": si es insuficiente avisa; si es suficiente muestra éxito y reinicia al pulsar OK.
    private void calcularCambio() {
        try {
            double total = mostrarTotales();
            double pago  = Double.parseDouble(pagoField.getText().trim());
            if (pago < total) {
                JOptionPane.showMessageDialog(this, "El pago es insuficiente");
                lblcambio.setText("0.0");
            } else {
                double cambio = calc.calcularCambio(pago, total);
                lblcambio.setText(String.valueOf(cambio));
                JOptionPane.showMessageDialog(this, "¡Pago realizado satisfactoriamente!\nCambio: $" + cambio,
                    "Pago exitoso", JOptionPane.INFORMATION_MESSAGE);
                reiniciar();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Escriba solo números en las cantidades y el pago");
        }
    }

    //Confirmación de salida con JOptionPane
    public void salir() {
        String[] opciones = {"Sí", "No"};
        int r = JOptionPane.showOptionDialog(this, "¿Está seguro que desea salir?", "Salir",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[1]);
        if (r == 0) System.exit(0);   //0 = "Sí"; -1 = cerró el diálogo con la X
    }

    public static void main(String[] args) {
        new frmcomidas().setVisible(true);
    }
}