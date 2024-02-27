package ggs.ggs.dto;

import ggs.ggs.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private List<Integer> products;
    private String orderNum;
    private Integer price;
    private Integer point;
    private Integer delivery;

}
