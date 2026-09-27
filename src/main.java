import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Procesa los archivos de vendedores y productos y genera los reportes CSV.
 *
 * <p>La clase utiliza métodos independientes para lectura, validacion,
 * acumulacion y ordenamiento, siguiendo el enfoque modular de la lectura
 * fundamental sobre funciones o métodos.</p>
 *
 * @author Juan David Gustin Andino
 * @author John Morera Barrera
 * @author Ebert Perez Ardila
 * @author Adriana del Pilar Salgado Perez
 * @version 1.0
 */
public class main {

    private static final String DATA_DIRECTORY = "data";
    private static final String SALES_DIRECTORY = DATA_DIRECTORY + File.separator + "ventas";
    private static final String PRODUCTS_FILE = DATA_DIRECTORY + File.separator + "productos.txt";
    private static final String SALESMEN_FILE = DATA_DIRECTORY + File.separator + "vendedores.txt";
    private static final String SALESMEN_REPORT = "output/reporte_vendedores.csv";
    private static final String PRODUCTS_REPORT = "output/reporte_productos.csv";

    /**
     * Punto de entrada del programa. No solicita datos al usuario.
     *
     * @param args argumentos de ejecucion; no se utilizan
     */
    public static void main(String[] args) {
        try {
            Map<Long, Product> products = loadProducts();
            Map<Long, Salesman> salesmen = loadSalesmen();

            processSalesFiles(products, salesmen);
            generateSalesmanReport(salesmen);
            generateProductReport(products);

            System.out.println("Procesamiento finalizado exitosamente.");
            System.out.println("Reporte generado: " + SALESMEN_REPORT);
            System.out.println("Reporte generado: " + PRODUCTS_REPORT);
        } catch (IOException | RuntimeException e) {
            System.err.println("Error durante el procesamiento: " + e.getMessage());
        }
    }

    /**
     * Carga todos los productos desde productos.txt.
     *
     * @return mapa indexado por id de producto
     */
    public static Map<Long, Product> loadProducts() throws IOException {
        Map<Long, Product> products = new HashMap<Long, Product>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length != 3) {
                    throw new IllegalArgumentException(
                        "Formato invalido en productos.txt, linea " + lineNumber);
                }

                long id = parsePositiveLong(fields[0], "ID de producto", lineNumber);
                String name = fields[1].trim();
                double price = parsePositiveDouble(fields[2], "precio", lineNumber);

                if (products.containsKey(id)) {
                    throw new IllegalArgumentException(
                        "ID de producto duplicado: " + id);
                }

