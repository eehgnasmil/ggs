package ggs.ggs.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderDto {
    MemberDto member;
    private LocalDateTime order_date;
    List<CartItemDto> cartItemDtos;

    public OrderDto(Member member, List<CartItemDto> nCartItemDtos) {
        this.member = new MemberDto(member);
        this.cartItemDtos = nCartItemDtos;
    }

}
