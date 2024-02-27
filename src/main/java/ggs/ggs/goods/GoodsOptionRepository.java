package ggs.ggs.goods;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface GoodsOptionRepository extends JpaRepository<GoodsOption,Integer> {

    @Query("SELECT DISTINCT g.color FROM GoodsOption g WHERE g.goods = :goods")
    List<String> findDistinctColorsByGoodsIdx(@Param("goods") Goods goods);

    @Query("SELECT g.size FROM GoodsOption g WHERE g.goods = :goods and g.color= :color")
    List<String> findSizesByGoodsIdxandcolor(@Param("goods") Goods goods, @Param("color") String color);

    @Query("SELECT DISTINCT g.size FROM GoodsOption g WHERE g.goods = :goods")
    List<String> findDistinctSizesByGoodsIdx(@Param("goods") Goods goods);

    @Query("SELECT g FROM GoodsOption g WHERE g.goods = :goods and g.color = :color and g.size = :size")
    GoodsOption findByGoodsAndColorAndSize(@Param("goods") Goods goods, @Param("color") String color, @Param("size")String size);
}