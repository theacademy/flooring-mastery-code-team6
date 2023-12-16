package service;

import dao.FlooringMasterDao;
import dto.Order;
import dto.Product;
import dto.Tax;
import ui.FlooringMasterView;

import java.io.File;
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
    public void editOrder() throws IOException {
        LocalDate localDate = view.promptOrderDate();
        int orderNumber = view.getIo().readInt("Enter Order Number?");
        Order oldOrder = view.findOrderByNumberAndDate(dao.getAllOrders(), orderNumber, localDate.toString());
        if (oldOrder != null) {
            String newCustomerName = view.promptCustomerName("Enter customer name (" + oldOrder.getCustomerName() + "): ");
            String newState = view.promptState(dao.getAllTaxRates(), "Enter state (" + oldOrder.getState() + "): ");
            Product product = view.promptProductType(dao.getAllProducts(), "Enter product type (" + oldOrder.getProductType() + "): ");
            BigDecimal newArea = view.promptArea("Enter area (" + oldOrder.getArea() + "): ");

            Order newOrder = new Order(oldOrder.getOrderNumber(), oldOrder.getOrderDate(), newCustomerName, newState, product.getProductType(), newArea);
            newOrder.setTaxRate(dao.getAllTaxRates().get(newState).getTaxRate());
            newOrder.setCostPerSqFoot(product.getCostPerSquareFoot());
            newOrder.setLaborCostPerSqFoot(product.getLaborCostPerSquareFoot());
            newOrder.calculateOrderCosts();
            // Display a summary of the updated order
            view.displayOrderSummary(newOrder);

            char confirmation = view.getIo().readChar("Do you want to save these changes? (Y/N): ");
            if (confirmation == 'Y') {
                // Save the changes (you may implement this part based on your data storage mechanism)
                dao.editOrder(newOrder, oldOrder);
                view.getIo().print("Changes saved successfully.");
            } else {
                view.getIo().print("Changes discarded.");
            }




        } else {
            view.getIo().print("No orders found!");
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
}
