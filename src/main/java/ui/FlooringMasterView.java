package ui;

import dto.Order;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FlooringMasterView {

    private final UserIO io;

    // @Autowired
    public FlooringMasterView(UserIO io){
        this.io = io;
    }



    public int displayMenu(){
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return Integer.parseInt(io.readString("Please select from the choices (1-6): "));
    }

    public Order promptUserAddOrder(){
        io.print("");
        io.print("Enter your order details below: ");


        //Validation for order date. Date must be in the future
        String orderDate = io.readString("Enter the future order date (YYYY-MM-DD): ");
        LocalDate futureDate = LocalDate.parse(orderDate);
        LocalDate now = LocalDate.now();
        now = now.plusDays(1);
        while (futureDate.isBefore(now)) {
            orderDate = io.readString("Date must be in the future (YYYY-MM-DD): ");
            futureDate = LocalDate.parse(orderDate);
            now = LocalDate.now();
        }


        String customerName = io.readString("Enter customer's name: ");

        boolean res;
        do {
            // Creating regex pattern by
            // creating object of Pattern class
            Pattern p = Pattern.compile(
                    "^[a-zA-Z0-9.,\\s]+$", Pattern.CASE_INSENSITIVE);

            // Creating matcher for above pattern on our string
            Matcher m = p.matcher(customerName);

            // Now finding the matches for which
            // let us set a boolean flag and
            // imposing find() method
            res = m.find();

            if (!res){
                customerName = io.readString("Name can only contain numbers, letters, period, and comma");
            }

        }
        while (!res);

        String state = io.readString("Enter state (e.g., NY for New York): ");
        String productType = io.readString("Enter product type: ");
        double area = io.readDouble("Enter area (minimum 100 sq ft): ");
        BigDecimal convertedArea = new BigDecimal(area);


        while (area <= 100) {
            io.print("Area must be at least 100 sq ft.");
            area = io.readDouble("Enter Area (minimum 100 sq ft): ");
        }

        Order order = new Order(LocalDate.parse(orderDate), customerName, state, productType, convertedArea);
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
