package service;

import dao.FlooringMasterDao;
import dto.Order;
import enums.EditSpec;
import ui.FlooringMasterView;

import java.awt.geom.Area;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasterServiceLayeriImpl implements FlooringMasterServiceLayer{
    FlooringMasterDao OrderDao;
    FlooringMasterView OrderView;

    public FlooringMasterServiceLayeriImpl (FlooringMasterDao OrderDao){
        this.OrderDao= OrderDao;
//        this.OrderView= OrderView;
    }

    @Override
    public Order addOrder(LocalDate orderDate, String name, String State, String productType, BigDecimal area) {
        // read from text file to get next orderNumber
        return null;
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate date) {
        return null;
    }

    @Override
    public Order editOrder(int orderNumber, LocalDate date, EditSpec editSelect) {
        return null;
    }

    @Override
    public BigDecimal CalculateMaterialCost(Area area, BigDecimal CostPerSquareFoot) {
        return null;
    }

    @Override
    public BigDecimal CalculateLaborCost(Area area, BigDecimal LaborCostPerSquareFoot) {
        return null;
    }

    @Override
    public BigDecimal CalculateTax(BigDecimal TaxRate) {
        return null;
    }

    @Override
    public BigDecimal CalculateTotal() {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }
}
