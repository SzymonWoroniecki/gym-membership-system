package io.github.SzymonWoroniecki.gym_membership_system.service;

import io.github.SzymonWoroniecki.gym_membership_system.dto.RevenueReportItem;
import io.github.SzymonWoroniecki.gym_membership_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<RevenueReportItem> getMonthlyRevenue() {
        return memberRepository.calculateRevenueReport();
    }
}