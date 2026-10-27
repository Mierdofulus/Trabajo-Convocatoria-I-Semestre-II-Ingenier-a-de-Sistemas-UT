## Convocatoria I ingenieria de Sistemas semestre II - Universidad del Tolima
Cipa 9
Edgar Andrés Wilches Hernández - 0854001282026
Nicolle Camila Romero Espinosa - 085401342026

Profesor 
Wilson Alexander Moran Guerrero

# Comidas Rápidas

Aplicación de escritorio desarrollada en Java para registrar pedidos de comidas rápidas y calcular el valor a pagar. Permite seleccionar un combo o ingresar las cantidades de cada producto, calcula automáticamente el subtotal y el IVA del 19 %, y muestra el cambio cuando se registra el pago.

## Productos y precios

| Producto | Precio |
| Hamburguesa | $35.000 |
| Papas | $15.000 |
| Gaseosa | $12.000 |
| Pizza | $70.000 |
| Nuggets | $20.000 |
| Ensalada | $30.000 |
| Yogur | $15.000 |
| Agua | $8.000 |

Combos:

- Hamburguesa, papas y gaseosa
- Pizza, nuggets y gaseosa
- Ensalada, yogur y agua
- Otras opciones (selección manual)

## Requisitos

- Java JDK instalado
- No requiere dependencias externas

## Compilar y ejecutar

Desde la carpeta raíz del proyecto, ejecuta:

```powershell
javac -d bin src\Calculos.java src\frmcomidas.java
java -cp bin frmcomidas
```

## Estructura del proyecto

- `src\frmcomidas.java`: interfaz gráfica, selección de productos y gestión del pago.
- `src\Calculos.java`: precios y cálculos de subtotal, IVA, total y cambio.
- `bin\`: archivos compilados.
- `lib\`: carpeta disponible para dependencias adicionales.
