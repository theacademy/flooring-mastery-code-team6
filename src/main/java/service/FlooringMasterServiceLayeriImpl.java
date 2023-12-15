package service;

import dao.FlooringMasterDao;
import dto.Order;
import dto.Product;
import dto.Tax;
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

    public FlooringMasterServiceLayeriImpl (FlooringMasterDao OrderDao, FlooringMasterView orderView){
        this.dao= OrderDao;
        this.view= orderView;
    }

    @Override
    public Order addOrder(Order order) throws IOException {

        // read from text file to get next orderNumber
        Order returnOrder = dao.addOrder(getNewOrderNumber(), order.getOrderDate(), order);
        return returnOrder;

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
        int newNumber = 0;

        //Try reading in the last number in orderNumberTracker.txt
        //If there is no number, throw error and set newNumber to 1
        Scanner myReader = new Scanner(myObj);
        try {
            String retrievedNumber = myReader.nextLine();
            newNumber = Integer.parseInt(retrievedNumber) + 1;
        }
        catch (Exception e){
            newNumber = newNumber + 1;
        }



        //Write the new order number to file
        FileWriter myWriter = new FileWriter("orderNumberTracker.txt",false);

        //write the new order back to the file
        myWriter.write(Integer.toString(newNumber));
        myWriter.close();


        return newNumber;
    }


    public Order promptUserAddOrder() throws IOException {
        view.getIo().print("");
        view.getIo().print("Enter your order details below: ");

        LocalDate futureDate = view.promptFutureOrderDate();
        String customerName = view.promptCustomerName();
        Tax state = view.promptState(dao.getAllTaxRates());
        Product productType = view.promptProductType(dao.getAllProducts());
        BigDecimal area = view.promptArea();

        int orderNumber = 1;
//                getNewOrderNumber();
        Order order = new Order(orderNumber, futureDate, customerName, state.getStateAbbreviation(), productType.getProductType(), area);
        order.setTaxRate(state.getTaxRate());
        order.setCostPerSqFoot(productType.getCostPerSquareFoot());
        order.setLaborCostPerSqFoot(productType.getLaborCostPerSquareFoot());

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
