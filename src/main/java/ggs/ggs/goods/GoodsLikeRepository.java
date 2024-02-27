package ggs.ggs.goods;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsLike;
import ggs.ggs.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface GoodsLikeRepository extends JpaRepository<GoodsLike,Integer> {
    boolean existsByGoodsIdxAndMemberIdx(Integer goodsIdx, Integer memberIdx);

    @Query("SELECT gl FROM GoodsLike gl LEFT JOIN FETCH gl.goods WHERE gl.member = :member")
    Page<GoodsLike> findGoodsLikesWithGoodsByMember(@Param("member") Member member, Pageable pageable);

    int countByGoods(Goods goods);

    void deleteByGoodsIdxAndMemberIdx(Integer goodsIdx, Integer memberIdx);
}

