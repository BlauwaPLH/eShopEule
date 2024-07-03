package org.senju.eshopeule.repository.mongodb;

import org.senju.eshopeule.model.product.ProductMeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMetaRepository extends MongoRepository<ProductMeta, String> {

}
