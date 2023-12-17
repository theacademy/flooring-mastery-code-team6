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
        this.view = new FlooringMasterView(new UserIOImpl());
        FlooringMasterDao dao = new FlooringMasterDaoImpl();
        this.service = new FlooringMasterServiceLayeriImpl(dao, view);
    }

    //Menu Selection
    public void run() {
        boolean keepGoing = true;
        int menuSelection;
        try {
            while (keepGoing) {
                menuSelection = getMenuSelection();

                switch (menuSelection) {
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
            view.exitMessage();
        } catch (Exception e) {
            view.print(e.getMessage());
            //Create new exception here for persistence,
            //view should also be used to display messages.
        }
    }

    private int getMenuSelection() {
        return view.displayMenu();
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
