package org.senju.eshopeule.repository.es;

import org.senju.eshopeule.model.product.ProductESDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductESRepository extends ElasticsearchRepository<ProductESDoc, String> {

}
