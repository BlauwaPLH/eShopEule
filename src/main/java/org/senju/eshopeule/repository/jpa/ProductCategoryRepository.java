package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

}
