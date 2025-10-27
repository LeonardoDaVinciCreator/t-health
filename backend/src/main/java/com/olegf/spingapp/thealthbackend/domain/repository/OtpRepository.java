package com.olegf.spingapp.thealthbackend.domain.repository;

import com.olegf.spingapp.thealthbackend.domain.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByPhone(String phone);
    boolean existsByPhone(String phone);
}
