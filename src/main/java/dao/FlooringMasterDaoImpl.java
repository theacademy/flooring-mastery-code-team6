package dao;

import dto.Order;

import java.time.LocalDate;
import java.util.HashMap;

public class FlooringMasterDaoImpl implements FlooringMasterDao{


    HashMap<Integer,String> dateOrder;

    HashMap<Integer, Order> orderInventory;
    @Override
    public boolean checkValidOrder(int orderNumber, LocalDate orderdate) {
        if(dateOrder.containsKey(orderdate.toString())){
            if (dateOrder.get(orderdate.toString()) == orderNumber){
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
