package io.github.SzymonWoroniecki.gym_membership_system.service;

import io.github.SzymonWoroniecki.gym_membership_system.dto.MemberRequest;
import io.github.SzymonWoroniecki.gym_membership_system.dto.MemberResponse;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Gym;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Member;
import io.github.SzymonWoroniecki.gym_membership_system.entity.MembershipPlan;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Money;
import io.github.SzymonWoroniecki.gym_membership_system.enums.MembershipStatus;
import io.github.SzymonWoroniecki.gym_membership_system.enums.PlanType;
import io.github.SzymonWoroniecki.gym_membership_system.exception.EmailAlreadyExistsException;
import io.github.SzymonWoroniecki.gym_membership_system.exception.MemberAlreadyCancelledException;
import io.github.SzymonWoroniecki.gym_membership_system.exception.MemberNotFoundException;
import io.github.SzymonWoroniecki.gym_membership_system.exception.PlanCapacityReachedException;
import io.github.SzymonWoroniecki.gym_membership_system.exception.PlanNotFoundException;
import io.github.SzymonWoroniecki.gym_membership_system.repository.MemberRepository;
import io.github.SzymonWoroniecki.gym_membership_system.repository.MembershipPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipPlanRepository planRepository;

    @InjectMocks
    private MemberService memberService;

    private Gym testGym;
    private MembershipPlan testPlan;
    private MemberRequest testRequest;

    @BeforeEach
    void setUp() {
        testGym = new Gym();
        testGym.setId(1L);
        testGym.setName("Test Gym");
        testGym.setAddress("Test Address");
        testGym.setPhoneNumber("+48123456789");

        testPlan = new MembershipPlan();
        testPlan.setId(10L);
        testPlan.setName("Premium");
        testPlan.setType(PlanType.PREMIUM);
        testPlan.setPrice(new Money(new BigDecimal("99.00"), Currency.getInstance("PLN")));
        testPlan.setDurationInMonths(1);
        testPlan.setMaxMembers(2);
        testPlan.setGym(testGym);

        testRequest = new MemberRequest("Jan Kowalski", "jan@example.com");
    }

    // === REGISTER TESTS ===

    @Test
    @DisplayName("Should register member when plan has capacity")
    void register_shouldSucceed_whenCapacityAvailable() {
        // przygotowanie scenariusza
        when(planRepository.findById(10L)).thenReturn(Optional.of(testPlan));
        when(memberRepository.countByPlanIdAndStatus(10L, MembershipStatus.ACTIVE))
                .thenReturn(0L);
        when(memberRepository.existsByEmailIgnoreCase("jan@example.com"))
                .thenReturn(false);
        when(memberRepository.save(any(Member.class)))
                .thenAnswer(invocation -> {
                    Member m = invocation.getArgument(0);
                    m.setId(100L);
                    return m;
                });

        // wywołanie metody
        MemberResponse response = memberService.register(10L, testRequest);

        // weryfikacja wyniku
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.fullName()).isEqualTo("Jan Kowalski");
        assertThat(response.email()).isEqualTo("jan@example.com");
        assertThat(response.status()).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(response.planName()).isEqualTo("Premium");
        assertThat(response.gymName()).isEqualTo("Test Gym");

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("Should throw PlanCapacityReachedException when plan is full")
    void register_shouldThrow_whenCapacityReached() {
        // scenariusz: plan ma maxMembers=2, mamy już 2 aktywnych
        when(planRepository.findById(10L)).thenReturn(Optional.of(testPlan));
        when(memberRepository.countByPlanIdAndStatus(10L, MembershipStatus.ACTIVE))
                .thenReturn(2L);

        // wywołanie i werywfikacja
        assertThatThrownBy(() -> memberService.register(10L, testRequest))
                .isInstanceOf(PlanCapacityReachedException.class)
                .hasMessageContaining("Plan capacity reached");

        // żaden member nie powinien zostać zapisany
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("Should throw PlanNotFoundException when plan does not exist")
    void register_shouldThrow_whenPlanNotFound() {
        // given
        when(planRepository.findById(999L)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> memberService.register(999L, testRequest))
                .isInstanceOf(PlanNotFoundException.class)
                .hasMessageContaining("999");

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email is taken")
    void register_shouldThrow_whenEmailAlreadyExists() {
        // given
        when(planRepository.findById(10L)).thenReturn(Optional.of(testPlan));
        when(memberRepository.countByPlanIdAndStatus(10L, MembershipStatus.ACTIVE))
                .thenReturn(0L);
        when(memberRepository.existsByEmailIgnoreCase("jan@example.com"))
                .thenReturn(true);

        // when + then
        assertThatThrownBy(() -> memberService.register(10L, testRequest))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("jan@example.com");

        verify(memberRepository, never()).save(any(Member.class));
    }

    // === CANCEL TESTS ===

    @Test
    @DisplayName("Should change status to CANCELLED when member is active")
    void cancel_shouldSucceed_whenMemberIsActive() {
        // scenariusz
        Member activeMember = new Member();
        activeMember.setId(100L);
        activeMember.setFullName("Jan Kowalski");
        activeMember.setEmail("jan@example.com");
        activeMember.setPlan(testPlan);
        activeMember.setStatus(MembershipStatus.ACTIVE);
        activeMember.setMembershipStartDate(java.time.LocalDate.now());

        when(memberRepository.findById(100L)).thenReturn(Optional.of(activeMember));
        when(memberRepository.save(any(Member.class))).thenReturn(activeMember);

        // wywołanie
        MemberResponse response = memberService.cancel(100L);

        // weryfikacja
        assertThat(response.status()).isEqualTo(MembershipStatus.CANCELLED);
        verify(memberRepository).save(activeMember);
    }

    @Test
    @DisplayName("Should throw MemberAlreadyCancelledException when member is already cancelled")
    void cancel_shouldThrow_whenAlreadyCancelled() {
        // scenariusz
        Member cancelledMember = new Member();
        cancelledMember.setId(100L);
        cancelledMember.setFullName("Jan Kowalski");
        cancelledMember.setEmail("jan@example.com");
        cancelledMember.setPlan(testPlan);
        cancelledMember.setStatus(MembershipStatus.CANCELLED);
        cancelledMember.setMembershipStartDate(java.time.LocalDate.now());

        when(memberRepository.findById(100L)).thenReturn(Optional.of(cancelledMember));

        // wywołanie i weryfikacja
        assertThatThrownBy(() -> memberService.cancel(100L))
                .isInstanceOf(MemberAlreadyCancelledException.class);

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("Should throw MemberNotFoundException when member does not exist")
    void cancel_shouldThrow_whenMemberNotFound() {
        // scenariusz
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // wywołanie i weryfikacja
        assertThatThrownBy(() -> memberService.cancel(999L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("999");
    }
}