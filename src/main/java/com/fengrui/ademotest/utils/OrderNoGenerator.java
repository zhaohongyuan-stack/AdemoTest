package com.fengrui.ademotest.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderNoGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generatePurchaseOrderNo() {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        return "PO" + dateStr + generateSequence();
    }

    public static String generateSaleOrderNo() {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        return "SO" + dateStr + generateSequence();
    }

    private static String generateSequence() {
        int sequence = (int) (Math.random() * 900) + 100;
        return String.valueOf(sequence);
    }
}
