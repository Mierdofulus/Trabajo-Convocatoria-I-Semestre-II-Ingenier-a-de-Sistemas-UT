/*
 *CIPA 9 - Comidas Rápidas
 *Universidad del Tolima - Ingeniería de Sistemas 2026B
 *Integrantes: Edgar Andrés Wilches Hernández 0854001282026
 *             Nicolle Camila Romero Espinosa 085401342026
 *Las cuentas se hacen en Calculos.java.
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Color;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class frmcomidas extends JFrame {

    //El menú: productos, precios y combos
    static final String[] PRODUCTOS = {"Hamburguesa","Papas","Gaseosa","Pizza","Nugghets","Ensalada","Yogourth","Agua"};
    static final int[]    PRECIOS   = {35000,15000,12000,70000,20000,30000,15000,8000};
    static final String[] COMBOS    = {"Hamburguesa, Papas y Gaseosa","Pizza, Nugghets y Gaseosa",
                                       "Ensalada, Yogourth y Agua","Otras Opciones"};
    //Cuántas unidades de cada producto trae cada combo ("Otras Opciones" deja todo en cero)
    static final int[][]  UNIDADES = {{1,1,1,0,0,0,0,0},{0,0,1,1,1,0,0,0},{0,0,0,0,0,1,1,1},{0,0,0,0,0,0,0,0}};

    static final DecimalFormat PESOS = new DecimalFormat("$#,##0.##", DecimalFormatSymbols.getInstance(Locale.GERMANY));
    static final Color ROJO = new Color(255, 200, 200);   // casilla mal escrita

    Calculos calculos = new Calculos();
    JTextField[] cantidades = new JTextField[PRODUCTOS.length];
    ButtonGroup  combos = new ButtonGroup();
    JTextField   pago, subtotal, iva, total, cambio;

    //Cada vez que se escribe algo, la cuenta se actualiza sola
    DocumentListener alEscribir = new DocumentListener() {
        public void insertUpdate(DocumentEvent e)  { actualizarCuenta(); }
        public void removeUpdate(DocumentEvent e)  { actualizarCuenta(); }
        public void changedUpdate(DocumentEvent e) { actualizarCuenta(); }
    };

    public frmcomidas() {
        setTitle("Comidas Rápidas - UT Ing Sistemas 2026B CIPA 9");
        setSize(640, 520);
        setLayout(null);                //cada cosa se ubica con coordenadas
        setResizable(false);            //no se puede agrandar
        setLocationRelativeTo(null);    //aparece en el centro
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { salir(); }   //la X también pregunta
        });

        JLabel titulo = texto("Comidas Rápidas", 40, 15, 300);
        titulo.setFont(new java.awt.Font("Tahoma", 0, 28));
        titulo.setSize(300, 40);

        //Cada producto: su nombre con precio y la casilla de cantidad
        for (int i = 0; i < PRODUCTOS.length; i++) {
            int y = 20 + i * 42;
            texto(PRODUCTOS[i] + ": " + dinero(PRECIOS[i]), 420, y, 200);
            cantidades[i] = new JTextField("0");
            cantidades[i].setBounds(320, y, 50, 28);
            cantidades[i].setHorizontalAlignment(JTextField.CENTER);
            seleccionarAlEntrar(cantidades[i]);
            cantidades[i].getDocument().addDocumentListener(alEscribir);
            add(cantidades[i]);
        }

        //Combos: se eligen con un clic del mouse
        for (int i = 0; i < COMBOS.length; i++) {
            final int numero = i;
            JRadioButton combo = new JRadioButton(COMBOS[i]);
            combo.setBounds(20, 70 + i * 30, 260, 25);
            combo.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { elegirCombo(numero); }
            });
            combos.add(combo);
            add(combo);
        }

        //Cuenta y pago
        texto("Subtotal:", 20, 380, 80);        subtotal = resultado(100, 380, 110);
        texto("IVA 19%:", 20, 410, 80);         iva      = resultado(100, 410, 110);
        texto("Total a pagar:", 230, 380, 100); total    = resultado(340, 380, 150);
        texto("Pago:", 280, 410, 50);
        texto("Cambio:", 270, 450, 60);         cambio   = resultado(340, 450, 150);

        pago = new JTextField("0");
        pago.setBounds(340, 410, 150, 28);
        pago.getDocument().addDocumentListener(alEscribir);   //el cambio se ve mientras se escribe
        add(pago);

        boton("Pago", 395, e -> pagar());
        boton("Salir", 435, e -> salir());
    }

    //Ayudas para dibujar
    private JLabel texto(String contenido, int x, int y, int ancho) {
        JLabel etiqueta = new JLabel(contenido);
        etiqueta.setBounds(x, y, ancho, 25);
        add(etiqueta);
        return etiqueta;
    }

    private JTextField resultado(int x, int y, int ancho) {   //cuadro que solo muestra, no se escribe
        JTextField campo = new JTextField(dinero(0));
        campo.setBounds(x, y, ancho, 25);
        campo.setEditable(false);
        campo.setHorizontalAlignment(JTextField.CENTER);
        add(campo);
        return campo;
    }

    private void boton(String nombre, int y, ActionListener alPulsar) {
        JButton b = new JButton(nombre);
        b.setBounds(520, y, 90, 30);
        b.addActionListener(alPulsar);
        add(b);
    }

    //Al hacer clic o llegar con Tab, se selecciona el número para escribir encima
    private void seleccionarAlEntrar(final JTextField campo) {
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { SwingUtilities.invokeLater(() -> campo.selectAll()); }
        });
        campo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { campo.selectAll(); }
        });
    }

    private static String dinero(double valor) { return PESOS.format(valor); }

    private void aviso(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }

    //Lee el número de una casilla (vacía = 0). Si está mal escrito la pinta de rojo y devuelve -1.
    private double leerNumero(JTextField campo, boolean soloEnteros) {
        String t = campo.getText().trim();
        boolean bien = t.isEmpty() || t.matches(soloEnteros ? "\\d{1,9}" : "\\d+(\\.\\d+)?");
        campo.setBackground(bien ? Color.WHITE : ROJO);
        return !bien ? -1 : t.isEmpty() ? 0 : Double.parseDouble(t);
    }

    // Lo que pasa cuando la persona hace algo

    //Llena las cantidades del combo, deja el pago en cero y actualiza la cuenta
    private void elegirCombo(int numero) {
        for (int i = 0; i < cantidades.length; i++)
            cantidades[i].setText(String.valueOf(UNIDADES[numero][i]));
        pago.setText("0");
        actualizarCuenta();
    }

    //Muestra subtotal, IVA y total, y devuelve el total (o -1 si hay una cantidad mal escrita)
    private double mostrarCuenta() {
        int[] u = new int[PRODUCTOS.length];
        boolean bien = true;
        for (int i = 0; i < u.length; i++) {
            u[i] = (int) leerNumero(cantidades[i], true);
            bien &= u[i] >= 0;
        }
        if (!bien) {
            subtotal.setText(""); iva.setText(""); total.setText("");
            return -1;
        }
        double sub = calculos.calcularSubtotal(u[0], u[1], u[2], u[3], u[4], u[5], u[6], u[7]);
        double imp = calculos.calcularIva(sub);
        double tot = calculos.calcularTotal(sub, imp);
        subtotal.setText(dinero(sub));
        iva.setText(dinero(imp));
        total.setText(dinero(tot));
        return tot;
    }

    //Actualiza cuenta y cambio sin mensajes; lo mal escrito se ve en rojo
    private void actualizarCuenta() {
        double aPagar = mostrarCuenta(), dado = leerNumero(pago, false);
        cambio.setText(aPagar < 0 ? "" : dinero(dado >= aPagar ? calculos.calcularCambio(dado, aPagar) : 0));
    }

    //Botón "Pago": si todo está bien y alcanza, avisa del éxito y empieza una compra nueva
    private void pagar() {
        double aPagar = mostrarCuenta(), dado = leerNumero(pago, false);

        if (aPagar < 0 || dado < 0 || pago.getText().trim().isEmpty())
            aviso("Escriba solo números positivos en las cantidades y el pago");
        else if (aPagar == 0)
            aviso("Elija al menos un producto o un combo antes de pagar");
        else if (dado < aPagar)
            aviso("El pago es insuficiente");
        else {
            double devuelta = calculos.calcularCambio(dado, aPagar);
            cambio.setText(dinero(devuelta));
            JOptionPane.showMessageDialog(this, "¡Pago realizado satisfactoriamente!\nCambio: " + dinero(devuelta),
                "Pago exitoso", JOptionPane.INFORMATION_MESSAGE);   // espera a que pulsen OK
            elegirCombo(COMBOS.length - 1);   //"Otras Opciones": todo en cero
            combos.clearSelection();          //ningún combo queda marcado
        }
    }

    //Pregunta "¿Está seguro?" y cierra solo si responden "Sí"
    private void salir() {
        int r = JOptionPane.showOptionDialog(this, "¿Está seguro que desea salir?", "Salir",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "No");
        if (r == 0) System.exit(0);   //0 = "Sí"; -1 = cerró el cuadro con la X
    }

    public static void main(String[] args) {
        new frmcomidas().setVisible(true);
    }
}
