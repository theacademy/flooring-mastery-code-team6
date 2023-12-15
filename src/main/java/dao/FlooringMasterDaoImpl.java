package dao;

import dto.Order;
import dto.Product;
import dto.Tax;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class FlooringMasterDaoImpl implements FlooringMasterDao {


    Map<Integer, String> dateOrder;
    Map<Integer, Order> orderInventory;
    Map<String, Product> products;
    Map<String, Tax> taxes;

    private final String PRODUCT_FILE = "Products.txt";
    private final String TAX_FILE = "Taxes.txt";
    private final String DELIMITER = ",";

    public FlooringMasterDaoImpl() {

        dateOrder = new HashMap<>();
        orderInventory = new HashMap<>();
        products = new HashMap<>();
        taxes = new HashMap<>();

    }

    @Override
    public Map<String, Product> getAllProducts () throws FileNotFoundException {
        readProduct();
        return products;
    }

    @Override

    public Map<String, Tax> getAllTaxRates() throws FileNotFoundException {
        loadFromTaxFile(); // load the information from the taxes file
        return taxes; // return the state and tax rates information
    }

    @Override
    public boolean checkValidOrder(int orderNumber, LocalDate orderdate) {
        if (dateOrder.containsKey(orderdate.toString())) {
            if (dateOrder.get(orderNumber).equals(orderdate.toString())) {
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {


        if (checkValidOrder(orderNumber, orderDate)) {
            dateOrder.remove(orderNumber);
            return orderInventory.remove(orderNumber);

        } else {
            return null;
        }
    }


    @Override
    public Order addOrder(int orderNumber, LocalDate orderDate, Order order) {
        Order added = orderInventory.put(orderNumber, order);

        //.getOrderDate() returns a LocalDate, needs to convert it to string to print

        dateOrder.put(orderNumber, order.getOrderDate().toString());
        return added;
    }

    @Override
    public Order editOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }

    public void readProduct() throws FileNotFoundException {

        try {
            File file = new File(PRODUCT_FILE);
            Scanner sc = new Scanner(file);

            // Skip the header
            if (sc.hasNextLine()) {
                sc.nextLine(); // Skip the first line (header)
            }

            // Read and store product information
            while (sc.hasNextLine()) {
                Product product = unmarshallProduct(sc.nextLine());
                products.put(product.getProductType(), product);
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + PRODUCT_FILE);
            e.printStackTrace();
        }
    }

    // convert lines in the tax file to a tax object
    private Product unmarshallProduct(String productLine) {

        // split the string into state abreviation, state, and tax rate
        String[] values = productLine.split(DELIMITER);

        // store the values in a tax object and return it
        String[] productData = productLine.split(DELIMITER);
        String productType = productData[0];
        BigDecimal costPerSquareFoot = new BigDecimal(productData[1]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(productData[2]);

        // Create a Product object and add it to the list
        return new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
    }


    private void loadFromTaxFile() throws FileNotFoundException {

        try {
            File taxFile = new File(TAX_FILE);
            Scanner sc = new Scanner(taxFile);

            // skip the first line - header
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            // go through the file
            while (sc.hasNextLine()) {
                Tax tax = unmarshallTax(sc.nextLine());
                taxes.put(tax.getStateAbbreviation(), tax);
            }

            sc.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + TAX_FILE);
            e.printStackTrace();
        }


    }


    // convert lines in the tax file to a tax object
    private Tax unmarshallTax(String taxLine) {

        // split the string into state abreviation, state, and tax rate

        String[] taxData = taxLine.split(DELIMITER);

        // store the values in a tax object and return it
        String stateAbbreviation = taxData[0];
        String stateName = taxData[1];
        BigDecimal taxRate = new BigDecimal(taxData[2]);

        return new Tax(stateAbbreviation, stateName, taxRate);

    }

}

