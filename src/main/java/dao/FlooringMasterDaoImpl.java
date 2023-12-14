package dao;

import java.time.LocalDate;
import java.util.HashMap;

public class FlooringMasterDaoImpl implements FlooringMasterDao{


    HashMap<String, Integer> dateOrder;

    HashMap<Integer, Order> orderInventory;
    @Override
    public boolean checkValidOrder(int orderNumber, LocalDate orderdate) {
        return false;
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }

    @Override
    public Order addOrder(int orderNumber, Order order) {
        Order added = orderInventory.put(orderNumber,order);

        //.getOrderDate() returns a LocalDate, needs to convert it to string to print
        dateOrder.put(Order.getOrderDate().toString(), orderNumber);
        return added;
    }

    @Override
    public Order editOrder(int orderNumber, LocalDate orderDate) {
        return null;
    }
}
