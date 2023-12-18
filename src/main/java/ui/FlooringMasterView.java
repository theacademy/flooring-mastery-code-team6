package ui;

import dto.Order;
import dto.Product;
import dto.Tax;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public void displayMenu() {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");
    }

    public int getUserSelection() {
        return io.readInt("Please select from the choices (1-6): ");
    }


    public void displayAddSuccessBanner() {
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
        io.print("State Tax Rate: $" + order.getTaxRate());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea());
        io.print("Cost per Square Ft: $" + order.getCostPerSqFoot());
        io.print("Labor Cost Per Square Foot: $" + order.getLaborCostPerSqFoot());
        io.print("Material Cost: $" + order.getMaterialCost());
        io.print("Labor Cost: $" + order.getLaborCost());
        io.print("Tax: $" + order.getTax());
        io.print("Total: $" + order.getTotal());
    }

    public void displayOrders(List<Order> orders) {
        if (orders == null) {
            io.print("There is no orders at all!");
            return;
        }
        boolean keepGoing;

        do {
            keepGoing = false;
            io.print("");
            io.print("Display Orders for a Specific Date: ");

            try {

                LocalDate dateToDisplay = LocalDate.parse(io.readString("Enter the date (YYYY-MM-DD): "));


                List<Order> ordersForDate = getOrdersForDate(dateToDisplay, orders);

                if (ordersForDate.isEmpty()) {
                    io.print("No orders found for the specified date.");
                } else {
                    io.print("Orders for " + dateToDisplay + ":");
                    for (Order order : ordersForDate) {
                        io.print("Order Number: " + order.getOrderNumber());
                        displayOrderSummary(order);
                        io.print("============================");
                    }
                }
            } catch (Exception e) {
                io.print("Date format is not correct!");

                keepGoing = true;
            }
        } while (keepGoing);
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

        LocalDate futureDate = LocalDate.now();
        do {
            String orderDate = io.readString("Enter the future order date (YYYY-MM-DD): ");
            try {
                futureDate = LocalDate.parse(orderDate);
                if (futureDate.isBefore(LocalDate.now().plusDays(1))) {
                    io.print("Date must be in the future.");
                }
            } catch (Exception e) {
                io.print("Date format is not correct!");
            }
        } while (futureDate.isBefore(LocalDate.now().plusDays(1)));
        return futureDate;
    }

    public LocalDate promptOrderDate(String prompt) {

        boolean keepGoint = true;
        LocalDate localDate = LocalDate.now();
        do {
            String orderDate = io.readString(prompt);
            try {
                localDate = LocalDate.parse(orderDate);
                keepGoint = false;
            } catch (Exception e) {
                io.print("Date format is not correct!");
            }
        } while (keepGoint);
        return localDate;
    }

    public String promptCustomerName(String prompt) {
        String customerName;
        boolean validName;
        do {
            customerName = io.readString(prompt);
            validName = customerName.matches("^[a-zA-Z0-9.,\\s]+$");
            if (!validName) {
                io.print("Name can only contain numbers, letters, period, and comma.");
            }
        } while (!validName);
        return customerName;
    }


    public Tax promptTax(Map<String, Tax> taxes, String prompt) {
        String state;
        Tax selectedState = null;
        do {
            state = io.readString(prompt);
            selectedState = taxes.get(state);

            if (!taxes.containsKey(state)) {
                io.print("Sorry, we cannot sell in " + state + ". Please choose a different state.");
            }
        } while (!taxes.containsKey(state));
        return selectedState;
    }

    public String promptState(Map<String, Tax> taxes, String prompt) {
        String state;
        Tax selectedState = null;
        do {
            state = io.readString(prompt);
            selectedState = taxes.get(state);

            if (!taxes.containsKey(state)) {
                io.print("Sorry, we cannot sell in " + state + ". Please choose a different state.");
            }
        } while (!taxes.containsKey(state));
        return state;
    }

    public Product promptProductType(Map<String, Product> products, String prompt) {
        io.print(prompt);

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
            } else {
                io.print("Invalid product type. Please choose from the available products.");

            }
        } while (keepGoing);

        return products.get(selectedProductType);
    }

    public BigDecimal promptArea(String prompt) {
        String areaStr;
        BigDecimal area = null;
        do {
            areaStr = io.readString(prompt);
            if (isBigDecimal(areaStr)) {
                area = new BigDecimal(areaStr);
                if (area.compareTo(new BigDecimal(100.0)) < 0) {
                    io.print("Area must be at least 100 sq ft.");
                }
            }
        } while (area == null || area.compareTo(new BigDecimal(100.0)) < 0) ;
        return area;
    }

    public boolean isBigDecimal(String area) {
        try {
            BigDecimal areaBD = new BigDecimal(area);
            return true;
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public int promptOrderNumber(String prompt) {
        int i = -999;
        do {
            try {
                i =io.readIntFromString(prompt);
            } catch (Exception e) {
                io.print("Not a valid order number");
            }
        } while(i == -999);
        return i;
    }


    public Order promptDateAndOrderNumberForOrder(List<Order> orders) {
        LocalDate orderDate = promptOrderDate("Enter the order date (YYYY-MM-DD): ");
        int orderNumber = promptOrderNumber("Enter the order number: ");
        return findOrderByNumberAndDate(orders, orderNumber, orderDate.toString());
    }

    public String editCustomerName(String oldName) {

        String prompt = "Enter customer name (" + oldName + "): ";
        String newName = io.readString(prompt);
        if (newName.isEmpty() || newName.isBlank()) {
            return null;
        } else if (newName.matches("^[a-zA-Z0-9.,\\s]+$")) {
            return newName;
        } else {
            io.print("Name can only contain numbers, letters, period, and comma.");
            return promptCustomerName(prompt);
        }
    }

    public String editState(String oldState, Map<String, Tax> taxes) {

        String prompt = "Enter state (" + oldState + "): ";
        String newState = io.readString(prompt);
        if (newState.isEmpty() || newState.isBlank()) {
            return null;
        } else if (taxes.containsKey(newState)) {
            return newState;
        } else {
            io.print("Sorry, we cannot sell in " + newState + ". Please choose a different state.");
            return promptState(taxes, prompt);
        }
    }

    public Product editProduct(String oldProduct, Map<String, Product> products) {

        String prompt = "Enter product type (" + oldProduct + "): ";
        String newProduct = io.readString(prompt);
        if (newProduct.isEmpty() || newProduct.isBlank()) {
            return null;
        } else if (products.containsKey(newProduct)) {
            return products.get(newProduct);
        } else {
            io.print("Invalid product type. Please choose from the available products.");
            return promptProductType(products, prompt);
        }
    }

    public BigDecimal editArea(BigDecimal oldArea) {

        String prompt = "Enter area (" + oldArea + "): ";
        String newArea = io.readString(prompt);
        if(newArea.isEmpty() || newArea.isBlank()) {
            return null;
        } else if (isBigDecimal(newArea) && new BigDecimal(newArea).compareTo(new BigDecimal(100.0)) >= 0) {
            return new BigDecimal(newArea);
        } else {
            io.print("Area must be at least 100 sq ft.");
            return promptArea(prompt);
        }
    }


    public Order editOrderPrompt(List<Order> orders, Map<String, Product> products, Map<String, Tax> taxes) {

        // retrieve the order by ask the user for the order date and order number
        Order orderToEdit = promptDateAndOrderNumberForOrder(orders);

        // If the order exists for that date,
        if (orderToEdit != null) {

            // ask the user for each piece of order data but display the existing data
            // any changes will change that particular order
            // pressing enter, will leave that data unchanged
            io.print("Edit your new data. Press Enter only if you wish to leave that particular field unchanged.");

            String newCustomerName = editCustomerName(orderToEdit.getCustomerName());
            String newState = editState(orderToEdit.getState(), taxes);
            Product newProduct = editProduct(orderToEdit.getProductType(), products);
            BigDecimal newArea = editArea(orderToEdit.getArea());

            // if there are no changes, return null
            if (newCustomerName == null && newState == null && newProduct == null && newArea == null) {
                return null;
            }

            Order newOrder = new Order();
            newOrder.setOrderNumber(orderToEdit.getOrderNumber());
            newOrder.setOrderDate(orderToEdit.getOrderDate());
            newOrder.setCustomerName(newCustomerName != null ? newCustomerName : orderToEdit.getCustomerName());
            newOrder.setState(newState != null ? newState : orderToEdit.getState());
            newOrder.setProductType(newProduct != null ? newProduct.getProductType() : orderToEdit.getProductType());
            newOrder.setArea(newArea != null ? newArea : orderToEdit.getArea());

            //Order newOrder = new Order(orderToEdit.getOrderNumber(), orderToEdit.getOrderDate(), newCustomerName, newState, product.getProductType(), newArea);

            // perform calculations
            newOrder.setTaxRate(taxes.get(newOrder.getState()).getTaxRate());
            newOrder.setCostPerSqFoot(products.get(newOrder.getProductType()).getCostPerSquareFoot());
            newOrder.setLaborCostPerSqFoot(products.get(newOrder.getProductType()).getLaborCostPerSquareFoot());
            newOrder.calculateOrderCosts();

            // Display a summary of the updated order
            displayOrderSummary(newOrder);

            char confirmation = io.readChar("Do you want to save these changes? (Y/N): ");
            if (confirmation == 'Y') {
                // Save the changes (you may implement this part based on your data storage mechanism)
                io.print("Changes saved successfully.");
                return newOrder;
            } else {
                io.print("Changes discarded.");
            }
        } else {
            io.print("No orders found!");
        }

        return null;

    }

    public Order removeOrderPrompt(List<Order> orders) {
        io.print("Remove an order: ");
        LocalDate orderDate = promptOrderDate("Enter the order date (YYYY-MM-DD) to be removed: ");
        int orderNumber = promptOrderNumber("Enter the order number: ");
        Order orderToRemove = findOrderByNumberAndDate(orders, orderNumber, orderDate.toString());
        if (orderToRemove != null) {
            displayOrderSummary(orderToRemove);

            char confirmation = io.readChar("Are you sure you want to remove this order? (Y/N): ");
            if (confirmation == 'Y') {
                //orders.remove(orderToRemove);
                io.print("Removing order...");
                return orderToRemove;
            } else {
                io.print("Order removal canceled.");
            }
        } else {
            io.print("Order not found.");
        }
        return null;
    }


    public void removeOrderResultPrompt(boolean removed) {
        if (removed) io.print("Order successfully removed.");
        else io.print("Error: Order removal was unsuccessful.");
    }

    public Order findOrderByNumberAndDate(List<Order> orders, int orderNumber, String orderDate) {
        for (Order order : orders) {
            if (order.getOrderNumber() == orderNumber && order.getOrderDate().toString().equals(orderDate)) {
                return order;
            }
        }
        return null;
    }


    public void displayUnknownCommandBanner() {
        io.print("Unknown Command. Please try again.");
    }

    public void displayErrorMsg(String errorMsg) {
        io.print("====Error====");
        io.print(errorMsg);
    }

    public void exitMessage() {
        io.print("Thank you for visiting. Goodbye.");
    }

    /**
     * For AddOrder. Everything here was used by AddOrder
     */
    public String promptAddOrderOrderDate() {


        return io.readString("Enter the future order date (YYYY-MM-DD): ");


    }

    public String promptAddOrderCustomerName() {
        return io.readString("Enter customer's name: ");
    }


    public String promptAddOrderTax() {
        return io.readString("Enter state (e.g., NY for New York): ");
    }

    public String promptAddOrdertate(Map<String, Tax> taxes, String prompt) {
        String state;
        Tax selectedState = null;
        do {
            state = io.readString(prompt);
            selectedState = taxes.get(state);

            if (!taxes.containsKey(state)) {
                io.print("Sorry, we cannot sell in " + state + ". Please choose a different state.");
            }
        } while (!taxes.containsKey(state));
        return state;
    }

    public String promptAddOrderProductType() {
        return io.readString("Enter the product type: ");
    }


    public String promptAddOrderArea() {
        return io.readString("Enter area (minimum 100 sq ft): ");
    }


    public int promptAddOrderOrderNumber() {
        return io.readInt("Enter the order number: ");
    }

    public void displayAreaMustBeGreater() {
        io.print("Area must be greater than 100 sq. ft");
    }

    public void displayCannotSellInState(String state) {
        io.print("Order is not available to be placed in " + state);
    }

    public void displayNameMustContain() {
        io.print("Name must only contain numbers, letters, periods, or comma.");
    }

    public void displayDateNotInFuture() {
        io.print("Date entered is not in the future (YYYY-MM-DD)");
    }

    public void displayDateFormatIncorrect() {
        io.print("Date format is incorrect, must be in (YYYY-MM-DD) ");
    }

    public void displayAvailableProduct() {
        io.print("Available Products:");
    }

    public void print(String productType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot) {
        String productPrintOut = (String.format("%s - Cost: $%.2f per sq ft, Labor Cost: $%.2f per sq ft",
                productType, costPerSquareFoot, laborCostPerSquareFoot));
        io.print(productPrintOut);
    }

    public void displayInvalidProductType() {
        io.print("Invalid Product type");
    }

    public char promptYN() {
        return io.readChar("Confirm by pressing Y/N");
    }

    public void displayOrderCanceled() {
        io.print("Order cancelled");
    }

    public void displayOrderPlacedSuccessfully(int orderNumber) {
        io.print("Order #" + orderNumber + " placed successfully:");
    }
    /**
     * End of AddOrder Suite
     */
}