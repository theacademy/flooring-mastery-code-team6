package dao;

import dto.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasterDaoImplTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDateOrder() {
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getAllTaxRates() {
    }

    @Test
    void checkValidOrder() {
    }

    @Test
    void removeOrder() {
    }

    @Test
    void addOrderAndGetAllOrders() throws IOException {
        FlooringMasterDaoImpl dao = new FlooringMasterDaoImpl();

        int sizeBeforeAdding = dao.getAllOrders().size();
        // Create a sample order
        Order order = new Order(1, LocalDate.now(), "HH", "TX", "Laminate", BigDecimal.valueOf(101.0));

        // Add the order
        dao.addOrder(order);

        // Get all orders and check if the added order is present
        assertEquals(sizeBeforeAdding + 1, dao.getAllOrders().size());
        assertTrue(dao.getAllOrders().contains(order));
        dao.removeOrder(order.getOrderNumber(), order.getOrderDate());
    }

    @Test
    void editOrder() {
    }

    @Test
    void getAllOrders() {
    }

    @Test
    void readProduct() {
    }

    @Test
    void writeOrderToFile() {
    }
}