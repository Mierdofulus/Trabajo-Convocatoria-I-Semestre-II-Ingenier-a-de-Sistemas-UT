/*
 * CIPA 9 - Comidas Rápidas
 * Universidad del Tolima  - Ingeniería de Sistemas 2026B
 * Integrantes Edgar Andrés Wilches Hernández 0854001282026 - Nicolle Camila Romero Espinosa 085401342026
 * Clase con los precios y los cálculos de la venta
 */

public class Calculos {

    // precios de los productos
    int precioHamburguesa = 35000;
    int precioPapas = 15000;
    int precioGaseosa = 12000;
    int precioPizza = 70000;
    int precioNugghets = 20000;
    int precioEnsalada = 30000;
    int precioYogourth = 15000;
    int precioAgua = 8000;

    // calcula el subtotal con las cantidades y los precios
    public double calcularSubtotal(int hamburguesa, int papas, int gaseosa, int pizza,
            int nugghets, int ensalada, int yogourth, int agua) {

        double subtotal = hamburguesa * precioHamburguesa
                + papas * precioPapas
                + gaseosa * precioGaseosa
                + pizza * precioPizza
                + nugghets * precioNugghets
                + ensalada * precioEnsalada
                + yogourth * precioYogourth
                + agua * precioAgua;
        return subtotal;
    }

    // calcula el IVA del 19%
    public double calcularIva(double subtotal) {
        return subtotal * 0.19;
    }

    // total a pagar = subtotal + iva
    public double calcularTotal(double subtotal, double iva) {
        return subtotal + iva;
    }

    // cambio = pago - total
    public double calcularCambio(double pago, double total) {
        return pago - total;
    }
}
