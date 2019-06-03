package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.Invoice;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDaoIntegrationTest {
    private static final DataSource IN_DATA_SOURCE = DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();
    private static final String PATH_TO_FILE = "src/test/resources/invoices_table.sql";
    private static final Invoice INVOICE_TEST = new Invoice(1, 10, "Something");
    private InvoiceDAO invoiceDAO;


    @BeforeAll
    public static void createTable() throws IOException, SQLException {
        createInvoicesTable();
    }

    @BeforeEach
    public void deleteAllFromTable() throws IOException, SQLException {
        deleteTable();
        invoiceDAO = new InvoiceDAO(IN_DATA_SOURCE);

    }

    @Test
    public void saveInvoiceThenReturnIt() {
        final Invoice expected = invoiceDAO.save(INVOICE_TEST);
        assertNotNull(expected.getId());
    }

    @Test
    public void deleteInvoiceWhenOkThenReturnTrue() {
        final long id = invoiceDAO.save(INVOICE_TEST).getId();
        assertTrue(invoiceDAO.delete(id));
    }

    @Test
    public void updateInvoiceWhenOkThenReturnTrue() {
        final long id = invoiceDAO.save(INVOICE_TEST).getId();
        final Invoice expect = new Invoice(id, 5, "sth");
        assertTrue(invoiceDAO.update(id, expect));
    }

    @Test
    public void findInvoiceByIdWhenOkThenReturnThatOne() {
        final long id = invoiceDAO.save(INVOICE_TEST).getId();
        assertEquals(invoiceDAO.findById(id).getAttributes(), INVOICE_TEST.getAttributes());
        assertEquals(invoiceDAO.findById(id).getPrice(), INVOICE_TEST.getPrice());
    }

    @Test
    public void findAllInvoicesFromDataBaseWhenOkThenReturnListWithThem() {
        invoiceDAO.save(INVOICE_TEST);
        final List<Invoice> invoices = invoiceDAO.findAll();
        assertEquals(invoices.get(0).getPrice(), invoiceDAO.findAll().get(0).getPrice());
        assertEquals(invoices.get(0).getAttributes(), invoiceDAO.findAll().get(0).getAttributes());


    }

    private static String scriptReader() throws IOException {
        return String.join("", Files.readAllLines(Paths.get(PATH_TO_FILE)));
    }

    private static void runDataBase(String script) throws SQLException, IOException {
        try (Connection con = IN_DATA_SOURCE.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.execute(script);
            }
        }
    }

    private static void createInvoicesTable() throws IOException, SQLException {
        runDataBase(scriptReader());
    }

    private static void deleteTable() throws SQLException, IOException {
        runDataBase("TRUNCATE TABLE invoices;");
    }
}
