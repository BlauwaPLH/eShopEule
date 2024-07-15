package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.RatingDTO;
import org.senju.eshopeule.dto.response.RatingPagingResponse;
import org.springframework.data.domain.Pageable;

public interface RatingService {

    RatingDTO getById(String id);

    RatingPagingResponse getRatingPageByProductId(String productId, Pageable pageRequest);

    RatingDTO createNewRating(RatingDTO dto);

    RatingDTO updateRating(RatingDTO dto);

    void deleteById(String id);

}
