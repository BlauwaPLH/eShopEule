package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM orders AS o " +
            "INNER JOIN customers AS c ON o.customer_id = c.id " +
            "INNER JOIN users AS u ON c.user_id = u.id " +
            "WHERE u.username = :un AND o.id = :oId)", nativeQuery = true)
    boolean checkExistsByUsername(@Param("oId") String orderId, @Param("un") String username);

    @Query(value = "SELECT o FROM Order o WHERE o.customer.user.username = :username")
    Page<Order> getAllOrderWithUsername(@Param("username") String username, Pageable pageable);
}
