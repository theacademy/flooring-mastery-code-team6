package service;

import dto.Order;
import dto.Product;
import dto.Tax;
import enums.EditSpec;

import java.awt.geom.Area;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FlooringMasterServiceLayer {

    Order addOrder(Order order) throws IOException;
    Order removeOrder(int orderNumber, LocalDate date);
    void editOrder(Order newOrder) throws IOException;
    List<Order> getAllOrders();
    int getNewOrderNumber() throws IOException;
    Map<String, Product> getAllProducts() throws FileNotFoundException;
    Map<String, Tax> getAllTaxes() throws FileNotFoundException;

    public void exportAllData() throws IOException;

    BigDecimal validateArea();

    Tax validateTaxState() throws FileNotFoundException;

    String validateCustomerName();

    LocalDate validateFutureOrderDate();


    Product validateProductType() throws FileNotFoundException;

    Order getUserNewOrder() throws IOException;
}