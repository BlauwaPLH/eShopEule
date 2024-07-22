package org.senju.eshopeule.service;

import org.senju.eshopeule.model.product.Product;

import java.util.List;

public interface ProductSyncDataService {

    void syncData(Product savedEntity, List<String> categoryIds, String brandId);

    void syncImageData(String productId);

}
