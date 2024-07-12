package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query(value = "SELECT c.id FROM customers AS c " +
            "INNER JOIN users AS u " +
            "ON c.user_id = u.id " +
            "WHERE u.username = :un", nativeQuery = true)
    Optional<String> findIdByUsername(@Param("un") String username);
}
