package io.github.SzymonWoroniecki.gym_membership_system.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record RevenueReportItem(
        Long gymId,
        String gymName,
        Currency currency,
        BigDecimal totalAmount
) {
}
