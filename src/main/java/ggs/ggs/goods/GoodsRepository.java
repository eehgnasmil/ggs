package ggs.ggs.goods;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.Member;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {
    // 로그인 안 함!!!!
    @Query("SELECT g, COUNT(gl) as likeCount FROM Goods g LEFT JOIN GoodsLike gl ON g = gl.goods " +
            "WHERE (:category = 0 OR g.category = :category) " +
            "GROUP BY g ORDER BY likeCount DESC")
    List<Object[]> findTop10ByCategoryWithLikeCount(@Param("category") Integer category, Pageable pageable);

    @Query("SELECT g, COUNT(gl) as likeCnt, COUNT(o) as orderCnt FROM Goods g " +
            "LEFT JOIN GoodsLike gl ON g = gl.goods " +
            "LEFT JOIN OrderItem o ON g = o.goods " +
            "WHERE (:category = 0 OR g.category = :category) " +
            "GROUP BY g.idx " +
            "ORDER BY " +
            "CASE WHEN :orderBy = 'idx' THEN g.idx END DESC, " +
            "CASE WHEN :orderBy = 'highPrice' THEN g.sellingPrice END DESC, " +
            "CASE WHEN :orderBy = 'lowPrice' THEN g.sellingPrice END ASC, " +
            "CASE WHEN :orderBy = 'orderCnt' THEN COUNT(o) END DESC, " +
            "CASE WHEN :orderBy = 'likeCnt' THEN COUNT(gl) END DESC")
    Page<Object[]> findByCategoryWithCounts(@Param("category") int category,
                                            @Param("orderBy") String orderBy,
                                            Pageable pageable);

    // 로그인 함!!!!

    @Query("SELECT g, COUNT(gl) as likeCount, " +
            "CASE WHEN COUNT(gl2.idx) > 0 THEN true ELSE false END as likeTF " +
            "FROM Goods g " +
            "LEFT JOIN GoodsLike gl ON g = gl.goods " +
            "LEFT JOIN GoodsLike gl2 ON g = gl2.goods AND gl2.member = :member " +
            "WHERE (:category = 0 OR g.category = :category) " +
            "GROUP BY g  ORDER BY likeCount DESC")
    List<Object[]> findTop10ByCategoryWithLikeCount(@Param("category") Integer category, @Param("member") Member member, Pageable pageable);


    @Query("SELECT g, COUNT(gl) as likeCnt, COUNT(o) as orderCnt, " +
            "CASE WHEN COUNT(gl2.idx) > 0 THEN true ELSE false END as likeTF " +
            "FROM Goods g " +
            "LEFT JOIN GoodsLike gl ON g = gl.goods " +
            "LEFT JOIN OrderItem o ON g = o.goods " +
            "LEFT JOIN GoodsLike gl2 ON g = gl2.goods AND gl2.member = :member " +
            "WHERE (:category = 0 OR g.category = :category) " +
            "GROUP BY g.idx " +
            "ORDER BY " +
            "CASE WHEN :orderBy = 'idx' THEN g.idx END DESC, " +
            "CASE WHEN :orderBy = 'highPrice' THEN g.sellingPrice END DESC, " +
            "CASE WHEN :orderBy = 'lowPrice' THEN g.sellingPrice END ASC, " +
            "CASE WHEN :orderBy = 'orderCnt' THEN COUNT(o) END DESC, " +
            "CASE WHEN :orderBy = 'likeCnt' THEN COUNT(gl) END DESC")
    Page<Object[]> findByCategoryWithCounts(@Param("category") int category,
                                            @Param("orderBy") String orderBy, @Param("member") Member member, Pageable pageable);

    @Query("SELECT g, CASE WHEN COUNT(gl.idx) > 0 THEN true ELSE false END as likeTF " +
            "FROM Goods g " +
            "LEFT JOIN GoodsLike gl ON g = gl.goods AND gl.member = :member " +
            "WHERE g.idx = :idx")
    Tuple findByIdxWithBoolean(@Param("idx") Integer idx, @Param("member") Member member);


}
