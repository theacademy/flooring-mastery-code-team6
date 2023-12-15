package dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import dto.Order;
import dto.Product;
import dto.Tax;

import javax.swing.*;

public interface FlooringMasterDao {

    Map<String, Product> getAllProducts();

    Map<String, Tax> getAllTaxRates();

    boolean checkValidOrder(int orderNumber, LocalDate orderdate);
    Order removeOrder(int orderNumber, LocalDate orderDate);

    Order addOrder(int orderNumber, LocalDate orderDate, Order order);

    Order editOrder(int orderNumber, LocalDate orderDate);


}
