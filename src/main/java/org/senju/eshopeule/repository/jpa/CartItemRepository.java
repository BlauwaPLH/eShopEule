package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.cart.CartItem;
import org.senju.eshopeule.repository.projection.CartItemQuantityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    @Query(value = "SELECT id, quantity FROM cart_items WHERE cart_id = :cartId AND product_id = :prodId", nativeQuery = true)
    Optional<CartItemQuantityView> getItemQuantity(@Param("cartId") String cartId, @Param("prodId") String productId);

    @Query(value = "SELECT id, quantity FROM cart_items WHERE cart_id = :cartId AND product_id = :prodId AND option_id = :opId", nativeQuery = true)
    Optional<CartItemQuantityView> getItemQuantity(@Param("cartId") String cartId, @Param("prodId") String productId, @Param("opId") String optionId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart_items SET quantity = :quantity WHERE id = :itemId", nativeQuery = true)
    void updateQuantityById(@Param("itemId") String itemId, @Param("quantity") Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM cart_items WHERE cart_id = :cId AND product_id = :pId", nativeQuery = true)
    void deleteByCartIdAndProductId(@Param("cId") String cartId, @Param("pId") String productId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM cart_items WHERE cart_id = :cId AND option_id = :oId", nativeQuery = true)
    void deleteByCartIdAndOptionId(@Param("cId") String cartId, @Param("oId") String optionId);

}
