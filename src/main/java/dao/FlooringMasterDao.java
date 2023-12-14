package dao;

import java.time.LocalDate;
import java.util.HashMap;

import dto.Order;

import javax.swing.*;

public interface FlooringMasterDao {



    boolean checkValidOrder(int orderNumber, LocalDate orderdate);
    Order removeOrder(int orderNumber, LocalDate orderDate);

    Order addOrder(int orderNumber, LocalDate orderDate, Order order);

    Order editOrder(int orderNumber, LocalDate orderDate);


}
