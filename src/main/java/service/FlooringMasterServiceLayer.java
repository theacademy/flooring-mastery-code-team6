package service;

import dto.Order;
import enums.EditSpec;

import java.awt.geom.Area;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasterServiceLayer {

    Order addOrder(LocalDate orderDate, String name, String State, String productType, BigDecimal area);
    Order removeOrder(int orderNumber, LocalDate date);
    Order editOrder(int orderNumber,  LocalDate date, EditSpec editSelect);
    BigDecimal CalculateMaterialCost(Area area, BigDecimal CostPerSquareFoot);
    BigDecimal CalculateLaborCost (Area area,BigDecimal LaborCostPerSquareFoot);
    BigDecimal CalculateTax (BigDecimal TaxRate);
    BigDecimal CalculateTotal();
    List<Order> getAllOrders();

}