package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, String> {
}
