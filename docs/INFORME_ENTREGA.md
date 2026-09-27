# Informe de entrega – Generación y clasificación de datos

## 1. Introducción

El proyecto consiste en diseñar e implementar una solución en Java 8 capaz de generar archivos de prueba con información de vendedores, productos y ventas, para luego procesarlos y generar reportes clasificados.

La propuesta aplica el enfoque modular estudiado en la Lectura Fundamental de la Unidad 2, Escenario 3: **Funciones o métodos**.

## 2. Requerimientos interpretados

La guía solicita:

- documentación del código según estándares de Java;
- buenas prácticas de programación;
- reporte de vendedores con nombre y dinero recaudado, ordenado de mayor a menor;
- reporte de productos ordenado por cantidad vendida, incluyendo nombre y precio;
- métodos de generación pseudoaleatoria;
- dos clases, y solo dos, con método `main`: `GenerateInfoFiles` y `main`;
- ausencia de entrada interactiva al usuario;
- uso de Java 8 y Eclipse;
- archivo `conslusion.txt`;
- entrega de un repositorio con el proyecto completo.

La solución también incorpora como mejora la posibilidad de procesar más de un archivo por vendedor y detectar datos incoherentes.

## 3. Diseño modular

La lectura señala que los métodos son una forma de resolver subproblemas independientemente del problema global y que este enfoque favorece la legibilidad, reutilización, mantenimiento y documentación.

### GenerateInfoFiles

Responsabilidad: producir las entradas de prueba.

Métodos centrales:

| Método | Responsabilidad |
|---|---|
| `createSalesMenFile(...)` | Generar un archivo de ventas para un vendedor |
| `createProductsFile(...)` | Generar productos pseudoaleatorios |
| `createSalesManInfoFile(...)` | Generar vendedores pseudoaleatorios |

### main

Responsabilidad: procesar las entradas y generar reportes.

| Método | Responsabilidad |
|---|---|
| `loadProducts()` | Leer productos |
| `loadSalesmen()` | Leer vendedores |
| `processSalesFiles(...)` | Explorar la carpeta de ventas |
| `processSingleSalesFile(...)` | Procesar un archivo individual |
| `generateSalesmanReport(...)` | Crear reporte de vendedores |
| `generateProductReport(...)` | Crear reporte de productos |

## 4. Parámetros y argumentos

La lectura diferencia el parámetro, que aparece en la definición del método, del argumento, que es el valor enviado al invocarlo.

Ejemplo aplicado:

```java
createProductsFile(PRODUCT_COUNT);
```

En la definición:

```java
public static void createProductsFile(int productsCount)
```

`productsCount` es el parámetro formal y `PRODUCT_COUNT` es el argumento utilizado en la invocación.

## 5. Uso de cadenas

El programa procesa información separada por punto y coma mediante `String.split(";")`.

También se utilizan operaciones de cadenas como:

- `trim()` para eliminar espacios innecesarios;
- `toLowerCase()` para comparar extensiones;
- `compareToIgnoreCase()` para desempates al ordenar productos.

Esto se relaciona con el contenido de la lectura sobre métodos de `String`.

## 6. Flujo de ejecución

```text
                 INICIO
                    |
                    v
          GenerateInfoFiles
                    |
          +---------+---------+
          |         |         |
          v         v         v
      productos  vendedores  ventas
          |         |         |
          +---------+---------+
                    |
                    v
                 main
                    |
          +---------+---------+
          |                   |
          v                   v
   cargar productos     cargar vendedores
          |                   |
          +---------+---------+
                    |
                    v
          procesar ventas/*.txt
                    |
                    v
          validar y acumular datos
                    |
          +---------+---------+
          |                   |
          v                   v
 reporte_vendedores.csv  reporte_productos.csv
          |                   |
          +---------+---------+
                    |
                    v
                   FIN
```

## 7. Pruebas propuestas

### Prueba 1 – ejecución normal

1. Ejecutar `GenerateInfoFiles`.
2. Confirmar que se creen `vendedores.txt`, `productos.txt` y los archivos de `data/ventas`.
3. Ejecutar `main`.
4. Confirmar que se generen los dos CSV.

### Prueba 2 – múltiples archivos por vendedor

La solución genera dos archivos por cada vendedor. El programa los procesa de forma acumulativa.

### Prueba 3 – producto inexistente

Modificar manualmente un archivo de ventas y colocar un ID que no exista en `productos.txt`. El programa debe mostrar un error.

### Prueba 4 – cantidad inválida

Cambiar una cantidad a `-5` o `0`. El programa debe rechazar el registro.

### Prueba 5 – formato inválido

Eliminar un campo de una línea de producto o vendedor. El programa debe informar la línea con formato incorrecto.

## 8. Resultado esperado

Al finalizar correctamente deben existir:

```text
output/reporte_vendedores.csv
output/reporte_productos.csv
```

El primer archivo debe estar ordenado por dinero recaudado en forma descendente.

El segundo debe estar ordenado por cantidad vendida en forma descendente y contener:

```text
NombreProducto;Precio;CantidadVendida
```

## 9. Conclusión

La solución cumple los requerimientos centrales de generación y clasificación de datos y aplica el principio de modularidad explicado en la lectura fundamental. Además, incorpora validaciones y procesamiento de múltiples archivos por vendedor como elementos de mejora.
