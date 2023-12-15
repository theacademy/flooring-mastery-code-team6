package controller;

import java.io.IOException;

import java.util.Scanner;

import dto.Order;
import ui.FlooringMasterView;

import service.FlooringMasterServiceLayer;
import ui.UserIO;
import ui.UserIOImpl;


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
                        removeOrder();
                        break;
                    case 4:
                        editOrder();
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

        // display order
        //Order test = view.promptUserAddOrder();
        //test.printOrderInfo();
        // ask user for date
        // service get all orders by a particular date
        // print out all the orders
        // if no orders exist, display an error message
        // return to the main menu
        //view.displayMenu();

        view.displayOrders(service.getAllOrders());

    }

    private void addOrder() throws IOException {

        /*
        Show a summary of the order once the calculations are completed
        and prompt the user as to whether they want to place the order (Y/N).
        If yes, the data will be added to in-memory storage.
        If no, simply return to the main menu.


        The system should generate an order number for the user based on the
        next available order # (so if there are two orders and the max order number is 4,
        the next order number should be 5).
         */
        /*

         */
        Order retrieved = service.promptUserAddOrder();
        service.addOrder(retrieved);
        view.displayAddSuccessBanner();
        view.pressEnterToGoBack();
    }

    private void removeOrder() {
        System.out.println("remove orders");
        //service.removeOrder();

    }
    private void editOrder() {
        System.out.println("edit orders");
        //service.editOrder();

    }

    private void unknownCommand() {
        System.out.println("unknown command");


    }

    private void exportAllData() {

        System.out.println("export all data");

    }




}
