package controller;

import dto.Order;
import service.FlooringMasterServiceLayer;
import ui.FlooringMasterView;

import java.io.IOException;
import java.util.List;


public class FlooringMasterController {
    private FlooringMasterView view;
    private FlooringMasterServiceLayer service;

    public FlooringMasterController(FlooringMasterServiceLayer service, FlooringMasterView view) {

        this.service = service;
        this.view = view;

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
            //exitMessage();
        } catch (Exception e) {
            System.out.println("Error");
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

        Order retrieved = service.promptUserAddOrder();
        service.addOrder(retrieved);
        view.displayAddSuccessBanner();
        view.pressEnterToGoBack();
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

    private void editOrder() throws IOException {
        service.editOrder();

    }

    private void unknownCommand() {
        System.out.println("unknown command");


    }

    private void exportAllData() {

        System.out.println("export all data");

    }

}
