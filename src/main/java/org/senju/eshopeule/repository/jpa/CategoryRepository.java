package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM categories WHERE name = :name OR slug = :slug)", nativeQuery = true)
    boolean checkCategoryExistsWithNameOrSlug(@Param("name") String name, @Param("slug") String slug);
}
