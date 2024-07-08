package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductAttribute;
import org.senju.eshopeule.repository.projection.SimpleProdAttrView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, String> {

    @Query(value = "SELECT pa.id, pa.name FROM product_attributes AS pa INNER JOIN product_attribute_category AS pc ON pa.id = pc.product_attribute_id WHERE pc.category_id = :cateId", nativeQuery = true)
    List<SimpleProdAttrView> getAllProdAttrWithCategoryId(@Param("cateId") String categoryId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM product_attributes WHERE name = :name)", nativeQuery = true)
    boolean checkProdAttrExistsWithName(@Param("name") String name);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM product_attributes WHERE name = :name AND id != :id)", nativeQuery = true)
    boolean checkProdAttrExistsWithNameExceptId(@Param("name") String name, @Param("id") String id);

    Optional<ProductAttribute> findByName(String name);

    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 FROM product_attribute_category AS pac " +
            "INNER JOIN product_category AS pc ON pac.category_id = pc.category_id " +
            "WHERE pac.product_attribute_id = :prodAttrId AND pc.product_id = :prodId)", nativeQuery = true)
    boolean checkCategoryContainsProductAndAttribute(@Param("prodId") String productId, @Param("prodAttrId") String productAttributeId);
}
