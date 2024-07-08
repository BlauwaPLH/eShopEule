package org.senju.eshopeule.mappers;

import org.senju.eshopeule.dto.ProductDTO;
import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.model.product.ProductImage;

import java.util.List;


public interface ProductMapper<D extends ProductDTO> extends BaseMapper<Product, D> {

    default List<String> mappingImageUrls(Product entity) {
        if (entity.getProductImages() == null) return null;
        return entity.getProductImages().stream()
                .map(ProductImage::getImageUrl)
                .toList();
    }

}
