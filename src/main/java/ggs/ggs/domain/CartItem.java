package ggs.ggs.domain;

import ggs.ggs.dto.CartDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart", referencedColumnName = "idx")
    private Cart cart;

    @Column
    private Integer cnt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goodsOption", referencedColumnName = "idx")
    private GoodsOption goodsOption;


    public CartItem(Cart cart, GoodsOption goodsOption, Integer cnt){
        this.cart = cart;
        this.goodsOption = goodsOption;
        this.cnt = cnt;
    }

    public static CartItem update(CartItem cartItem, CartDto cartDto){
        CartItem nCartItem = CartItem.builder()
                .idx(cartItem.getIdx())
                .cart(cartItem.getCart())
                .cnt(cartItem.getCnt() + cartDto.getCnt())
                .goodsOption(cartItem.getGoodsOption())
                .build();
        return nCartItem;
    }

    public static CartItem directUpdate(CartItem cartItem, CartDto cartDto){
        CartItem nCartItem = CartItem.builder()
                .idx(cartItem.getIdx())
                .cart(cartItem.getCart())
                .cnt(cartDto.getCnt())
                .goodsOption(cartItem.getGoodsOption())
                .build();
        return nCartItem;
    }
    @PreRemove
    private void preRemove() {
        if (cart != null) {
            cart.removeItemAndUpdateCount(this);
        }
    }
}
