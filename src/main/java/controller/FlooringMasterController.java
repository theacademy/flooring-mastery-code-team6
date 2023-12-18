package controller;

import dto.Order;
import dto.Product;
import dto.Tax;
import enums.MenuSelectionType;
import service.FlooringMasterServiceLayer;
import ui.FlooringMasterView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FlooringMasterController {
    private FlooringMasterView view;
    private FlooringMasterServiceLayer service;

    public FlooringMasterController(FlooringMasterView view, FlooringMasterServiceLayer service) throws IOException {
        this.view = view;
        this.service = service;
    }

    //Menu Selection
    public void run() throws IOException {
        boolean keepGoing = true;
        while (keepGoing) {
            switch (chooseSelectionType(getMenuSelection())) {
                case DISPLAY_ORDERS:
                    displayAllOrders();
                    break;
                case ADD_ORDERS:
                    addOrder();
                    break;
                case EDIT_ORDERS:
                    editOrder();
                    break;
                case REMOVE_ORDERS:
                    removeOrder();
                    break;
                case EXPORT_ALL_DATA:
                    exportAllData();
                    break;
                case QUIT:
                    keepGoing = false;
                    break;
                case UNKNOWNCOMMANDS:
                    unknownCommand();
                    break;

            }
        }
    }

    // display the menu selection
    private int getMenuSelection() {
        view.displayMenu();
        return view.getUserSelection();
    }

    // display all orders
    private void displayAllOrders() {
        view.displayOrders(service.getAllOrders());
    }

    // add an order
    private void addOrder() throws IOException {

        Order order = service.getUserNewOrder(); // create a new order
        view.displayOrderSummary(order);        // display order
        char confirmation = view.promptYN();    // confirm order
        if (confirmation == 'N') {              // N - cancel order
            view.displayOrderCanceled();
        }
        else{
            service.addOrder(order);            // otherwise add the order
            view.displayAddSuccessBanner();     // display success banner
            view.pressEnterToGoBack();          // tell user to press enter to go back
            view.displayOrderPlacedSuccessfully(order.getOrderNumber()); //show placed order
        }
    }

    // remove order
    private void removeOrder() {
        List<Order> orders = service.getAllOrders();    // get all orders
        Order orderToBeRemoved = view.removeOrderPrompt(orders);    // get order to be removed
        if(orderToBeRemoved != null) {  // if order to be removed exist
            // remove order
            Order removedOrder = service.removeOrder(orderToBeRemoved.getOrderNumber(),
                    orderToBeRemoved.getOrderDate());
            if (removedOrder != null && removedOrder.equals(orderToBeRemoved)) {
                view.removeOrderResultPrompt(true); // successful removal
            } else {
                view.removeOrderResultPrompt(false); // unsuccessful removal
            }
        }
    }

    private void editOrder() throws IOException, FileNotFoundException {
        List<Order> orders = service.getAllOrders();                // get list of orders
        Map<String, Product> products = service.getAllProducts();   // get list of products
        Map<String, Tax> taxes = service.getAllTaxes();                     // get all taxes
        Order editedOrder = view.editOrderPrompt(orders, products, taxes);  // get edit order
        if (editedOrder != null) {
            service.editOrder(editedOrder);                         // edit to new order if new order exists
        }
    }

    // prompt unknown command
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    // export all data
    private void exportAllData() throws IOException {
        service.exportAllData();
    }

    // make selection based on user input
    private MenuSelectionType chooseSelectionType(int i) {
        MenuSelectionType[] selections = MenuSelectionType.values();
        return selections[i-1];
    }

}
