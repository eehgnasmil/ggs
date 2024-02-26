package ggs.ggs.goods;

import com.fasterxml.jackson.core.JsonProcessingException;
import ggs.ggs.dto.GoodsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface GoodsService {
    List<GoodsDto> findAll(int category);
    List<GoodsDto> findAll(Integer category, String name);

    Page<GoodsDto> findAll(int sortValue, int page, int category);
    Page<GoodsDto> findAll(Integer sortValue, int page, Integer category, String name);

    GoodsDto getGoods(Integer idx);
    GoodsDto getGoods(Integer idx, String name);

    List<String> selectSizes(Integer goodsIdx, String color);
    Integer insert(GoodsDto goodsDto) throws IOException;
    Integer update(GoodsDto goodsDto) throws IOException;
    void delete(Integer idx);


}
