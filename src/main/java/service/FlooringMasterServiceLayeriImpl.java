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



}
