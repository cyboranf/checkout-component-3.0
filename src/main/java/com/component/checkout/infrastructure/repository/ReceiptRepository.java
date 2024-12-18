package com.component.checkout.infrastructure.repository;

import com.component.checkout.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
