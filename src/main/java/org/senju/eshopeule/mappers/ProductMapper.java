package org.senju.eshopeule.mappers;

import org.senju.eshopeule.dto.ProductDTO;
import org.senju.eshopeule.model.product.Product;


public interface ProductMapper<D extends ProductDTO> extends BaseMapper<Product, D> {

}
