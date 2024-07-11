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
import org.senju.eshopeule.repository.projection.CartItemView;
import org.senju.eshopeule.repository.projection.ProductPriceView;
import org.senju.eshopeule.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.exceptionMessage.CartExceptionMsg.CART_ITEM_OPTION_REQUIRED_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.CartExceptionMsg.ONLY_ONE_ACTIVE_CART_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.CustomerExceptionMsg.CUSTOMER_NOT_FOUND_WITH_USERNAME_MSG;
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
        if (!productRepository.existsById(itemDTO.getProduct().getId())) {
            throw new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, itemDTO.getProduct().getId()));
        }

        if (optionRepository.checkHasOptionsWithProductId(itemDTO.getProduct().getId())) {
            if (itemDTO.getOption() == null || itemDTO.getOption().getId() == null || itemDTO.getOption().getId().isBlank()) {
                throw new NotFoundException(PROD_OPTION_NOT_FOUND_MSG);
            }
            if (!optionRepository.existsById(itemDTO.getOption().getId())) {
                throw new NotFoundException(String.format(PROD_OPTION_NOT_FOUND_WITH_ID_MSG, itemDTO.getOption().getId()));
            }
        }

        final Cart activeCart = this.getLatestCartOfCurrentUser();

        if (activeCart.getId() == null) {
            CartItem newItem = cartItemMapper.convertToEntity(itemDTO);
            activeCart.setItems(List.of(newItem));
            activeCart.setTotal(this.calculateTotal(itemDTO));
            newItem.setCart(activeCart);
        } else {
            final CartItemQuantityView existedItemQuantity;

            if (itemDTO.getOption() != null) {
                existedItemQuantity = cartItemRepository
                        .getItemQuantity(activeCart.getId(), itemDTO.getProduct().getId(), itemDTO.getOption().getId())
                        .orElse(null);
            } else {
                existedItemQuantity = cartItemRepository
                        .getItemQuantity(activeCart.getId(), itemDTO.getProduct().getId())
                        .orElse(null);
            }

            if (existedItemQuantity != null) {
                cartItemRepository.updateQuantityById(
                        existedItemQuantity.getId(),
                        existedItemQuantity.getQuantity() + itemDTO.getQuantity());
            } else {
                CartItem newItem = cartItemMapper.convertToEntity(itemDTO);
                activeCart.getItems().add(newItem);
                newItem.setCart(activeCart);
                cartItemRepository.save(newItem);
            }

            activeCart.setTotal(activeCart.getTotal() + calculateTotal(itemDTO));
        }

        return cartMapper.convertToDTO(cartRepository.save(activeCart));
    }

    @Override
    @Transactional
    public CartDTO addToCart(List<CartItemDTO> itemDTOList) {
        if (itemDTOList == null) throw new CartException("Cart item must not be null");
        if (itemDTOList.isEmpty()) throw new CartException("Cart item must not be empty");

        List<String> productIdList = itemDTOList.stream()
                .map(i -> {
                    if (i.getProduct() == null || i.getProduct().getId() == null || i.getProduct().getId().isBlank()) {
                        throw new NotFoundException(PRODUCT_NOT_FOUND_MSG);
                    }
                    return i.getProduct().getId();
                })
                .toList();

        List<ProductPriceView> priceViewList = productRepository.getPriceViewByIds(productIdList);

        Set<String> productIdSet = new HashSet<>(productIdList);
        Set<String> loadedProductId = priceViewList.stream().map(ProductPriceView::getId).collect(Collectors.toSet());

        if (loadedProductId.size() != productIdSet.size() || !loadedProductId.containsAll(productIdSet)) {
            throw new  NotFoundException(PRODUCT_NOT_FOUND_MSG);
        }

        List<CartItemDTO> noOptionItemList = new ArrayList<>();
        List<CartItemDTO> hasOptionItemList = new ArrayList<>();
        itemDTOList.forEach(i -> {
            if (i.getOption() != null && i.getOption().getId() != null && !i.getOption().getId().isBlank()) {
                hasOptionItemList.add(i);
            } else noOptionItemList.add(i);
        });

        List<String> optionIdList = hasOptionItemList.stream().map(i -> i.getOption().getId()).toList();
        if (optionIdList.size() != optionRepository.countDistinctByIds(optionIdList)) {
            throw new NotFoundException(PROD_OPTION_NOT_FOUND_MSG);
        }

        noOptionItemList.forEach(i -> {
            if (optionRepository.checkHasOptionsWithProductId(i.getProduct().getId())) {
                throw new CartException(String.format(CART_ITEM_OPTION_REQUIRED_MSG, i.getProduct().getId()));
            }
        });

        final Map<String, ProductPriceView> priceMap = priceViewList.stream()
                .collect(Collectors.toMap(ProductPriceView::getId, Function.identity()));

        final Cart activeCart = this.getLatestCartOfCurrentUser();
        activeCart.setTotal(this.calculateTotal(itemDTOList, priceMap));

        List<CartItem> newItems = itemDTOList.stream()
                .map(dto -> {
                    CartItem ci = cartItemMapper.convertToEntity(dto);
                    ci.setCart(activeCart);
                    return ci;
                }).toList();

        if (activeCart.getId() == null) {
            activeCart.setItems(newItems);
        } else {
            final List<CartItemView> itemViewList = cartItemRepository.getAllItemViewWithCategoryId(activeCart.getId());
            if (itemViewList.isEmpty()) {
                activeCart.setItems(newItems);
                cartItemRepository.saveAll(newItems);
            } else {
                itemViewList.forEach(
                        iv -> {
                            if (iv.getOptionId() == null) {
                                for (var itemDTO: noOptionItemList) {
                                    if (iv.getProductId().equals(itemDTO.getProduct().getId())) {
                                        cartItemRepository.updateQuantityById(iv.getId(), iv.getQuantity() + itemDTO.getQuantity());
                                    }
                                }
                            } else {
                                for (var itemDTO: hasOptionItemList) {
                                    if (iv.getProductId().equals(itemDTO.getProduct().getId()) && iv.getOptionId().equals(itemDTO.getOption().getId())) {
                                        cartItemRepository.updateQuantityById(iv.getId(), iv.getQuantity() + itemDTO.getQuantity());
                                    }
                                }
                            }
                        }
                );
            }
        }

        return cartMapper.convertToDTO(cartRepository.save(activeCart));
    }

    @Override
    public void deleteCartItemByProductId(String productId) {
        final Cart activeCart = this.getLatestCartOfCurrentUser();
        if (activeCart.getId() == null) return;
        cartItemRepository.deleteByCartIdAndProductId(activeCart.getId(), productId);
    }

    @Override
    public void deleteCartItemByOptionId(String optionId) {
        final Cart activeCart = this.getLatestCartOfCurrentUser();
        if (activeCart.getId() == null) return;
        cartItemRepository.deleteByCartIdAndOptionId(activeCart.getId(), optionId);
    }

    private Double calculateTotal(CartItemDTO item) {
        ProductPriceView priceView = productRepository.getPriceViewByIds(List.of(item.getProduct().getId())).get(0);
        if (priceView.getDiscount() != null) {
            return priceView.getPrice() * (1.0 - priceView.getDiscount() / 100.0) * item.getQuantity();
        }
        return priceView.getPrice() * item.getQuantity();
    }

    private Double calculateTotal(List<CartItemDTO> items, Map<String, ProductPriceView> priceMap) {
        return items.stream()
                .map(ci -> {
                    final double finalPrice;
                    ProductPriceView priceView = priceMap.get(ci.getProduct().getId());
                    if (priceView.getDiscount() != null) {
                        finalPrice = priceView.getPrice() * (1.0 - priceView.getDiscount() / 100.0) * ci.getQuantity();
                    } else {
                        finalPrice = priceView.getPrice() * ci.getQuantity();
                    }
                    return finalPrice;
                })
                .reduce(Double::sum)
                .orElseThrow(CartException::new);
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
