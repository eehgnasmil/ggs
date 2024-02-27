package ggs.ggs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Integer goodsIdx;
    private String color;
    private String size;
    private Integer cnt;
}
