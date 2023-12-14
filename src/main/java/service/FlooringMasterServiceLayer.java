package service;

import java.awt.geom.Area;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface FlooringMasterServiceLayer {

    Order addOrder(LocalDate orderDate, String name, String State, String productType, BigDecimal area);
    Order removeOrder(int orderNumber, LocalDate date);
    Order editOrder(int orderNumber,  LocalDate date, EditSpec editSelect);
    BigDecimal CalculateMaterialCost(Area, CostPerSquareFoot);
    BigDecimal CalculateLaborCost (Area,LaborCostPerSquareFoot);
    BigDecimal CalculateTax (TaxRate);
    BigDecimal CalculateTotal();
    List<Order> getAllOrders();

}
