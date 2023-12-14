package ui;

import dto.Order;


import java.time.LocalDate;


public class FlooringMasterView {

    private final UserIO io;

    // @Autowired
    public FlooringMasterView(UserIO io){
        this.io = io;
    }


    public Order promptUserAddOrder(){
        io.print("");
        io.print("Enter your order details below: ");
        String orderDate = io.readString("Enter the order date (MM-DD-YYYY): ");
        String customerName = io.readString("Enter customer's name: ");
        String state = io.readString("Enter state (e.g., NY for New York): ");
        String productType = io.readString("Enter product type: ");
        double area = io.readDouble("Enter area (minimum 100 sq ft): ");
        while(area < 100){
            io.print("Area must be at least 100 sq ft.");
            area = io.readDouble("Enter Area (minimum 100 sq ft): ");
        }

        Order order = new Order(LocalDate.parse(orderDate), customerName, state, productType, area);

        displayOrderSummary(order);

        char confirmation = io.readChar("Do you want to place this order? (Y/N): ");
        if (confirmation == 'N') {
            io.print("Order canceled.");
            return null;
        }

        return order;
    }

    public void displayAddSuccessBanner(){
        io.print("Order has been placed!");
    }

    public void pressEnterToGoBack(){
        io.readString("Press Enter to continue. ");
    }

    private void displayOrderSummary(Order order) {
        System.out.println("\nOrder Summary: ");
        System.out.println("Order Date: " + order.getOrderDate());
        System.out.println("Customer Name: " + order.getCustomerName());
        System.out.println("State: " + order.getState());
        System.out.println("Product Type: " + order.getProductType());
        System.out.println("Area: " + order.getArea());
    }
}
