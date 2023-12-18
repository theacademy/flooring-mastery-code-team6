package service;

import dao.FlooringMasterDao;
import dao.FlooringMasterDaoImpl;
import dto.Order;

import static java.util.Collections.max;
import static org.junit.jupiter.api.Assertions.*;

import dto.Product;
import dto.Tax;
import ui.FlooringMasterView;

import ui.UserIO;
import ui.UserIOImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class FlooringMasterServiceLayeriImplTest {
    private FlooringMasterServiceLayerImpl service;


    private UserIO io;
    private FlooringMasterView view;

    private FlooringMasterDao dao;
    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        service = new FlooringMasterServiceLayerImpl(new FlooringMasterDaoImpl(), new FlooringMasterView(new UserIOImpl()));

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void addOrderAndRemoveOrderTest() throws IOException {
        // Set up test order
        Order testOrder = new Order(1, LocalDate.now().plusDays(10),"Hoda",
                "TX", "Laminate", new BigDecimal(120));


        // Call the method to add an order
        Order resultOrder = service.addOrder(testOrder);

        // Verify that dao.addOrder was called with the correct parameters
        assertEquals(testOrder, resultOrder, "Returned order should match the test order");

        // Verify that getAllOrders has order
        assertTrue(service.getDao().getAllOrders().contains(testOrder));

        assertTrue(service.getDao().getDateOrder().containsKey(testOrder.getOrderDate().toString()));

        // remove the order from dao
        service.getDao().removeOrder(testOrder.getOrderNumber(),testOrder.getOrderDate());


        // delete the order and verify it
        assertFalse(service.getDao().getAllOrders().contains(testOrder));


    }

    @org.junit.jupiter.api.Test
    void editOrder() throws IOException {
        // Set up test order
        Order testOrder = new Order(1, LocalDate.now().plusDays(10),"Hoda",
                "TX", "Laminate", new BigDecimal(120));

        // Call the method to add an order
        Order resultOrder = service.addOrder(testOrder);

        // Verify that dao.addOrder was called with the correct parameters
        assertEquals(testOrder, resultOrder, "Returned order should match the test order");

        // Verify that getAllOrders has order
        assertTrue(service.getDao().getAllOrders().contains(testOrder));

        assertTrue(service.getDao().getDateOrder().containsKey(testOrder.getOrderDate().toString()));

        // remove the order from dao
        service.getDao().removeOrder(testOrder.getOrderNumber(),testOrder.getOrderDate());

        // delete the order and verify it
        assertFalse(service.getDao().getAllOrders().contains(testOrder));
    }

    @org.junit.jupiter.api.Test
    void validateAreaTest() throws IOException {


        //Validate on correct data
        BigDecimal testArea = new BigDecimal("101");
        boolean result = service.validateArea(testArea);
        assertEquals(result,true,"validateArea on correct area should return true");

        //Validate on incorrect data
        testArea = new BigDecimal("99");
        result = service.validateArea(testArea);
        assertEquals(result,false,"validateArea on incorrect area should return false");
    }

    @org.junit.jupiter.api.Test
    void validateNameTest() throws IOException {

        //Validate on correct data
        String testName = "Acme5, Inc.";
        boolean result = service.validateCustomerName(testName);
        assertEquals(result,true,"validateCustomerName on correct data should return true");


        //Validate on incorrect data
        testName = "Acme5, Inc.%";
        result = service.validateCustomerName(testName);
        assertEquals(result,false,"validateCustomerName on incorrect data should return false");
    }

    @org.junit.jupiter.api.Test
    void validateStateTest() throws IOException {

        Tax myTax = new Tax("TX","Texas",new BigDecimal(4.02));
        Map<String,Tax> myMap = new HashMap<>();
        myMap.put("TX", myTax);
        //Validate on correct data
        String testState = "TX";
        boolean result = service.validateTaxState(testState,myMap);
        assertEquals(result,true,"validateTaxState on correct data should return true");


        //Validate on incorrect data
        testState = "NY";
        result = service.validateTaxState(testState,myMap);
        assertEquals(result,false,"validateTaxState on incorrect data should return false");
    }

    @org.junit.jupiter.api.Test
    void validateDateTest() throws IOException {

        //Validate on correct data
        String testDate = LocalDate.now().plusDays(5).toString();
        boolean result = service.validateFutureOrderDate(testDate);
        assertEquals(result,true,"validateCustomerName on correct data should return true");


        //Validate on incorrect data
        testDate = LocalDate.now().minusDays(5).toString();
        result = service.validateCustomerName(testDate);
        assertEquals(result,false,"validateCustomerName on incorrect data should return false");
    }

    @org.junit.jupiter.api.Test
    void validateProductTypeTest(){

        Product myProduct = new Product("Tile",new BigDecimal("5.00"), new BigDecimal("15.00"));
        Map<String,Product> myMap = new HashMap<>();
        myMap.put(myProduct.getProductType(), myProduct);
        //Validate on correct data
        String testProduct = "Tile";
        boolean result = service.validateProductType(testProduct,myMap);
        assertEquals(result,true,"validateTaxState on correct data should return true");


        //Validate on incorrect data
        testProduct = "tile";
        result = service.validateProductType(testProduct,myMap);
        assertEquals(result,false,"validateTaxState on incorrect data should return false");
    }



    @org.junit.jupiter.api.Test
    void getNewOrderNumberTest() throws IOException {

        // Set up test order
        Order testOrder = new Order(1, LocalDate.now().plusDays(10),"Hoda",
                "TX", "Laminate", new BigDecimal(120));
        Order testOrder2 = new Order(2, LocalDate.now().plusDays(10),"Luan",
                "TX", "Laminate", new BigDecimal(120));
        Order testOrder3 = new Order(3, LocalDate.now().plusDays(10),"Ivy",
                "TX", "Laminate", new BigDecimal(120));
        service.addOrder(testOrder);
        service.addOrder(testOrder2);
        service.addOrder(testOrder3);
        service.removeOrder(3,testOrder3.getOrderDate());

        assertEquals(max(service.getDao().getAllOrderNumber()) + 1,service.getNewOrderNumber(), "service is creating wrong new order number");

    }

}