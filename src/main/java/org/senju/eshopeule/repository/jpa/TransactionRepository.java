package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.order.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
