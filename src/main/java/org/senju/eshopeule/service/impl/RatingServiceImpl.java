package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.RatingDTO;
import org.senju.eshopeule.dto.response.RatingPagingResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.exceptions.RatingException;
import org.senju.eshopeule.mappers.RatingMapper;
import org.senju.eshopeule.model.order.OrderItem;
import org.senju.eshopeule.model.rating.Rating;
import org.senju.eshopeule.model.user.Customer;
import org.senju.eshopeule.repository.jpa.CustomerRepository;
import org.senju.eshopeule.repository.jpa.OrderItemRepository;
import org.senju.eshopeule.repository.jpa.ProductRepository;
import org.senju.eshopeule.repository.mongodb.RatingRepository;
import org.senju.eshopeule.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.senju.eshopeule.constant.exceptionMessage.CustomerExceptionMsg.CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.PRODUCT_NOT_FOUND_WITH_ID_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.RatingExceptionMsg.*;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper mapper;
    private final RatingRepository ratingRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(RatingService.class);

    @Override
    public RatingDTO getById(String id) {
        return mapper.convertToDTO(ratingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(RATING_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public RatingPagingResponse getRatingPageByProductId(String productId, Pageable pageRequest) {
        return getOrderPaging(ratingRepository.findAllByProductId(productId, pageRequest), mapper);
    }

    private RatingPagingResponse getOrderPaging(Page<Rating> orderPage, RatingMapper mapper) {
        return RatingPagingResponse.builder()
                .totalPages(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .isLast(orderPage.isLast())
                .pageNo(orderPage.getPageable().getPageNumber() + 1)
                .pageSize(orderPage.getPageable().getPageSize())
                .ratings(
                        orderPage.getContent().stream()
                                .map(mapper::convertToDTO)
                                .toList()
                )
                .build();
    }

    @Override
    @Transactional
    public RatingDTO createNewRating(RatingDTO dto) {
        if (!productRepository.existsById(dto.getProductId())) {
            throw new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, dto.getProductId()));
        }
        String username = this.getUsernameOfCurrentUser();
        Customer customer = customerRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG, username))
        );

        if (!orderItemRepository.checkOrderedProduct(dto.getProductId(), customer.getId())) {
            throw new RatingException(NOT_ALLOWED_TO_REVIEW_MSG);
        }

        if (ratingRepository.findByProductIdAndCustomerId(dto.getProductId(), customer.getId()).isPresent()) {
            throw new ObjectAlreadyExistsException(RATING_ALREADY_EXISTS_MSG);
        }

        Rating newRating = mapper.convertToEntity(dto);
        newRating.setId(null);
        newRating.setCustomerId(customer.getId());
        newRating.setCustomerName(customer.getFullName());
        return mapper.convertToDTO(ratingRepository.save(newRating));
    }

    @Override
    @Transactional
    public RatingDTO updateRating(RatingDTO dto) {
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new NotFoundException(RATING_NOT_FOUND_MSG);
        }
        String username = this.getUsernameOfCurrentUser();
        Customer customer = customerRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG, username))
        );

        Rating loadedRating = ratingRepository.findByProductIdAndCustomerId(dto.getProductId(), customer.getId()).orElseThrow(
                () -> new NotFoundException(RATING_NOT_FOUND_MSG)
        );

        if (!loadedRating.getId().equals(dto.getId())) {
            throw new NotFoundException(String.format(RATING_NOT_FOUND_WITH_ID_MSG, dto.getId()));
        }

        mapper.updateFromDTO(dto, loadedRating);
        return mapper.convertToDTO(ratingRepository.save(loadedRating));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        if (!ratingRepository.existsById(id)) {
            throw new NotFoundException(String.format(RATING_NOT_FOUND_WITH_ID_MSG, id));
        }
        ratingRepository.deleteById(id);
    }

    private String getUsernameOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
