package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.cart.CartItem;
import org.senju.eshopeule.repository.projection.CartItemQuantityView;
import org.senju.eshopeule.repository.projection.CartItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    @Query(value = "SELECT id, quantity FROM cart_items " +
            "WHERE cart_id = :cartId AND product_id = :prodId " +
            "AND ((option_id = :opId) OR (option_id IS NULL AND :opId IS NULL))", nativeQuery = true)
    Optional<CartItemQuantityView> getItemQuantityView(@Param("cartId") String cartId, @Param("prodId") String productId, @Param("opId") String optionId);


    @Query(value = "SELECT DISTINCT ci.id, ci.quantity AS itemQuantity, ci.product_id, ci.option_id, p.price, p.discount, p.quantity AS productQuantity " +
            "FROM cart_items AS ci INNER JOIN products AS p ON ci.product_id = p.id WHERE ci.cart_id = :cartId", nativeQuery = true)
    List<CartItemView> getItemViewByCartId(@Param("cartId") String cartItemId);

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
