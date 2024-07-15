package org.senju.eshopeule.repository.mongodb;

import org.senju.eshopeule.model.rating.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

    Page<Rating> findAllByProductId(String productId, Pageable pageRequest);

    Optional<Rating> findByProductIdAndCustomerId(String productId, String customerId);
}