                products.put(id, new Product(id, name, price));
            }
        }

        return products;
    }

    /**
     * Carga los vendedores desde vendedores.txt.
     *
     * @return mapa indexado por numero de documento
     */
    public static Map<Long, Salesman> loadSalesmen() throws IOException {
        Map<Long, Salesman> salesmen = new HashMap<Long, Salesman>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SALESMEN_FILE))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length != 4) {
                    throw new IllegalArgumentException(
                        "Formato invalido en vendedores.txt, linea " + lineNumber);
                }

                String documentType = fields[0].trim();
                long documentNumber = parsePositiveLong(
                    fields[1], "documento del vendedor", lineNumber);
                String firstName = fields[2].trim();
                String lastName = fields[3].trim();

                if (salesmen.containsKey(documentNumber)) {
                    throw new IllegalArgumentException(
                        "Documento de vendedor duplicado: " + documentNumber);
                }

                salesmen.put(documentNumber,
                    new Salesman(documentType, documentNumber, firstName, lastName));
            }
        }

        return salesmen;
    }

    /**
     * Explora la carpeta de ventas y procesa todos los archivos .txt.
     * Permite mas de un archivo por vendedor.
     */
    public static void processSalesFiles(Map<Long, Product> products,
                                         Map<Long, Salesman> salesmen) throws IOException {
        File directory = new File(SALES_DIRECTORY);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("No existe la carpeta de ventas.");
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new IOException("No hay archivos de ventas para procesar.");
        }

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase(Locale.ENGLISH).endsWith(".txt")) {
                processSingleSalesFile(file, products, salesmen);
            }
        }
    }

    /**
     * Procesa un archivo individual de ventas.
     */
    public static void processSingleSalesFile(File file,
                                              Map<Long, Product> products,
                                              Map<Long, Salesman> salesmen) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();

            if (header == null) {
                throw new IllegalArgumentException("Archivo vacio: " + file.getName());
            }

            String[] sellerFields = header.split(";");
            if (sellerFields.length != 2) {
                throw new IllegalArgumentException(
                    "Cabecera invalida en " + file.getName());
            }

            String documentType = sellerFields[0].trim();
            long sellerId = parsePositiveLong(
                sellerFields[1], "documento del vendedor", 1);

            Salesman salesman = salesmen.get(sellerId);
            if (salesman == null) {
                throw new IllegalArgumentException(
                    "El vendedor " + sellerId + " del archivo " + file.getName()
                    + " no existe en vendedores.txt.");
            }

            if (!documentType.equals(salesman.getDocumentType())) {
                throw new IllegalArgumentException(
                    "Tipo de documento inconsistente para vendedor " + sellerId);
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 2 || fields.length > 3) {
                    throw new IllegalArgumentException(
                        "Formato invalido en " + file.getName()
                        + ", linea " + lineNumber);
                }

                long productId = parsePositiveLong(
                    fields[0], "ID de producto", lineNumber);
                int quantity = parsePositiveInt(
                    fields[1], "cantidad", lineNumber);

                Product product = products.get(productId);
                if (product == null) {
                    throw new IllegalArgumentException(
                        "El producto " + productId + " no existe. Archivo: "
                        + file.getName() + ", linea " + lineNumber);
                }

                salesman.addSale(product.getPrice() * quantity);
                product.addQuantitySold(quantity);
            }
        }
    }

    /**
     * Genera el reporte de vendedores ordenado de mayor a menor recaudo.
     */
    public static void generateSalesmanReport(Map<Long, Salesman> salesmen)
            throws IOException {
        List<Salesman> ordered = new ArrayList<Salesman>(salesmen.values());

        ordered.sort(new Comparator<Salesman>() {
            @Override
            public int compare(Salesman first, Salesman second) {
                return Double.compare(second.getTotalSales(), first.getTotalSales());
            }
        });

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALESMEN_REPORT))) {
            for (Salesman salesman : ordered) {
                writer.write(salesman.getFullName() + ";"
                    + String.format(Locale.US, "%.2f", salesman.getTotalSales()));
                writer.newLine();
            }
        }
    }

    /**
     * Genera el reporte de productos ordenado por cantidad vendida.
     */
    public static void generateProductReport(Map<Long, Product> products)
            throws IOException {
        List<Product> ordered = new ArrayList<Product>(products.values());

        ordered.sort(new Comparator<Product>() {
            @Override
            public int compare(Product first, Product second) {
                int result = Integer.compare(
                    second.getQuantitySold(), first.getQuantitySold());

                if (result != 0) {
                    return result;
                }

                return first.getName().compareToIgnoreCase(second.getName());
            }
        });

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_REPORT))) {
            for (Product product : ordered) {
            	writer.write(product.getName() + ";"
            	        + String.format(Locale.US, "%.2f", product.getPrice()));
            	writer.newLine();
            }
        }
    }

    private static long parsePositiveLong(String value, String field, int lineNumber) {
        try {
            long number = Long.parseLong(value.trim());
            if (number <= 0) {
                throw new IllegalArgumentException(
                    field + " debe ser positivo en linea " + lineNumber);
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                field + " invalido en linea " + lineNumber + ": " + value);
        }
    }

    private static int parsePositiveInt(String value, String field, int lineNumber) {
        try {
            int number = Integer.parseInt(value.trim());
            if (number <= 0) {
                throw new IllegalArgumentException(
                    field + " debe ser positiva en linea " + lineNumber);
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                field + " invalida en linea " + lineNumber + ": " + value);
        }
    }

    private static double parsePositiveDouble(String value, String field, int lineNumber) {
        try {
            double number = Double.parseDouble(value.trim());
            if (number <= 0 || Double.isNaN(number) || Double.isInfinite(number)) {
                throw new IllegalArgumentException(
                    field + " debe ser positivo en linea " + lineNumber);
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                field + " invalido en linea " + lineNumber + ": " + value);
        }
    }
}
