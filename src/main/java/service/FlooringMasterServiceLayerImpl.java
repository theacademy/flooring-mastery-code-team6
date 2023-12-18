package service;

import dao.FlooringMasterDao;
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

public class FlooringMasterServiceLayerImpl implements FlooringMasterServiceLayer{

    FlooringMasterDao dao;
    FlooringMasterView view;

    public FlooringMasterServiceLayerImpl(FlooringMasterDao dao, FlooringMasterView view) throws IOException {
        this.dao= dao;
        this.view= view;
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
        LocalDate futureDate = getFutureDate();

        String customerName = getCustomerName();
        Tax state = getTaxState();

        Product productType = getProductType();
        BigDecimal area = getArea();
        int orderNumber = getNewOrderNumber();
        Order order = new Order(orderNumber, futureDate, customerName, state.getStateAbbreviation(), productType.getProductType(), area);
        order.setTaxRate(state.getTaxRate());
        order.setCostPerSqFoot(productType.getCostPerSquareFoot());
        order.setLaborCostPerSqFoot(productType.getLaborCostPerSquareFoot());
        order.calculateOrderCosts();

        return order;
    }

    @Override
    public BigDecimal getArea() {
        String areaStr;
        BigDecimal area = null;
        do {
            areaStr = view.promptAddOrderArea();
            if (view.isBigDecimal(areaStr)) {
                area = new BigDecimal(areaStr);
            }
        } while (area == null || !validateArea(area)) ;
        return area;
    }

    @Override
    public boolean validateArea(BigDecimal area) {
        if (area.compareTo(new BigDecimal(100.0)) >= 0) {
            return true;
        } else {
            view.displayAreaMustBeGreater();
            return false;
        }
    }

    @Override
    public Tax getTaxState() throws FileNotFoundException {
        Map<String, Tax> taxes = dao.getAllTaxRates();
        Tax selectedState = null;
        String state;
        do {
            state = view.promptAddOrderTax();
            selectedState = taxes.get(state);
        } while (!validateTaxState(state, taxes));
        return selectedState;
    }

    @Override
    public boolean validateTaxState(String state, Map<String, Tax> taxes) throws FileNotFoundException {
        if (taxes.containsKey(state)) {
            return true;
        } else {
            view.displayCannotSellInState(state);
        }
        return false;
    }

    @Override
    public String getCustomerName() {
        String customerName;
        do {
            customerName = view.promptAddOrderCustomerName();
        } while (!validateCustomerName(customerName));
        return customerName;
    }

    @Override
    public boolean validateCustomerName(String customerName) {
        if(customerName.matches("^[a-zA-Z0-9.,\\s]+$")) {
            return true;
        } else {
            view.displayNameMustContain();
        }
        return false;
    }

    @Override
    public LocalDate getFutureDate() {
        LocalDate futureDate = LocalDate.now();
        String orderDate;
        do {
            orderDate = view.promptAddOrderOrderDate();
        } while (!validateFutureOrderDate(orderDate));
        return LocalDate.parse(orderDate);
    }

    @Override
    public boolean validateFutureOrderDate(String orderDate) {
        LocalDate futureDate;
        try {
            futureDate = LocalDate.parse(orderDate);
            if (!futureDate.isBefore(LocalDate.now().plusDays(1))) {
                return true;
            } else {
                view.displayDateNotInFuture();
            }
        } catch (Exception e) {
            view.displayDateFormatIncorrect();
        }
        return false;
    }


    @Override
    public Product getProductType() throws FileNotFoundException {

        String selectedProductType;
        Map<String, Product>  products = dao.getAllProducts();
        view.displayAvailableProduct();

        for (String productType : products.keySet()) {
            BigDecimal costPerSquareFoot = products.get(productType).getCostPerSquareFoot();
            BigDecimal laborCostPerSquareFoot = products.get(productType).getLaborCostPerSquareFoot();

            view.print(productType, costPerSquareFoot, laborCostPerSquareFoot);
        }

        do {
            selectedProductType = view.promptAddOrderProductType();
        } while (!validateProductType(selectedProductType, products));

        return products.get(selectedProductType);
    }

    @Override
    public boolean validateProductType(String selectedProductType, Map<String,Product> products) {

        if (products.containsKey(selectedProductType)) {
            return true;
        } else{
            view.displayInvalidProductType();
        }
        return false;
    }

    /**
     * End of AddOrder Suite
     */
}
