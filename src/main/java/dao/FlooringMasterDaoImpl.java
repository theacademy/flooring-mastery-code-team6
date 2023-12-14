package dao;

import dto.Order;
import dto.Product;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class FlooringMasterDaoImpl implements FlooringMasterDao{
    private static final String PRODUCT_FILE_PATH = "Products.txt";

    HashMap<Integer,String> dateOrder;

    HashMap<Integer, Order> orderInventory;

    @Override
    public Map<String, Product> getProducts() {
        Map<String, Product> products = new HashMap<>();
        try(Scanner sc = new Scanner(new File(PRODUCT_FILE_PATH))) {
            if(sc.hasNextLine()){
                sc.nextLine();
            }

            // Read product information
            while(sc.hasNextLine()){
                String[] parts = sc.nextLine().split(",");
                if(parts.length == 3){
                    String productType = parts[0];
                    BigDecimal costPerSquareFoot = new BigDecimal(parts[1]);
                    BigDecimal laborCostPerSquareFoot = new BigDecimal(parts[2]);

                    // Create Product instance and add it to the map
                    products.put(productType, new Product(productType, costPerSquareFoot, laborCostPerSquareFoot));
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public boolean checkValidOrder(int orderNumber, LocalDate orderdate) {
        if(dateOrder.containsKey(orderdate.toString())){
            if (dateOrder.get(orderNumber).equals(orderdate.toString())){
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
        Order added = orderInventory.put(orderNumber,order);

        //.getOrderDate() returns a LocalDate, needs to convert it to string to print

        dateOrder.put( orderNumber,order.getOrderDate().toString());
        return added;
    }

    @Override
    public Order editOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }
}
