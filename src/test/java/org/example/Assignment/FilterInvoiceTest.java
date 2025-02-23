package org.example.Assignment;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
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

    @Test
    public void testWhenLowInvoicesSent() {
        FilterInvoice mockFilterInvoice = Mockito.mock(FilterInvoice.class);
        SAP mockSAP = Mockito.mock(SAP.class);
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilterInvoice, mockSAP);

        List<Invoice> mockInvoices = Arrays.asList(
                new Invoice("Customer A", 25),
                new Invoice("Customer B", 50),
                new Invoice("Customer C", 99)
        );

        when(mockFilterInvoice.lowValueInvoices()).thenReturn(mockInvoices);
        sender.sendLowValuedInvoices();

        verify(mockSAP, times(1)).send(mockInvoices.get(0));
        verify(mockSAP, times(1)).send(mockInvoices.get(1));
        verify(mockSAP, times(1)).send(mockInvoices.get(2));
        verify(mockSAP, times(3)).send(any(Invoice.class));
    }

    @Test
    public void testWhenNoInvoices() {
        FilterInvoice mockFilterInvoice = Mockito.mock(FilterInvoice.class);
        SAP mockSAP = Mockito.mock(SAP.class);
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilterInvoice, mockSAP);

        when(mockFilterInvoice.lowValueInvoices()).thenReturn(Collections.emptyList());
        sender.sendLowValuedInvoices();

        verify(mockSAP, never()).send(any(Invoice.class));
    }
}
