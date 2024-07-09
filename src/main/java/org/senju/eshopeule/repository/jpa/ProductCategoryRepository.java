package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM product_category WHERE product_id = :prodId AND category_id = :cateId)", nativeQuery = true)
    boolean checkExistsWithProductIdAndCategoryId(@Param("prodId") String productId, @Param("cateId") String categoryId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM product_category WHERE product_id = :prodId AND category_id NOT IN :cateIds", nativeQuery = true)
    void deleteWithCategoryIdNotInList(@Param("prodId") String productId, @Param("cateIds") List<String> categoryId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM product_category WHERE product_id = :prodId", nativeQuery = true)
    void deleteByProductId(@Param("prodId") String productId);
}
