package controller;

import dto.Order;
import dto.Product;
import dto.Tax;
import enums.MenuSelectionType;
import service.FlooringMasterServiceLayer;
import service.FlooringMasterServiceLayeriImpl;
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
                default:
                    unknownCommand();
            }

        }
    }

    private int getMenuSelection() {
        view.displayMenu();
        return view.getUserSelection();
    }

    private void displayAllOrders() {
        view.displayOrders(service.getAllOrders());
    }

    private void addOrder() throws IOException {

        Order order = service.getUserNewOrder();

        view.displayOrderSummary(order);

        char confirmation = view.promptYN();
        if (confirmation == 'N') {
            view.displayOrderCanceled();
        }
        else{
            service.addOrder(order);
            view.displayAddSuccessBanner();
            view.pressEnterToGoBack();
            view.displayOrderPlacedSuccessfully(order.getOrderNumber());
        }


    }

    private void removeOrder() {
        List<Order> orders = service.getAllOrders();
        Order orderToBeRemoved = view.removeOrderPrompt(orders);
        if(orderToBeRemoved != null) {
            Order removedOrder = service.removeOrder(orderToBeRemoved.getOrderNumber(), orderToBeRemoved.getOrderDate());
            if (removedOrder != null && removedOrder.equals(orderToBeRemoved)) {
                view.removeOrderResultPrompt(true);
            }
            view.removeOrderResultPrompt(false);
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

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exportAllData() throws IOException {
        service.exportAllData();
    }
    
    private MenuSelectionType chooseSelectionType(int i) {
        MenuSelectionType[] selections = MenuSelectionType.values();
        return selections[i-1];
    }

}
