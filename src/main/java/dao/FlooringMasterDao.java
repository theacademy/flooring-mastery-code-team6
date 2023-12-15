package dao;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dto.Order;
import dto.Product;
import dto.Tax;

public interface FlooringMasterDao {

    Map<String, Product> getAllProducts() throws FileNotFoundException;

    Map<String, Tax> getAllTaxRates() throws FileNotFoundException;

    boolean checkValidOrder(int orderNumber, LocalDate orderdate);
    Order removeOrder(int orderNumber, LocalDate orderDate);

    Order addOrder(Order order) throws IOException;

    Order editOrder(int orderNumber, LocalDate orderDate);

    List<Order> getAllOrders();


}