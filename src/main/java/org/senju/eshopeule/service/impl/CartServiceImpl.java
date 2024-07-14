package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.CartDTO;
import org.senju.eshopeule.dto.CartItemDTO;
import org.senju.eshopeule.exceptions.CartException;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.mappers.CartItemMapper;
import org.senju.eshopeule.mappers.CartMapper;
import org.senju.eshopeule.model.cart.Cart;
import org.senju.eshopeule.model.cart.CartItem;
import org.senju.eshopeule.model.cart.CartStatus;
import org.senju.eshopeule.model.user.Customer;
import org.senju.eshopeule.repository.jpa.*;
import org.senju.eshopeule.repository.projection.CartItemQuantityView;
import org.senju.eshopeule.repository.projection.ProductQuantityView;
import org.senju.eshopeule.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.senju.eshopeule.constant.exceptionMessage.CartExceptionMsg.*;
import static org.senju.eshopeule.constant.exceptionMessage.CustomerExceptionMsg.CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.OrderExceptionMsg.NOT_ALLOWED_TO_ORDER;
import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository optionRepository;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);


    @Override
    public CartDTO getCartOfCurrentUser() {
        return cartMapper.convertToDTO(this.getLatestCartOfCurrentUser());
    }

    @Override
    @Transactional
    public CartDTO addToCart(CartItemDTO itemDTO) {
        if (itemDTO.getProduct() == null || itemDTO.getProduct().getId() == null || itemDTO.getProduct().getId().isBlank()) {
            throw new NotFoundException(PRODUCT_NOT_FOUND_MSG);
        }
        if (!productRepository.checkAllowedToOrder(itemDTO.getProduct().getId())) {
            throw new CartException(String.format(NOT_ALLOWED_TO_ORDER, itemDTO.getProduct().getId()));
        }

        if (optionRepository.checkHasOptionsWithProductId(itemDTO.getProduct().getId())) {
            if (itemDTO.getOption() == null || itemDTO.getOption().getId() == null || itemDTO.getOption().getId().isBlank()) {
                throw new NotFoundException(PROD_OPTION_NOT_FOUND_MSG);
            }
            if (!optionRepository.existsById(itemDTO.getOption().getId())) {
                throw new NotFoundException(String.format(PROD_OPTION_NOT_FOUND_WITH_ID_MSG, itemDTO.getOption().getId()));
            }
        } else {
            itemDTO.setOption(null);
        }

        final ProductQuantityView prodQuantityView = productRepository.getQuantityViewById(itemDTO.getProduct().getId());
        if (itemDTO.getQuantity() > prodQuantityView.getQuantity()) {
            throw new CartException(QUANTITY_EXCEEDED_MSG);
        }

        final Cart activeCart = this.getLatestCartOfCurrentUser();

        if (activeCart.getId() == null) {
            CartItem newItem = cartItemMapper.convertToEntity(itemDTO);
            activeCart.setItems(List.of(newItem));
            newItem.setCart(activeCart);
        } else {
            final CartItemQuantityView existedItemQuantity;

//            if (itemDTO.getOption() != null) {
//                existedItemQuantity = cartItemRepository
//                        .getItemQuantityView(activeCart.getId(), itemDTO.getProduct().getId(), itemDTO.getOption().getId())
//                        .orElse(null);
//            } else {
//                existedItemQuantity = cartItemRepository
//                        .getItemQuantityView(activeCart.getId(), itemDTO.getProduct().getId())
//                        .orElse(null);
//            }

            existedItemQuantity = cartItemRepository
                    .getItemQuantityView(activeCart.getId(), itemDTO.getProduct().getId(), itemDTO.getOption().getId())
                    .orElse(null);

            if (existedItemQuantity != null) {
                if (existedItemQuantity.getQuantity() + itemDTO.getQuantity() > prodQuantityView.getQuantity()) {
                    throw new CartException(QUANTITY_EXCEEDED_MSG);
                }
                cartItemRepository.updateQuantityById(
                        existedItemQuantity.getId(),
                        existedItemQuantity.getQuantity() + itemDTO.getQuantity());
            } else {
                CartItem newItem = cartItemMapper.convertToEntity(itemDTO);
                activeCart.getItems().add(newItem);
                newItem.setCart(activeCart);
                cartItemRepository.save(newItem);
            }
        }

        return cartMapper.convertToDTO(cartRepository.save(activeCart));
    }


    @Override
    @Transactional
    public void deleteCartItemByProductId(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, productId));
        }
        final Cart activeCart = this.getLatestCartOfCurrentUser();
        if (activeCart.getId() == null) return;
        cartItemRepository.deleteByCartIdAndProductId(activeCart.getId(), productId);
    }

    @Override
    @Transactional
    public void deleteCartItemByOptionId(String optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new NotFoundException(String.format(PROD_OPTION_NOT_FOUND_WITH_ID_MSG, optionId));
        }
        final Cart activeCart = this.getLatestCartOfCurrentUser();
        if (activeCart.getId() == null) return;

        cartItemRepository.deleteByCartIdAndOptionId(activeCart.getId(), optionId);
    }


    private Cart getLatestCartOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username == null || username.isBlank()) throw new CartException("Username is invalid");

        List<Cart> activeCartList = cartRepository.getActiveCartByUsername(username);
        if (activeCartList.size() > 1) throw new CartException(ONLY_ONE_ACTIVE_CART_MSG);

        if (activeCartList.isEmpty()) {
            final String customerId =  customerRepository.findIdByUsername(username).orElseThrow(
                    () -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG, username))
            );

            return Cart.builder()
                    .status(CartStatus.ACTIVE)
                    .customer(Customer.builder().id(customerId).build())
                    .build();
        }

        return activeCartList.stream().findFirst().get();
    }
}
