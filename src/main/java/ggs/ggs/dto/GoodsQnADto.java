package ggs.ggs.dto;

import ggs.ggs.domain.GoodsQnA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsQnADto {
    private Integer idx;
    private Integer goodsIdx;
    private GoodsDto goodsDto;
    private String memberId;
    private Integer category;
    private String title;
    private String question;
    private String answer;
    private LocalDateTime questionDate;
    private LocalDateTime answerDate;
    private Integer state;

    public GoodsQnADto(GoodsQnA goodsQnA) {
        this.idx = goodsQnA.getIdx();
        this.goodsDto = new GoodsDto(goodsQnA.getGoods());
        this.memberId = goodsQnA.getMember().getId();
        this.category = goodsQnA.getCategory();
        this.title = goodsQnA.getTitle();
        this.question = goodsQnA.getQuestion();
        this.answer = goodsQnA.getAnswer();
        this.questionDate = goodsQnA.getQuestionDate();
        this.answerDate = goodsQnA.getAnswerDate();
        this.state = goodsQnA.getState();
    }



}
