package ui;

import dto.Order;
import dto.Product;
import dto.Tax;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FlooringMasterView {

    private final UserIO io;

    // @Autowired
    public FlooringMasterView(UserIO io) {
        this.io = io;
    }

    public UserIO getIo() {
        return io;
    }

    public int displayMenu() {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return Integer.parseInt(io.readString("Please select from the choices (1-6): "));
    }


    public void displayAddSuccessBanner(){
        io.print("Order has been placed!");
    }

    public void pressEnterToGoBack() {
        io.readString("Press Enter to continue. ");
    }

    public void displayOrderSummary(Order order) {
        io.print("\nOrder Summary: ");
        io.print("Order Date: " + order.getOrderDate());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea());
        io.print("Tax Rate: $" + order.getTaxRate());
    }

    public void displayOrders(List<Order> orders) {
        if (orders == null) {
            io.print("There is no orders at all!");
            return;
        }

        io.print("");
        io.print("Display Orders for a Specific Date: ");


        LocalDate dateToDisplay = LocalDate.parse(io.readString("Enter the date (YYYY-MM-DD): "));

        List<Order> ordersForDate = getOrdersForDate(dateToDisplay, orders);

        if (ordersForDate.isEmpty()) {
            io.print("No orders found for the specified date.");
        } else {
            io.print("Orders for " + dateToDisplay + ":");
            for (Order order : ordersForDate) {
                displayOrderSummary(order);
            }
        }
    }

        public List<Order> getOrdersForDate(LocalDate date, List<Order> orders) {
            List<Order> ordersForDate = new ArrayList<>();

            for (Order order : orders) {
                if (order.getOrderDate().isEqual(date)) {
                    ordersForDate.add(order);
                }
            }

            return ordersForDate;
        }

    public LocalDate promptFutureOrderDate() {

        LocalDate futureDate;
        do {
            String orderDate = io.readString("Enter the future order date (YYYY-MM-DD): ");
            futureDate = LocalDate.parse(orderDate);
            if (futureDate.isBefore(LocalDate.now().plusDays(1))) {
                io.print("Date must be in the future.");
            }
        } while (futureDate.isBefore(LocalDate.now().plusDays(1)));
        return futureDate;
    }

    public String promptCustomerName() {
        String customerName;
        boolean validName;
        do {
            customerName = io.readString("Enter customer's name: ");
            validName = customerName.matches("^[a-zA-Z0-9.,\\s]+$");
            if (!validName) {
                io.print("Name can only contain numbers, letters, period, and comma.");
            }
        } while (!validName);
        return customerName;
    }

    public Tax promptState(Map<String, Tax> taxes) {
        String state;
        Tax selectedState = null;
        do {
            state = io.readString("Enter state (e.g., NY for New York): ");
            selectedState = taxes.get(state);

            if (!taxes.containsKey(state)) {
                io.print("Sorry, we cannot sell in " + state + ". Please choose a different state.");
            }
        } while (!taxes.containsKey(state));
        return selectedState;
    }

    public Product promptProductType(Map<String, Product>  products) {
        io.print("Available Products:");
        boolean keepGoing = true;
        for (String productType : products.keySet()) {
            BigDecimal costPerSquareFoot = products.get(productType).getCostPerSquareFoot();
            BigDecimal laborCostPerSquareFoot = products.get(productType).getLaborCostPerSquareFoot();

            io.print(String.format("%s - Cost: $%.2f per sq ft, Labor Cost: $%.2f per sq ft",
                    productType, costPerSquareFoot, laborCostPerSquareFoot));
        }

        String selectedProductType;
        do {
            selectedProductType = io.readString("Enter the product type: ");
            if (products.containsKey(selectedProductType)) {
               keepGoing = false;
            }
            else{
                io.print("Invalid product type. Please choose from the available products.");

            }
        } while (keepGoing);

        return products.get(selectedProductType);
    }


    public BigDecimal promptArea() {
        BigDecimal area;
        do {
            area = new BigDecimal(io.readString("Enter area (minimum 100 sq ft): "));
            if (area.compareTo(new BigDecimal(100.0)) < 0) {
                io.print("Area must be at least 100 sq ft.");
            }
        } while (area.compareTo(new BigDecimal(100.0)) < 0);
        return area;
    }

    public void removeOrderPrompt(List<Order> orders){
        io.print("Remove an order: ");
        String orderDate = io.readString("Enter the future order date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(orderDate);
        int orderNumber = io.readInt("Enter the order number: ");

        Order orderToRemove = findOrderByNumberAndDate(orders, orderNumber, orderDate);
        if (orderToRemove != null) {
            displayOrderSummary(orderToRemove);

            char confirmation = io.readChar("Are you sure you want to remove this order? (Y/N): ");
            if (confirmation == 'Y') {
                orders.remove(orderToRemove);
                io.print("Order successfully removed.");
            } else {
                io.print("Order removal canceled.");
            }
        } else {
            io.print("Order not found.");
        }
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