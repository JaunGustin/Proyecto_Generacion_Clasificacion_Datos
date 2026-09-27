/**
 * Representa un producto disponible para la venta.
 *
 * @author Juan David Gustin Andino
 * @author John Morera Barrera
 * @author Ebert Perez Ardila
 * @author Adriana del Pilar Salgado Perez
 */
public class Product {

    private final long id;
    private final String name;
    private final double price;
    private int quantitySold;

    /**
     * @param id identificador del producto
     * @param name nombre del producto
     * @param price precio unitario
     */
    public Product(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantitySold = 0;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    /**
     * Acumula unidades vendidas.
     *
     * @param quantity cantidad a agregar
     */
    public void addQuantitySold(int quantity) {
        quantitySold += quantity;
    }
}
