package ggs.ggs.dto;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOptionDto {

    private Integer idx;
    private String color;
    private String size;
    private String status;
    private Goods goods;

    public GoodsOptionDto(GoodsOption option) {
        this.idx = option.getIdx();
        this.color = option.getColor();
        this.size = option.getSize();
    }
}
