package controller;

import service.FlooringMasterServiceLayer;

public class FlooringMasterController {



    private FlooringMasterView view;
    private FlooringMasterServiceLayer service;
    //private UserIO io = new UserIOConsoleImpl();

    public FlooringMasterController( FlooringMasterServiceLayer service, FlooringMasterView view) {
        this.service = service;
        this.view = view;
    }

    //Menu Selection
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayAllOrder();
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
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (Exception e) {
            System.out.println("Error");
            //Create new exception here for persistence,
            //view should also be used to display messages.
        }

        private void addOrder(){
            Order retrieved = view.promptUserAddOrder();
            service.addOrder(retrieved);
            view.displayAddSuccessBanner();
            view.pressEnterToGoBack();
        }








}
