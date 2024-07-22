package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM brands WHERE name = :name OR slug = :slug)", nativeQuery = true)
    boolean checkBrandExistsWithNameOrSlug(@Param("name") String name, @Param("slug") String slug);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM brands WHERE (name = :name OR slug = :slug) AND id != :id)", nativeQuery = true)
    boolean checkBrandExistsWithNameOrSlugExceptId(@Param("name") String name, @Param("slug") String slug, @Param("id") String id);

    Optional<String> findNameById(String id);
}
