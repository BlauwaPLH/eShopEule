package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
