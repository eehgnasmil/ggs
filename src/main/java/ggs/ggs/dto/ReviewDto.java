package ggs.ggs.dto;

import ggs.ggs.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Integer idx;
    private CartItemDto cartItemDto;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String content;
    private Integer star; // 1~5
    private List<FileDto> fileDtos;

    public ReviewDto(CartItemDto cartItemDto) {
        this.cartItemDto = cartItemDto;
    }
}
