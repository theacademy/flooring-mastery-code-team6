package service;

import dao.FlooringMasterDao;
import dto.Order;
import dto.Product;
import dto.Tax;
import ui.FlooringMasterView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
        Order returnOrder = dao.addOrder(order);
        return returnOrder;

    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate date) {
        List<Order> allOrders = dao.getAllOrders();
        Order orderToRemove = findOrderByNumberAndDate(allOrders, orderNumber, date.toString());

        if (orderToRemove != null) {
            dao.removeOrder(orderNumber, date);
            return orderToRemove;
        }

        return null;
    }

    @Override
    public void editOrder(Order newOrder) throws IOException {
        if (newOrder != null) {
            dao.editOrder(newOrder);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return dao.getAllOrders();
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
        String customerName = view.promptCustomerName("Enter customer's name: ");
        Tax state = view.promptTax(dao.getAllTaxRates(), "Enter state (e.g., NY for New York): ");
        Product productType = view.promptProductType(dao.getAllProducts(), "");
        BigDecimal area = view.promptArea("Enter area (minimum 100 sq ft): ");

        int orderNumber = getNewOrderNumber();
        Order order = new Order(orderNumber, futureDate, customerName, state.getStateAbbreviation(), productType.getProductType(), area);
        order.setTaxRate(state.getTaxRate());
        order.setCostPerSqFoot(productType.getCostPerSquareFoot());
        order.setLaborCostPerSqFoot(productType.getLaborCostPerSquareFoot());
        order.calculateOrderCosts();

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

    private Order findOrderByNumberAndDate(List<Order> orders, int orderNumber, String orderDate) {
        for (Order order : orders) {
            if (order.getOrderNumber() == orderNumber && order.getOrderDate().toString().equals(orderDate)) {
                return order;
            }
        }
        return null;
    }

    public Map<String, Product> getAllProducts() throws FileNotFoundException {
        return dao.getAllProducts();
    }

    public Map<String, Tax> getAllTaxes() throws FileNotFoundException {
        return dao.getAllTaxRates();
    }
}
