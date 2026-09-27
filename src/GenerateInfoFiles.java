import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genera los archivos de entrada pseudoaleatorios requeridos por el proyecto.
 *
 * <p>La clase contiene los tres métodos solicitados en la guía:
 * createSalesMenFile, createProductsFile y createSalesManInfoFile.</p>
 *
 * @author Juan David Gustin Andino
 * @author John Morera Barrera
 * @author Ebert Perez Ardila
 * @author Adriana del Pilar Salgado Perez
 * @version 1.0
 */
public class GenerateInfoFiles {

    private static final String DATA_DIRECTORY = "data";
    private static final String SALES_DIRECTORY = DATA_DIRECTORY + File.separator + "ventas";
    private static final String PRODUCTS_FILE = DATA_DIRECTORY + File.separator + "productos.txt";
    private static final String SALESMEN_FILE = DATA_DIRECTORY + File.separator + "vendedores.txt";

    private static final int PRODUCT_COUNT = 20;
    private static final int SALESMAN_COUNT = 10;
    private static final int FILES_PER_SALESMAN = 2;

    private static final Random RANDOM = new Random(2026L);

    private static final String[] FIRST_NAMES = {
        "Ana", "Carlos", "Laura", "Miguel", "Sofia",
        "Daniel", "Valentina", "Andres", "Camila", "Juan",
        "Natalia", "Felipe", "Mariana", "David", "Paula"
    };

    private static final String[] LAST_NAMES = {
        "Gomez", "Rodriguez", "Martinez", "Lopez", "Garcia",
        "Hernandez", "Perez", "Torres", "Ramirez", "Moreno",
        "Vargas", "Castro", "Rojas", "Mendoza", "Suarez"
    };

    private static final String[] PRODUCT_NAMES = {
        "Teclado", "Mouse", "Monitor", "Audifonos", "Webcam",
        "Parlantes", "Memoria USB", "Disco SSD", "Cable HDMI", "Router",
        "Impresora", "Camara", "Microfono", "Base Laptop", "Tablet",
        "Smartwatch", "Cargador", "Memoria RAM", "Hub USB", "Adaptador"
    };

    /**
     * Punto de entrada del generador. No solicita información al usuario.
     *
     * @param args argumentos de ejecución; no se utilizan
     */
    public static void main(String[] args) {
        try {
            prepareDirectories();
            cleanSalesDirectory();

            createProductsFile(PRODUCT_COUNT);
            createSalesManInfoFile(SALESMAN_COUNT);

            List<SalesmanSeed> salesmen = readGeneratedSalesmen();

            for (SalesmanSeed salesman : salesmen) {
                for (int fileNumber = 1; fileNumber <= FILES_PER_SALESMAN; fileNumber++) {
                    int randomSalesCount = 5 + RANDOM.nextInt(11);
                    createSalesMenFile(randomSalesCount, salesman.name, salesman.id);
                }
            }

            System.out.println("Generacion de archivos finalizada exitosamente.");
        } catch (IOException | RuntimeException e) {
            System.err.println("Error durante la generacion de archivos: " + e.getMessage());
        }
    }

    /**
     * Crea un archivo pseudoaleatorio de ventas para un vendedor.
     *
     * @param randomSalesCount cantidad de registros de venta
     * @param name nombre del vendedor
     * @param id identificador del vendedor
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        if (randomSalesCount <= 0 || id <= 0 || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Los datos del vendedor no son validos.");
        }

        File directory = new File(SALES_DIRECTORY);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("No fue posible crear la carpeta de ventas.");
        }

        int fileNumber = countFilesForSalesman(id) + 1;
        String fileName = "ventas_" + id + "_" + fileNumber + ".txt";
        File file = new File(directory, fileName);

        List<Long> productIds = readProductIds();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("CC;" + id);
            writer.newLine();

            for (int i = 0; i < randomSalesCount; i++) {
                long productId = productIds.get(RANDOM.nextInt(productIds.size()));
                int quantity = 1 + RANDOM.nextInt(10);
                writer.write(productId + ";" + quantity + ";");
                writer.newLine();
            }
        }
    }

    /**
     * Crea el archivo con productos pseudoaleatorios.
     *
     * @param productsCount cantidad de productos
     */
    public static void createProductsFile(int productsCount) throws IOException {
        if (productsCount <= 0 || productsCount > PRODUCT_NAMES.length) {
            throw new IllegalArgumentException("La cantidad de productos no es valida.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
            for (int i = 0; i < productsCount; i++) {
                long productId = 1000L + i;
                String productName = PRODUCT_NAMES[i];
                int price = 50000 + RANDOM.nextInt(451) * 1000;
                writer.write(productId + ";" + productName + ";" + price);
                writer.newLine();
            }
        }
    }

    /**
     * Crea el archivo con la informacion de los vendedores.
     *
     * @param salesmanCount cantidad de vendedores
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        if (salesmanCount <= 0) {
            throw new IllegalArgumentException("La cantidad de vendedores debe ser positiva.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALESMEN_FILE))) {
            for (int i = 0; i < salesmanCount; i++) {
                long id = 10000000L + i + 1;
                String firstName = FIRST_NAMES[i % FIRST_NAMES.length];
                String lastName = LAST_NAMES[(i * 3) % LAST_NAMES.length];

                writer.write("CC;" + id + ";" + firstName + ";" + lastName);
                writer.newLine();
            }
        }
    }

    private static void prepareDirectories() throws IOException {
        File dataDirectory = new File(DATA_DIRECTORY);
        File salesDirectory = new File(SALES_DIRECTORY);
        File outputDirectory = new File("output");

        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            throw new IOException("No fue posible crear data.");
        }
        if (!salesDirectory.exists() && !salesDirectory.mkdirs()) {
            throw new IOException("No fue posible crear data/ventas.");
        }
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            throw new IOException("No fue posible crear output.");
        }
    }

    private static void cleanSalesDirectory() {
        File directory = new File(SALES_DIRECTORY);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }

    private static int countFilesForSalesman(long id) {
        File directory = new File(SALES_DIRECTORY);
        File[] files = directory.listFiles();
        int count = 0;

        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith("ventas_" + id + "_")) {
                    count++;
                }
            }
        }
        return count;
    }

    private static List<Long> readProductIds() throws IOException {
        List<Long> ids = new ArrayList<Long>();
        java.io.BufferedReader reader = new java.io.BufferedReader(
            new java.io.FileReader(PRODUCTS_FILE));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] fields = line.split(";");
                    ids.add(Long.parseLong(fields[0]));
                }
            }
        } finally {
            reader.close();
        }
        return ids;
    }

    private static List<SalesmanSeed> readGeneratedSalesmen() throws IOException {
        List<SalesmanSeed> salesmen = new ArrayList<SalesmanSeed>();
        java.io.BufferedReader reader = new java.io.BufferedReader(
            new java.io.FileReader(SALESMEN_FILE));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] fields = line.split(";");
                    long id = Long.parseLong(fields[1]);
                    String name = fields[2] + " " + fields[3];
                    salesmen.add(new SalesmanSeed(id, name));
                }
            }
        } finally {
            reader.close();
        }
        return salesmen;
    }

    /**
     * Pequeña estructura auxiliar para transportar id y nombre.
     */
    private static class SalesmanSeed {
        private final long id;
        private final String name;

        SalesmanSeed(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
