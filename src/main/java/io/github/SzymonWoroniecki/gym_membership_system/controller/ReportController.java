package io.github.SzymonWoroniecki.gym_membership_system.controller;

import io.github.SzymonWoroniecki.gym_membership_system.dto.RevenueReportItem;
import io.github.SzymonWoroniecki.gym_membership_system.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueReportItem>> getMonthlyRevenue() {
        return ResponseEntity.ok(reportService.getMonthlyRevenue());
    }
}