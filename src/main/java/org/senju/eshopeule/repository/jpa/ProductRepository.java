package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.repository.projection.ProductPriceOptionView;
import org.senju.eshopeule.repository.projection.ProductPriceView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findBySlug(String slug);

    @Query(value = "SELECT p FROM Product p WHERE p.brand.id = :brandId")
    Page<Product> findAllByBrandId(@Param("brandId") String brandId, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.brand.name = :brandSlug")
    Page<Product> findAllByBrandSlug(@Param("brandSlug") String brandSlug, Pageable pageable);

    @Query(value = "SELECT p FROM Product p JOIN ProductCategory pc WHERE pc.category.id = :categoryId")
    Page<Product> findAllByCategoryId(@Param("categoryId") String categoryId, Pageable pageable);

    @Query(value = "SELECT p FROM Product p JOIN ProductCategory pc WHERE pc.category.slug = :cateSlug")
    Page<Product> findAllByCategorySlug(@Param("cateSlug") String categorySlug, Pageable pageable);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM products WHERE slug = :slug)", nativeQuery = true)
    boolean checkExistsWithSlug(@Param("slug") String slug);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM products WHERE slug = :slug AND id != :prodId)", nativeQuery = true)
    boolean checkExistsWithSlugExceptId(@Param("prodId") String productId, @Param("slug") String slug);

    @Query(value = "SELECT DISTINCT id, price, discount FROM products WHERE id IN :prodIds", nativeQuery = true)
    List<ProductPriceView> getPriceViewByIds(@Param("prodIds") List<String> productIds);

    @Query(value = "SELECT DISTINCT p.id, p.price, p.discount, po.id " +
            "FROM products AS p " +
            "LEFT JOIN product_options AS po " +
            "ON po.product_id = p.id " +
            "WHERE p.id IN :prodIds", nativeQuery = true)
    List<ProductPriceOptionView> getPriceOptionViewByIds(@Param("prodIds") Set<String> productIds);

}
