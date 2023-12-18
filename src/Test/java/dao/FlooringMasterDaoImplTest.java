package dao;

import dto.Order;
import dto.Tax;
import enums.FileType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasterDaoImplTest {
    FlooringMasterDaoImpl dao ;

    @BeforeEach
    void setUp() throws IOException {
        dao = new FlooringMasterDaoImpl();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void getDateOrder() {
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getAllTaxRates() throws IOException {
        Map<String, Tax> expectedTaxes = new HashMap<>();
        File taxFile = new File(FileType.TAX.getFileName());
        Scanner sc = new Scanner(taxFile);

        // skip the first line - header
        if (sc.hasNextLine()) {
            sc.nextLine();
        }

        // go through the file
        while (sc.hasNextLine()) {
            Tax tax = dao.unmarshallTax(sc.nextLine());
            expectedTaxes.put(tax.getStateAbbreviation(), tax);
        }

        sc.close();
        dao.loadFromTaxFile();
        for (String stateAbbreviation : expectedTaxes.keySet()) {
            assertTrue(expectedTaxes.containsKey(stateAbbreviation));
            assertTrue(dao.getTaxes().containsKey(stateAbbreviation));

        }
        FlooringMasterDaoImpl daoWithInvalidPath = new FlooringMasterDaoImpl();


    }

    @Test
    void checkValidOrder() {
    }

    @Test
    void removeOrder() throws IOException {

        int sizeBeforeAdding = dao.getAllOrders().size();
        int orderNumber = (new Random()).nextInt();


        // Create a sample order
        Order order = new Order(orderNumber, LocalDate.now(), "HH", "TX", "Wood", BigDecimal.TEN);

        // Add the order
        dao.addOrder(order);

        // Remove the order
        Order removedOrder = dao.removeOrder(orderNumber, LocalDate.now());

        // Check if the order is removed successfully
        assertNotNull(removedOrder);
        assertEquals(order, removedOrder);
        assertEquals(sizeBeforeAdding, dao.getAllOrders().size());
    }

    @Test
    void addOrderAndGetAllOrders() throws IOException {
        FlooringMasterDaoImpl dao = new FlooringMasterDaoImpl();

        int sizeBeforeAdding = dao.getAllOrders().size();
        int orderNumber = (new Random()).nextInt();
        // Create a sample order
        Order order = new Order(orderNumber, LocalDate.now(), "HH", "TX", "Laminate", BigDecimal.valueOf(101.0));

        // Add the order
        dao.addOrder(order);

        // Get all orders and check if the added order is present
        assertEquals(sizeBeforeAdding + 1, dao.getAllOrders().size());
        assertTrue(dao.getAllOrders().contains(order));
        dao.removeOrder(1, order.getOrderDate());
    }



}