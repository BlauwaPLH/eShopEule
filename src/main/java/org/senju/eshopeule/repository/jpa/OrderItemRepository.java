package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.order.OrderItem;
import org.senju.eshopeule.repository.projection.OrderItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    @Query(value = "SELECT EXISTS " +
            "(SELECT 1 FROM order_items AS oi " +
            "INNER JOIN orders AS o ON oi.order_id = o.id " +
            "INNER JOIN customers AS c on o.customer_id = c.id " +
            "INNER JOIN users AS u ON c.user_id = u.id " +
            "WHERE u.username = :un AND oi.id = :oiId)", nativeQuery = true)
    boolean checkExistsByUsername(@Param("oiId") String oderItemId, @Param("un") String username);


    @Query(value = "SELECT oi.id, oi.product_id, p.quantity, p.price, p.discount, p.option_id " +
            "FROM order_items AS oi " +
            "INNER JOIN products AS p ON oi.product_id = p.id " +
            "WHERE oi.id = :oiId", nativeQuery = true)
    Optional<OrderItemView> getItemViewById(@Param("oiId") String orderItemId);

    @Query(value = "SELECT oi.id, oi.product_id, p.quantity, p.price, p.discount, p.option_id " +
            "FROM order_items AS oi " +
            "INNER JOIN orders AS o ON oi.order_id = o.id " +
            "INNER JOIN products AS p ON oi.product_id = p.id " +
            "WHERE o.id = :oId", nativeQuery = true)
    List<OrderItemView> getItemViewByOrderId(@Param("oId") String orderId);
}
