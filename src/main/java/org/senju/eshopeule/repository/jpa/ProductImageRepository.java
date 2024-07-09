package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {

    @Query(value = "SELECT name FROM product_images WHERE id = :id", nativeQuery = true)
    Optional<String> getNameById(@Param("id") String id);

    @Query(value = "SELECT name FROM product_images WHERE product_id = :prodId", nativeQuery = true)
    List<String> getNameByProductId(@Param("prodId") String productId);

    @Query(value = "SELECT image_url FROM product_images WHERE product_id = :prodId", nativeQuery = true)
    List<String> getAllImageUrlByProductId(@Param("prodId") String productId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM product_images WHERE product_id = :prodId", nativeQuery = true)
    void deleteByProductId(@Param("prodId") String productId);
}
