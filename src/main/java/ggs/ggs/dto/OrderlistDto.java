package ggs.ggs.dto;

import ggs.ggs.domain.Order;
import ggs.ggs.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderlistDto {
    private Integer idx;
    private String orderNum;
    private MemberDto member;
    private List<CartItemDto> cartItems;
    private LocalDateTime created_date;
    private Integer state;
    private Integer price;
    private Integer delivery_price;
    private Integer usePoint;
    private Integer givePoint;

    public OrderlistDto(Order order) {
        this.idx = order.getIdx();
        this.orderNum = order.getOrderNum();
        this.member = new MemberDto(order.getMember());
        this.cartItems = order.getOrderItems().stream()
                .map(orderItem -> new CartItemDto(orderItem))
                .collect(Collectors.toList());
        this.state = order.getState();
        this.created_date = order.getCreated_date();
        this.price = order.getPrice();
        this.delivery_price = order.getDelivery_price();
        this.usePoint = this.getUsePoint();
        this.givePoint = this.getGivePoint();

    }
}
