package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.CartDTO;
import org.senju.eshopeule.dto.CartItemDTO;

import java.util.List;

public interface CartService {

    CartDTO getCartOfCurrentUser();

    CartDTO addToCart(CartItemDTO item);

    void deleteCartItemByProductId(String productId);

    void deleteCartItemByOptionId(String optionId);

}
