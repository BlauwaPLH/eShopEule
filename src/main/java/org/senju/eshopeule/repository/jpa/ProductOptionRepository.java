package org.senju.eshopeule.repository.jpa;

import org.senju.eshopeule.model.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, String> {

    List<ProductOption> findAllByProductId(String productId);

    @Query(value = "SELECT po FROM ProductOption po WHERE po.id = :optionId AND po.product.id = :prodId")
    Optional<ProductOption> findByOptionIdAndProductId(@Param("optionId") String optionId, @Param("prodId") String productId);

}
