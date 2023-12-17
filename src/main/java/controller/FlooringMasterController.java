package controller;

import dao.FlooringMasterDao;
import dao.FlooringMasterDaoImpl;
import dto.Order;
import dto.Product;
import dto.Tax;
import service.FlooringMasterServiceLayer;
import service.FlooringMasterServiceLayeriImpl;
import ui.FlooringMasterView;
import ui.UserIO;
import ui.UserIOImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class FlooringMasterController {
    private FlooringMasterView view;
    private FlooringMasterServiceLayer service;

    public FlooringMasterController() throws IOException {
        this.view = new FlooringMasterView();
        this.service = new FlooringMasterServiceLayeriImpl();
    }

    //Menu Selection
    public void run() throws IOException {
        boolean keepGoing = true;

        while (keepGoing) {
            switch (getMenuSelection()) {
                case 1:
                    displayAllOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportAllData();
                    break;
                case 6:
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

}
