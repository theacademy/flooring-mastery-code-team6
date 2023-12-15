package service;

import dao.FlooringMasterDao;
import dao.FlooringMasterDaoImpl;
import dto.Order;

import static org.junit.jupiter.api.Assertions.*;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import ui.FlooringMasterView;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
class FlooringMasterServiceLayeriImplTest {
    private FlooringMasterDao dao;
    private FlooringMasterView view;
    private FlooringMasterServiceLayeriImpl service;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // defining mock objects
        dao = new FlooringMasterDaoImpl();
        view = mock(FlooringMasterView.class);
        service = new FlooringMasterServiceLayeriImpl(dao, view);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        dao = null;
        view = null;
        service = null;
    }

    @org.junit.jupiter.api.Test
    void addOrder() throws IOException {
        // Set up test order
        Order testOrder = new Order(1, LocalDate.now().plusDays(10),"Hoda",
                "TX", "Laminate", new BigDecimal(120));


        // Call the method to add an order
        Order resultOrder = service.addOrder(testOrder);

        // Verify that dao.addOrder was called with the correct parameters
        verify(dao).addOrder(eq(testOrder));
        verify(dao.getAllOrders().contains(testOrder));

        // Verify that the returned order matches the expected test order
        assertEquals(testOrder, resultOrder, "Returned order should match the test order");

    }

    @org.junit.jupiter.api.Test
    void removeOrder() {
    }

    @org.junit.jupiter.api.Test
    void editOrder() {
    }

    @org.junit.jupiter.api.Test
    void calculateMaterialCost() {
    }

    @org.junit.jupiter.api.Test
    void calculateLaborCost() {
    }

    @org.junit.jupiter.api.Test
    void calculateTax() {
    }

    @org.junit.jupiter.api.Test
    void calculateTotal() {
    }

    @org.junit.jupiter.api.Test
    void getAllOrders() {
    }

    @org.junit.jupiter.api.Test
    void getNewOrderNumber() {
    }

    @org.junit.jupiter.api.Test
    void promptUserAddOrder() {
    }
}