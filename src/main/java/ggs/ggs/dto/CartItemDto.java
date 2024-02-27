package ggs.ggs.dto;

import ggs.ggs.domain.CartItem;
import ggs.ggs.domain.Goods;
import ggs.ggs.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Integer idx;
    private Integer cnt;
    private String color;
    private String size;
    private GoodsDto goodsDto;
    private Integer cartNum;

    public CartItemDto(CartItem cartItem) {
        this.idx = cartItem.getIdx();
        this.cnt = cartItem.getCnt();
        this.color = cartItem.getGoodsOption().getColor();
        this.size = cartItem.getGoodsOption().getSize();
        this.goodsDto = new GoodsDto(cartItem.getGoodsOption().getGoods());
    }
    public CartItemDto(OrderItem orderItem){
        this.idx = orderItem.getIdx();
        this.cnt = orderItem.getCnt();
        this.color = orderItem.getColor();
        this.size = orderItem.getSize();
        if (orderItem.getGoods() != null) {
            this.goodsDto = new GoodsDto(orderItem.getGoods());
        } else {
            this.goodsDto = new GoodsDto();
        }
    }
}
