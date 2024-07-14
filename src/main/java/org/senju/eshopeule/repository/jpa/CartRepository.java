package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    @Query(value = "SELECT c FROM Cart c WHERE c.customer.user.username = :cusName AND c.status = 'ACTIVE'")
    List<Cart> getActiveCartByUsername(@Param("cusName") String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE carts SET status = 'COMPLETED' " +
            "WHERE status = 'ACTIVE' AND customer_id = " +
            "(SELECT cus.id FROM customers AS cus " +
            "INNER JOIN users AS u " +
            "ON cus.user_id = u.id " +
            "WHERE u.username = :username)", nativeQuery = true)
    void updateCompletedCartWithUsername(@Param("username") String username);
}
