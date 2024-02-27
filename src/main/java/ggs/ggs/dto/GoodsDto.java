package ggs.ggs.dto;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsLike;
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
public class GoodsDto {

    private Integer idx;
    private Integer category;
    private String name;
    private String summary;
    private String detail;
    private Integer sellingPrice;
    private Integer discountPrice;
    private LocalDateTime created_date;
    private LocalDateTime modified_date;
    private List<FileDto> fileDtos;
    private List<GoodsOptionDto> goodsOptionDtos;

    private List<String> colors;
    private List<String> sizes;
    private Integer likeCnt;
    private Integer orderCnt;
    private boolean likeTF;

    public GoodsDto(Goods goods){
            this.idx = goods.getIdx();
            this.category = goods.getCategory();
            this.name = goods.getName();
            this.summary = goods.getSummary();
            this.detail = goods.getDetail();
            this.sellingPrice = goods.getSellingPrice();
            this.discountPrice = goods.getDiscountPrice();
            this.created_date = goods.getCreated_date();
            this.modified_date = goods.getModified_date();


            this.goodsOptionDtos = goods.getGoodsOptions().stream()
                    .map(option -> new GoodsOptionDto(option))
                    .collect(Collectors.toList());
            this.fileDtos = goods.getFiles().stream()
                    .map(file -> new FileDto(file))
                    .collect(Collectors.toList());
    }

    public GoodsDto(Goods goods, int likeCnt, int orderCnt) {
            this.idx = goods.getIdx();
            this.category = goods.getCategory();
            this.name = goods.getName();
            this.summary = goods.getSummary();
            this.detail = goods.getDetail();
            this.sellingPrice = goods.getSellingPrice();
            this.discountPrice = goods.getDiscountPrice();
            this.created_date = goods.getCreated_date();
            this.modified_date = goods.getModified_date();
            this.likeCnt = likeCnt;
            this.orderCnt = orderCnt;

            this.goodsOptionDtos = goods.getGoodsOptions().stream()
                    .map(option -> new GoodsOptionDto(option))
                    .collect(Collectors.toList());
            this.fileDtos = goods.getFiles().stream()
                    .map(file -> new FileDto(file))
                    .collect(Collectors.toList());

    }

    public GoodsDto(Goods goods, int likeCnt, int orderCnt, boolean likeTF) {
        this.idx = goods.getIdx();
        this.category = goods.getCategory();
        this.name = goods.getName();
        this.summary = goods.getSummary();
        this.detail = goods.getDetail();
        this.sellingPrice = goods.getSellingPrice();
        this.discountPrice = goods.getDiscountPrice();
        this.created_date = goods.getCreated_date();
        this.modified_date = goods.getModified_date();
        this.likeCnt = likeCnt;
        this.orderCnt = orderCnt;
        this.likeTF = likeTF;

        this.goodsOptionDtos = goods.getGoodsOptions().stream()
                .map(option -> new GoodsOptionDto(option))
                .collect(Collectors.toList());
        this.fileDtos = goods.getFiles().stream()
                .map(file -> new FileDto(file))
                .collect(Collectors.toList());
    }
}
