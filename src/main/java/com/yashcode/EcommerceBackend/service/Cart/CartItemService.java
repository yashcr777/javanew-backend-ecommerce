package com.yashcode.EcommerceBackend.service.Cart;

import com.yashcode.EcommerceBackend.Repository.CartItemRepository;
import com.yashcode.EcommerceBackend.Repository.CartRepository;
import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.CartItem;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.entity.Products;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.ProductClient.ProductClient;
import com.yashcode.EcommerceBackend.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final CartRepository cartRepository;
    private final ProductClient pro;
    @Override
    public void addItemToCart(Long id, Long productId, int quantity) {
        Cart cart=cartService.getCart(id);
        Products product=pro.getProductById(productId);
        System.out.println(product);
        CartItem cartItem=cart.getCartItems()
                .stream()
                .filter(item->item.getProductId().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId()==null){
            cartItem.setCart(cart);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart=cartService.getCart(cartId);
        CartItem itemToRemove =getCartItems(cartId,productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        Products products=pro.getProductById(productId);
        System.out.println(cart);
        cart.getCartItems()
                .stream()
                .filter(item->item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(products.getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount=cart.getCartItems()
                .stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItems(Long cartId,Long productId){
        Cart cart=cartService.getCart(cartId);
        return cart.getCartItems()
                .stream()
                .filter(item->item.getProductId().equals(productId))
                .findFirst().orElseThrow(()->new ResourceNotFoundException("Item not found exception"));
    }
}
