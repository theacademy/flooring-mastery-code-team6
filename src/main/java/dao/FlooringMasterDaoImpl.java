package dao;

import dto.Order;
import dto.Product;
import dto.Tax;

import java.awt.geom.Area;
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
    Map<String, ArrayList<Integer>> dateOrder = new HashMap<>();
    Map<Integer, Order> orderInventory = new HashMap<>();

    Map<String, Product> products;

    Map<String, Tax> taxes;

    private final String PRODUCT_FILE = "Products.txt";
    private final String TAX_FILE = "Taxes.txt";
    private final String DELIMITER = ",";


    public FlooringMasterDaoImpl() throws IOException {

        dateOrder = new HashMap<>();
        orderInventory = new HashMap<>();
        products = new HashMap<>();
        taxes = new HashMap<>();

        //Load in all the info from file
        readInAllOrderFromFile();

    }

    public Map<String, ArrayList<Integer>> getDateOrder(){
        return dateOrder;
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


    /**
     * Removes an order from the inventory based on the provided order number and order date.
     *
     * @param orderNumber The unique identifier of the order to be removed.
     * @param orderDate   The date associated with the order to be removed.
     * @return The removed order if it exists and is valid, null otherwise.
     */
    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        if (checkValidOrder(orderNumber, orderDate)) {
            dateOrder.remove(orderDate);
            return orderInventory.remove(orderNumber);
        } else {
            return null;
        }
    }


    @Override
    public Order addOrder(Order order) throws IOException {

        orderInventory.put(order.getOrderNumber(), order);

        ArrayList<Integer> tempArray = dateOrder.get(order.getOrderDate().toString());
             if (tempArray== null) {
                 tempArray = new ArrayList<>();
                 tempArray.add(order.getOrderNumber());
             }
             else {
                 tempArray.add(order.getOrderNumber());
             }
        System.out.println("Size"+tempArray.size());



        dateOrder.put(order.getOrderDate().toString(),tempArray);

        return order;
    }

    @Override
    public void editOrder(Order newOrder) throws IOException {
        orderInventory.replace(newOrder.getOrderNumber(), newOrder);
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





    @Override
    public void writeOrderToFile() throws IOException {

        for (String i : dateOrder.keySet()){

            LocalDate fileDate = LocalDate.parse(i);
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/dd/YYYY");
            String fileName = "Orders_" + fileDate.format(formatters);
            fileName = fileName.replace("/", "");
            String folderPath = "Order";

            File file = new File(folderPath + "\\" + fileName);
            FileWriter myWriter = new FileWriter(file, false);
            for(int j : dateOrder.get(i)) {
                String writeString = marhsallOrder(orderInventory.get(j));

                myWriter.write(writeString + "\n");

            }
            myWriter.flush();
            myWriter.close();


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

        marshallString = marshallString + order.getProductType().toString()+ DELIMITER;

        marshallString = marshallString + order.getArea().toString() + DELIMITER;

        marshallString = marshallString + order.getCostPerSqFoot().toString() + DELIMITER;

        marshallString = marshallString + order.getLaborCostPerSqFoot().toString() + DELIMITER;

        marshallString = marshallString + order.getMaterialCost().toString() + DELIMITER;

        marshallString = marshallString + order.getLaborCost().toString() + DELIMITER;

        marshallString = marshallString + order.getTax().toString() + DELIMITER;

        marshallString = marshallString + order.getTotal().toString();
        return marshallString;

    }

    private Order unmarshallOrder(String fileLine, String fileName){


        String[] split = (fileLine.split(","));
        int orderNumber = Integer.parseInt(split[0]);

        // Create proper date from the file name. File name date is mmddyyyy. Convert to Local Date
        String dateFromFile = fileName.split("_")[1];
        int year = Integer.parseInt(dateFromFile.substring(4,8));
        int month = Integer.parseInt(dateFromFile.substring(0,2));
        int date = Integer.parseInt(dateFromFile.substring(2,4));


        LocalDate orderDate = LocalDate.of(year,month,date);
        String customerName = split[1];
        String state = split[2];
        String productType = split[4];
        BigDecimal area = new BigDecimal(split[5]);

        //Create new order object based on the given information
        Order retrievedOrder = new Order(orderNumber,orderDate,customerName,state,productType,area);

        BigDecimal taxRate = new BigDecimal(split[3]);
        retrievedOrder.setTaxRate(taxRate);

        BigDecimal costPerSquareFoot = new BigDecimal(split[6]);
        retrievedOrder.setCostPerSqFoot(costPerSquareFoot);

        BigDecimal laborPerSquareFoot = new BigDecimal(split[7]);
        retrievedOrder.setLaborCostPerSqFoot(laborPerSquareFoot);

        BigDecimal materialCost = new BigDecimal(split[8]);
        retrievedOrder.setMaterialCost(materialCost);

        BigDecimal laborCost = new BigDecimal(split[9]);
        retrievedOrder.setLaborCost(laborCost);

        BigDecimal tax = new BigDecimal(split[10]);
        retrievedOrder.setTax(tax);

        BigDecimal total = new BigDecimal(split[11]);
        retrievedOrder.setTotal(total);

        return retrievedOrder;

    }


    private void readInAllOrderFromFile() throws IOException {
        File dir = new File("Order");
        for (File file : dir.listFiles()) {
            Scanner s = new Scanner(file);

            while(s.hasNextLine()) {
                String nextLine = s.nextLine();
                String fileName = file.toString();
                Order retrievedOrder = unmarshallOrder(nextLine, fileName);

                this.addOrder(retrievedOrder);
            }
            s.close();
        }
    }


}


