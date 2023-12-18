package service;

import dao.FlooringMasterDao;
import dao.FlooringMasterDaoImpl;
import dto.Order;

import static org.junit.jupiter.api.Assertions.*;

import ui.FlooringMasterView;

import ui.UserIO;
import ui.UserIOImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

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

}