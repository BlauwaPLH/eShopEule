package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, String> {

    @Query(value = "DELETE FROM product_attribute_values WHERE product_option_id = :optionId", nativeQuery = true)
    void deleteByOptionId(@Param("optionId") String productOptionId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product_attribute_values " +
            "SET value = :val " +
            "WHERE id = :attrValId AND product_option_id = :optionId AND product_attribute_id = :attrId", nativeQuery = true)
    void updateAttributeValue(@Param("attrValId") String attributeValueId, @Param("optionId") String optionId,
                              @Param("attrId") String attributeId, @Param("val") String value);
}
