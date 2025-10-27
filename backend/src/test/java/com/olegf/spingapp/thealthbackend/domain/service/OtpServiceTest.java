package com.olegf.spingapp.thealthbackend.domain.service;

import com.olegf.spingapp.thealthbackend.domain.entity.Otp;
import com.olegf.spingapp.thealthbackend.domain.repository.OtpRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtpServiceTest {

    OtpRepository otpRepository = mock(OtpRepository.class);
    private ArgumentCaptor<Otp> otpArgumentCaptor = ArgumentCaptor.forClass(Otp.class);
    private OtpProperties otpProperties = new OtpProperties("PT1M", 5);

    private OtpService otpService =  new OtpService(otpRepository, otpProperties);

    // Test

    @Test
    void should_verify() {
        // given
        Otp otp = new Otp("+79090903315", "1q2w3e");
        doReturn(Optional.of(otp)).when(otpRepository).findByPhone(eq(otp.getPhone()));

        // when & then
        Assertions.assertThatNoException().isThrownBy(() -> {
            otpService.verify(otp.getCode(), otp.getPhone());
        });

        assertThat(otp.getAttempts()).isEqualTo(0);

        verify(otpRepository).findByPhone(eq(otp.getPhone()));
        verifyNoMoreInteractions(otpRepository);
    }

    @Test
    void should_throw_missmatch_exception() {
        // given
        Otp otp = new Otp("+79090903315", "1q2w3e");
        doReturn(Optional.of(otp)).when(otpRepository).findByPhone(eq(otp.getPhone()));

        // when & then
        Assertions.assertThatThrownBy(() -> {
            otpService.verify("OopsHui", otp.getPhone());
        })
                .isInstanceOf(OtpException.MissmatchException.class)
                .hasMessage("OTP code mismatch");

        assertThat(otp.getAttempts()).isEqualTo(1);

        verify(otpRepository).findByPhone(eq(otp.getPhone()));
        verifyNoMoreInteractions(otpRepository);
    }

    @Test
    void should_throw_expired_exception() {
        // given
        Otp otp = new Otp("+79090903315", "1q2w3e");
        otp.setCreatedAt(LocalDateTime.now().minusSeconds(61));

        doReturn(Optional.of(otp)).when(otpRepository).findByPhone(eq(otp.getPhone()));

        // when & then
        Assertions.assertThatThrownBy(() -> {
                    otpService.verify(otp.getCode(), otp.getPhone());
                })
                .isInstanceOf(OtpException.ExpiredException.class)
                .hasMessage("OTP expired");

        assertThat(otp.getAttempts()).isEqualTo(0);

        verify(otpRepository).findByPhone(eq(otp.getPhone()));
        verifyNoMoreInteractions(otpRepository);
    }

    @Test
    void should_throw_attempts_exception() {
        // given
        Otp otp = new Otp("+79090903315", "1q2w3e");
        otp.setAttempts(6);

        doReturn(Optional.of(otp)).when(otpRepository).findByPhone(eq(otp.getPhone()));

        // when & then
        Assertions.assertThatThrownBy(() -> {
                    otpService.verify(otp.getCode(), otp.getPhone());
                })
                .isInstanceOf(OtpException.AttemptsException.class)
                .hasMessage("OTP attempts exceeded");

        verify(otpRepository).findByPhone(eq(otp.getPhone()));
        verifyNoMoreInteractions(otpRepository);
    }

    @Test
    void should_throw_not_found_exception() {
        // given
        String phone = "+9039409283";

        // when & then
        Assertions.assertThatThrownBy(() -> {
                    otpService.verify("d", phone);
                })
                .isInstanceOf(OtpException.NotFoundException.class)
                .hasMessage("OTP not found");

        verify(otpRepository).findByPhone(eq(phone));
        verifyNoMoreInteractions(otpRepository);
    }

    // handleOtp
    @Test
    void should_handle_otp() {
        // given
        Otp otp = new Otp("+79090331515", "1q2w3e");

        doReturn(true).when(otpRepository).existsByPhone(eq(otp.getPhone()));
        doReturn(Optional.of(otp)).when(otpRepository).findByPhone(eq(otp.getPhone()));
        doReturn(otp).when(otpRepository).save(any());

        // when & then
        Assertions.assertThatNoException().isThrownBy(() -> {
            otpService.handleOtp(otp.getPhone());
        });

        verify(otpRepository).existsByPhone(eq(otp.getPhone()));
        verify(otpRepository).findByPhone(eq(otp.getPhone()));
        verify(otpRepository).save(any());

        verifyNoMoreInteractions(otpRepository);
    }

    @Test
    void should_generate_otp() {
        // given
        String phone = "+79090331515";

        doReturn(false).when(otpRepository).existsByPhone(eq(phone));

        // when
        otpService.handleOtp(phone);

        // then
        verify(otpRepository).existsByPhone(eq(phone));
        verify(otpRepository).save(otpArgumentCaptor.capture());
        Otp result = otpArgumentCaptor.getValue();

        assertThat(result.getCode()).isNotNull();
        assertThat(result.getAttempts()).isEqualTo(0);
        assertThat(result.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(result.getPhone()).isEqualTo(phone);
        assertThat(result.getId()).isNull();

        verifyNoMoreInteractions(otpRepository);
    }
}
