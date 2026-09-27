# Proyecto: Generación y clasificación de datos

## Autores

- Juan David Gustin Andino
- John Morera Barrera
- Ebert Perez Ardila
- Adriana del Pilar Salgado Perez

## 1. Objetivo

Diseñar e implementar en Java 8 un programa modular que genere archivos de entrada pseudoaleatorios y, posteriormente, procese esos archivos para producir dos reportes CSV:

1. `reporte_vendedores.csv`: vendedores ordenados de mayor a menor según el dinero recaudado.
2. `reporte_productos.csv`: productos ordenados de mayor a menor según la cantidad vendida.

La solución se construye aplicando el enfoque modular de funciones o métodos estudiado en la Lectura Fundamental de la Unidad 2 / Escenario 3.

## 2. Estructura del proyecto

```text
Proyecto_Generacion_Clasificacion_Datos/
├── src/
│   ├── GenerateInfoFiles.java
│   ├── Product.java
│   ├── Salesman.java
│   └── main.java
├── data/
│   ├── vendedores.txt
│   ├── productos.txt
│   └── ventas/
├── output/
├── docs/
├── conslusion.txt
└── README.md
```

## 3. Ejecución en Eclipse Java 8

1. Crear o importar el proyecto en Eclipse.
2. Configurar un JRE compatible con Java 8.
3. Verificar que `src` sea el directorio de código fuente.
4. Ejecutar primero `GenerateInfoFiles.java` como **Java Application**.
5. Ejecutar después `main.java` como **Java Application**.
6. Revisar los archivos generados en `output/`.

Ninguna de las dos clases solicita datos al usuario, tal como exige la guía.

## 4. Clases principales

### GenerateInfoFiles

Tiene el único `main` encargado de generar las entradas.

Métodos requeridos por la guía:

- `createSalesMenFile(int randomSalesCount, String name, long id)`
- `createProductsFile(int productsCount)`
- `createSalesManInfoFile(int salesmanCount)`

Además, se generan dos archivos de ventas por vendedor para demostrar el elemento extra de procesamiento de más de un archivo por vendedor.

### main

Tiene el segundo y único `main`. Carga vendedores y productos, explora todos los archivos de `data/ventas`, acumula las ventas y produce los dos reportes.

### Product

Representa el ID, nombre, precio y cantidad vendida acumulada.

### Salesman

Representa tipo de documento, número de documento, nombre, apellido y total recaudado.

## 5. Archivos de entrada

### vendedores.txt

Formato:

```text
TipoDocumento;NúmeroDocumento;Nombres;Apellidos
```

### productos.txt

Formato:

```text
IDProducto;NombreProducto;PrecioPorUnidad
```

### archivos de ventas

Primera línea:

```text
TipoDocumentoVendedor;NúmeroDocumentoVendedor
```

Líneas siguientes:

```text
IDProducto;CantidadVendida;
```

## 6. Validaciones implementadas

La solución agrega el elemento extra de detección de información incoherente:

- archivos inexistentes o vacíos;
- formato incorrecto;
- IDs de producto duplicados;
- documentos de vendedor duplicados;
- productos inexistentes;
- cantidades no positivas;
- precios no positivos;
- vendedores inexistentes;
- inconsistencias en el tipo de documento.

Cuando se detecta un problema se muestra un mensaje de error y se detiene el procesamiento.

## 7. Relación con la Lectura Fundamental

La Lectura Fundamental explica que los métodos permiten resolver subproblemas de manera independiente y favorecen la legibilidad, reutilización, mantenimiento y documentación del código. La solución separa las responsabilidades en métodos como `loadProducts`, `loadSalesmen`, `processSalesFiles`, `processSingleSalesFile`, `generateSalesmanReport` y `generateProductReport`.

También se emplean parámetros y argumentos de manera explícita, y los métodos retornan información cuando la tarea lo requiere. Se utilizan cadenas y métodos de `String`, como `trim`, `split`, `toLowerCase` y `compareToIgnoreCase`, de acuerdo con los contenidos de la lectura.

## 8. Entregables cubiertos

- Código Java documentado con Javadoc.
- Generación pseudoaleatoria de archivos.
- Procesamiento de múltiples archivos por vendedor.
- Reporte de vendedores ordenado por recaudo.
- Reporte de productos ordenado por cantidad vendida.
- Validación de datos incoherentes.
- `conslusion.txt`.
- Evidencias de ejecución y explicación del diseño en `docs/`.

## 9. Observación sobre el nombre conslusion.txt

Se conserva exactamente `conslusion.txt`, incluida la ortografía indicada en la guía.
