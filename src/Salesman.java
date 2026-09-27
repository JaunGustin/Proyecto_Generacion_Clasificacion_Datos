/**
 * Representa un vendedor y el valor total recaudado.
 *
 * @author Juan David Gustin Andino
 * @author John Morera Barrera
 * @author Ebert Perez Ardila
 * @author Adriana del Pilar Salgado Perez
 */
public class Salesman {

    private final String documentType;
    private final long documentNumber;
    private final String firstName;
    private final String lastName;
    private double totalSales;

    /**
     * @param documentType tipo de documento
     * @param documentNumber numero de documento
     * @param firstName nombres
     * @param lastName apellidos
     */
    public Salesman(String documentType, long documentNumber,
                    String firstName, String lastName) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalSales = 0.0;
    }

    public String getDocumentType() {
        return documentType;
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public double getTotalSales() {
        return totalSales;
    }

    /**
     * Incrementa el total recaudado.
     *
     * @param amount valor de la venta
     */
    public void addSale(double amount) {
        totalSales += amount;
    }
}
