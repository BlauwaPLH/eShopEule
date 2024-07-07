package org.senju.eshopeule.repository.mongodb;

import org.senju.eshopeule.model.product.ProductMeta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductMetaRepository extends MongoRepository<ProductMeta, String> {

    Optional<ProductMeta> findByProductId(String productId);

    void deleteByProductId(String productId);
}
