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

public class FlooringMasterDaoImpl implements FlooringMasterDao{

    HashMap<String, ArrayList<Integer>> dateOrder = new HashMap<>();
    HashMap<Integer, Order> orderInventory = new HashMap<>();
    Map<String, Tax> taxes;

    private final String TAXRATE_FILE = "src\\Taxes.txt";
    private final String DELIMITER = ",";



    @Override
    public Map<String, Product> getAllProducts() {
        return null;
    }

    @Override
    public Map<String, Tax> getAllTaxRates() {
        loadFromTaxFile(); // load the information from the taxes file
        return taxes; // return the state and tax rates information
    }

    @Override
    public boolean checkValidOrder(int orderNumber, LocalDate orderdate) {

        String dateInString = orderdate.toString();
        if(dateOrder.containsKey(orderdate.toString())){
            if (dateOrder.get(dateInString).contains(orderNumber)){
                return true;

            }
            else {
                return false;
            }
        }
        else{
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
    public Order addOrder(int orderNumber, LocalDate orderDate, Order order) throws IOException {


        Order added = orderInventory.put(orderNumber,order);


        ArrayList<Integer> tempArray;
        try {
             tempArray = dateOrder.get(orderDate.toString());
            tempArray.add(orderNumber);
        }
        catch (Exception e){
            tempArray = new ArrayList<>();
            tempArray.add(orderNumber);
        }

        dateOrder.put(order.getOrderDate().toString(),tempArray);
        writeOrderToFile();

        return added;
    }

    @Override
    public Order editOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }


    private void loadFromTaxFile() {

        try {
            File taxFile = new File(TAXRATE_FILE);
            Scanner sc = new Scanner(taxFile);

            // skip the first line - header
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            // go through the file
            while(sc.hasNextLine()) {

                Tax tax = unmarshallTax(sc.nextLine());
                taxes.put(tax.getStateName(), tax);

            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


    // convert lines in the tax file to a tax object
    private Tax unmarshallTax(String taxLine) {

        // split the string into state abreviation, state, and tax rate
        String[] values = taxLine.split(DELIMITER);

        // store the values in a tax object and return it
        return new Tax(values[0], values[1], new BigDecimal(values[2]));

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
