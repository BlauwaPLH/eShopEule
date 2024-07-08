package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.Category;
import org.senju.eshopeule.repository.projection.SimpleCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM categories WHERE name = :name OR slug = :slug)", nativeQuery = true)
    boolean checkCategoryExistsWithNameOrSlug(@Param("name") String name, @Param("slug") String slug);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM categories WHERE (name = :name OR slug = :slug) AND id != :id)", nativeQuery = true)
    boolean checkCategoryExistsWithNameOrSlugExceptId(@Param("name") String name, @Param("slug") String slug, @Param("id") String id);

    @Query(value = "SELECT COUNT(1) FROM categories WHERE id IN :cateIds", nativeQuery = true)
    int countCategoriesWithIds(@Param("cateIds") List<String> categoryIds);

    @Query(value = "SELECT id, name, slug FROM categories WHERE parent_id = :parentId", nativeQuery = true)
    List<SimpleCategoryView> getAllCategoryChildrenByParentId(@Param("parentId") String parentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE categories SET parent_id = NULL WHERE parent_id = :parentId", nativeQuery = true)
    void updateChildBeforeDeleteParent(@Param("parentId") String parentId);

}
