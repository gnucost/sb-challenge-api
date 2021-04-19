package com.challenge.sb.demo.repositories;

import com.challenge.sb.demo.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
