package service;

import dto.Order;
import dto.Product;
import dto.Tax;

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

    BigDecimal getArea();
    boolean validateArea(BigDecimal area);

    Tax getTaxState() throws FileNotFoundException;

    boolean validateTaxState(String state, Map<String, Tax> taxes) throws FileNotFoundException;

    String getCustomerName();
    boolean validateCustomerName(String customerName);

    LocalDate getFutureDate();

    boolean validateFutureOrderDate(String orderDate);

    Product getProductType() throws FileNotFoundException;

    boolean validateProductType(String selectedProductType, Map<String,Product> products);

    Order getUserNewOrder() throws IOException;
}