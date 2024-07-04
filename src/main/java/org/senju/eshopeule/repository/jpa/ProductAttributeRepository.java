package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductAttribute;
import org.senju.eshopeule.repository.projection.SimpleProdAttrView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, String> {

    @Query(value = "SELECT pa.id, pa.name FROM product_attributes AS pa INNER JOIN product_attribute_category AS pc ON pa.id = pc.product_attribute_id WHERE pc.category_id = :cateId", nativeQuery = true)
    List<SimpleProdAttrView> getAllProdAttrWithCategoryId(@Param("cateId") String categoryId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM product_attributes WHERE name = :name)", nativeQuery = true)
    boolean checkProdAttrExistsWithName(@Param("name") String name);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM product_attributes WHERE name = :name AND id != :id)", nativeQuery = true)
    boolean checkProdAttrExistsWithNameExceptId(@Param("name") String name, @Param("id") String id);
}
