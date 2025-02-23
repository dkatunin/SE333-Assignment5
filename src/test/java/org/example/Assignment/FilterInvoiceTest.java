package org.example.Assignment;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterInvoiceTest {

    @Test
    public void filterInvoiceTest() {
        FilterInvoice filterInvoice = new FilterInvoice();
        List<Invoice> result = filterInvoice.lowValueInvoices();
        assertTrue(result.stream().allMatch(invoice -> invoice.getValue() < 1));
    }
}
