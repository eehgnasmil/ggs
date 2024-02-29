package ggs.ggs.goods;

import ggs.ggs.dto.GoodsQnADto;
import ggs.ggs.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


public interface GoodsCSService {
    void save(GoodsQnADto goodsQnADto);

    Page<GoodsQnADto> findbyGoodsQnA(String id, int page);

    ReviewDto findbyOrderItem(Integer idx);

	Page<GoodsQnADto> findbyGoodsQnA(int page);
}
