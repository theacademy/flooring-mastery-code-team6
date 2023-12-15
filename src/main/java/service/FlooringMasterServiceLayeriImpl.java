package service;

import dao.FlooringMasterDao;
import dto.Order;
import enums.EditSpec;
import ui.FlooringMasterView;

import java.awt.geom.Area;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class FlooringMasterServiceLayeriImpl implements FlooringMasterServiceLayer{
    FlooringMasterDao dao;
    FlooringMasterView view;

    public FlooringMasterServiceLayeriImpl (FlooringMasterDao OrderDao){
        this.dao= OrderDao;
//        this.OrderView= OrderView;
    }

    @Override
    public Order addOrder(Order order) throws IOException {
        // read from text file to get next orderNumber
        return dao.addOrder(getNewOrderNumber(), order.getOrderDate(), order);
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


    @Override
    public int getNewOrderNumber() throws IOException {

        //Read in previous order number
        File myObj = new File("orderNumberTracker.txt");
        Scanner myReader = new Scanner(myObj);
        int retrievedNumber = myReader.nextInt();
        int newNumber = retrievedNumber + 1;


        //write the new order back to the file
        FileWriter myWriter = new FileWriter("orderNumberTracker.txt",false);
        myWriter.write(Integer.toString(newNumber));
        myWriter.close();


        return newNumber;
    }


    public Order promptUserAddOrder() throws IOException {
        view.getIo().print("");
        view.getIo().print("Enter your order details below: ");

        LocalDate futureDate = view.promptFutureOrderDate();
        String customerName = view.promptCustomerName();
        String state = view.promptState(dao.getAllTaxRates());
        String productType = view.promptProductType(dao.getAllProducts());
        BigDecimal area = view.promptArea();

        int orderNumber = getNewOrderNumber();
        Order order = new Order(orderNumber, futureDate, customerName, state, productType, area);

        view.displayOrderSummary(order);

        char confirmation = view.getIo().readChar("Do you want to place this order? (Y/N): ");
        if (confirmation == 'N') {
            view.getIo().print("Order canceled.");
            return null;
        }

        addOrder(order);
        view.getIo().print("Order placed successfully. Order number: " + orderNumber);

        return order;
    }


}
