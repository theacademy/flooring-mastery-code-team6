package dao;

import dto.Order;
import dto.Product;
import dto.Tax;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;


public class FlooringMasterDaoImpl implements FlooringMasterDao {
    HashMap<String, ArrayList<Integer>> dateOrder = new HashMap<>();
    HashMap<Integer, Order> orderInventory = new HashMap<>();

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

        String dateInString = orderdate.toString();
        if(dateOrder.containsKey(orderdate.toString())){
            if (dateOrder.get(dateInString).contains(orderNumber)){

                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    //Incorrectly implemented, needs to change
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
    public Order addOrder(Order order) throws IOException {
    // need to remove orderNumber and OrderDate from Signature

        Order added = orderInventory.put(order.getOrderNumber(), order); // --> should change to Order added = orderInventory.put(order.getOrderNumber(),order)



        ArrayList<Integer> tempArray;
        try {
             tempArray = dateOrder.get(order.getOrderDate().toString()); // need to replace with order.getOrderDate()
             if (tempArray== null)
                 tempArray = new ArrayList<>();
            tempArray.add(order.getOrderNumber());
        }
        catch (Exception e){
            tempArray = new ArrayList<>();
            tempArray.add(order.getOrderNumber());
        }


        dateOrder.put(order.getOrderDate().toString(),tempArray);

        return added;
    }

    @Override
    public Order editOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orderInventory.values());
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





    private void writeOrderToFile() throws IOException {
        for (String i : dateOrder.keySet()){

            LocalDate fileDate = LocalDate.parse(i);
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/dd/YYYY");
            String fileName = "Orders_" + fileDate.format(formatters);
            fileName = fileName.replace("/", "");

            for(int j : dateOrder.get(i)) {
                String writeString = marhsallOrder(orderInventory.get(j));

                FileWriter myWriter = new FileWriter(fileName, true);
                myWriter.write(writeString);
                myWriter.close();
            }


        }

//        String fileName =
//        File myObj = new File("orderNumberTracker.txt");
    }
    /*
    Write both order inventory to file and order date. File name should be the date
    and every order with that date should be in the same file.
    OrderNumber – Integer
    CustomerName – String
    State – String
    TaxRate – BigDecimal
    ProductType – String
    Area – BigDecimal
    CostPerSquareFoot – BigDecimal
    LaborCostPerSquareFoot – BigDecimal
    MaterialCost – BigDecimal
    LaborCost – BigDecimal
    Tax – BigDecimal
    Total – BigDecimal
     */
    private String marhsallOrder(Order order){

        final String DELIMITER = ",";
        String marshallString = "";

        marshallString = marshallString + order.getOrderNumber() + DELIMITER;

        marshallString = marshallString + order.getCustomerName() + DELIMITER;

        marshallString = marshallString + order.getState() + DELIMITER;

        marshallString = marshallString + order.getTaxRate().toString()+ DELIMITER;

        marshallString = marshallString + order.getArea().toString() + DELIMITER;

        marshallString = marshallString + order.getCostPerSqFoot().toString() + DELIMITER;

        marshallString = marshallString + order.getLaborCostPerSqFoot().toString() + DELIMITER;

        marshallString = marshallString + order.getOrderNumber() + DELIMITER;

        marshallString = marshallString + order.getMaterialCost().toString() + DELIMITER;

        marshallString = marshallString + order.getLaborCost().toString() + DELIMITER;

        marshallString = marshallString + order.getTax().toString() + DELIMITER;

        marshallString = marshallString + order.getTotal().toString();
        return marshallString;

    }

}


