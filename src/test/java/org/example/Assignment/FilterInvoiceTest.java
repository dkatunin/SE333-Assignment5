package org.example.Assignment;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilterInvoiceTest {

    @Test
    public void filterInvoiceTest() {
        Database db = new Database();
        QueryInvoicesDAO dao = new QueryInvoicesDAO(db);
        FilterInvoice filterInvoice = new FilterInvoice(dao);

        dao.save(new Invoice("Customer A", 50));
        dao.save(new Invoice("Customer B", 200));

        List<Invoice> result = filterInvoice.lowValueInvoices();
        assertEquals(1, result.size());
    }

    @Test
    void filterInvoiceStubbedTest() {
        QueryInvoicesDAO mockDao = Mockito.mock(QueryInvoicesDAO.class);
        FilterInvoice filterInvoice = new FilterInvoice(mockDao);
        List<Invoice> mockInvoices = Arrays.asList(
                new Invoice("Customer A", 50),
                new Invoice("Customer B", 200)
        );

        when(mockDao.all()).thenReturn(mockInvoices);
        List<Invoice> result = filterInvoice.lowValueInvoices();
        assertEquals(1, result.size());
        assertEquals("Customer A", result.get(0).getCustomer());
    }
}
