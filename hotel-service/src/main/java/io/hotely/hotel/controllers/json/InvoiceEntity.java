package io.hotely.hotel.controllers.json;

import java.util.UUID;
import java.sql.Timestamp;
import java.time.LocalDate;

public record InvoiceEntity (
    UUID id,
    UUID booking_id,
    UUID customer_id,
    float total,
    Timestamp issued,
    Boolean paid,
    LocalDate payment_date,
    Boolean cancelled
    ) {}
