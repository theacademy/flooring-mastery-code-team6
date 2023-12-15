package service;

import dto.Order;
import enums.EditSpec;

import java.awt.geom.Area;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasterServiceLayer {

    Order addOrder(Order order) throws IOException;
    Order removeOrder(int orderNumber, LocalDate date);
    Order editOrder(int orderNumber,  LocalDate date, EditSpec editSelect);
    List<Order> getAllOrders();
    public int getNewOrderNumber() throws IOException;
    Order promptUserAddOrder() throws IOException;
}