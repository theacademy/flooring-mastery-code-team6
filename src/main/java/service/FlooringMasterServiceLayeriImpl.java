package service;

import dao.FlooringMasterDao;
import dao.FlooringMasterDaoImpl;
import dto.Order;
import dto.Product;
import dto.Tax;
import ui.FlooringMasterView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;

public class FlooringMasterServiceLayeriImpl implements FlooringMasterServiceLayer{

    FlooringMasterDao dao;
    FlooringMasterView view;

    public FlooringMasterServiceLayeriImpl () throws IOException {
        this.dao= new FlooringMasterDaoImpl();
        this.view= new FlooringMasterView();
    }

    public FlooringMasterDao getDao() {
        return dao;
    }

    public FlooringMasterView getView() {
        return view;
    }
    @Override
    public Order addOrder(Order order) throws IOException {
        // read from text file to get next orderNumber

        Order returnOrder = dao.addOrder(order);
        return returnOrder;

    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate date) {
        List<Order> allOrders = dao.getAllOrders();
        Order orderToRemove = findOrderByNumberAndDate(allOrders, orderNumber, date.toString());

        if (orderToRemove != null) {
            dao.removeOrder(orderNumber, date);
            return orderToRemove;
        }

        return null;
    }

    @Override
    public void editOrder(Order newOrder) throws IOException {
        if (newOrder != null) {
            dao.editOrder(newOrder);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }


    @Override
    public int getNewOrderNumber() throws IOException {

        if(dao.getAllOrderNumber().isEmpty()){
            return 1;
        }
        else {
            int highestOrderNumber = max(dao.getAllOrderNumber());
            return highestOrderNumber + 1;
        }
    }


    private Order findOrderByNumberAndDate(List<Order> orders, int orderNumber, String orderDate) {
        for (Order order : orders) {
            if (order.getOrderNumber() == orderNumber && order.getOrderDate().toString().equals(orderDate)) {
                return order;
            }
        }
        return null;
    }

    public Map<String, Product> getAllProducts() throws FileNotFoundException {
        return dao.getAllProducts();
    }

    public Map<String, Tax> getAllTaxes() throws FileNotFoundException {
        return dao.getAllTaxRates();
    }

    @Override
    public void exportAllData() throws IOException {
        dao.writeOrderToFile();
    }


    /**
     * For AddOrder.Everything here was used by AddOrder
     */

    @Override
    public Order getUserNewOrder() throws IOException {
        LocalDate futureDate = validateFutureOrderDate();

        String customerName = validateCustomerName();
        Tax state = validateTaxState();

        Product productType = validateProductType();
        BigDecimal area = validateArea();
        int orderNumber = getNewOrderNumber();
        Order order = new Order(orderNumber, futureDate, customerName, state.getStateAbbreviation(), productType.getProductType(), area);
        order.setTaxRate(state.getTaxRate());
        order.setCostPerSqFoot(productType.getCostPerSquareFoot());
        order.setLaborCostPerSqFoot(productType.getLaborCostPerSquareFoot());
        order.calculateOrderCosts();

        view.displayOrderSummary(order);

        return order;
    }

    @Override
    public BigDecimal validateArea() {
        BigDecimal area;
        do {
            area = new BigDecimal(view.promptAddOrderArea());
            if (area.compareTo(new BigDecimal(100.0)) < 0) {
                view.displayAreaMustBeGreater();
            }
        } while (area.compareTo(new BigDecimal(100.0)) < 0);
        return area;
    }

    @Override
    public Tax validateTaxState() throws FileNotFoundException {
        Tax selectedState = null;
        String state;
        do {
            state = view.promptAddOrderTax();
            selectedState = dao.getAllTaxRates().get(state);

            if (!dao.getAllTaxRates().containsKey(state)) {
                view.displayCannotSellInState(state);
            }
        } while (!dao.getAllTaxRates().containsKey(state));
        return selectedState;
    }

    @Override
    public String validateCustomerName() {
        String customerName;
        boolean validName;
        do {
            customerName = view.promptAddOrderCustomerName();
            validName = customerName.matches("^[a-zA-Z0-9.,\\s]+$");
            if (!validName) {
                view.displayNameMustContain();
            }
        } while (!validName);
        return customerName;
    }

    @Override
    public LocalDate validateFutureOrderDate() {
        LocalDate futureDate = LocalDate.now();
        do {
            String orderDate = view.promptAddOrderOrderDate();
            try {
                futureDate = LocalDate.parse(orderDate);
                if (futureDate.isBefore(LocalDate.now().plusDays(1))) {
                    view.displayDateNotInFuture();
                }
            } catch (Exception e) {
                view.displayDateFormatIncorrect();
            }
        } while (futureDate.isBefore(LocalDate.now().plusDays(1)));
        return futureDate;
    }


    @Override
    public Product validateProductType() throws FileNotFoundException {
        String selectedProductType;
        view.displayAvailableProduct();
        boolean keepGoing = true;
        for (String productType :  dao.getAllProducts().keySet()) {
            BigDecimal costPerSquareFoot =  dao.getAllProducts().get(productType).getCostPerSquareFoot();
            BigDecimal laborCostPerSquareFoot =  dao.getAllProducts().get(productType).getLaborCostPerSquareFoot();

            view.print(productType, costPerSquareFoot, laborCostPerSquareFoot);
        }


        do {
            selectedProductType = view.promptAddOrderProductType();
            if ( dao.getAllProducts().containsKey(selectedProductType)) {
                keepGoing = false;
            }
            else{
                view.displayInvalidProductType();

            }
        } while (keepGoing);

        return  dao.getAllProducts().get(selectedProductType);
    }

    /**
     * End of AddOrder Suite
     */
}
