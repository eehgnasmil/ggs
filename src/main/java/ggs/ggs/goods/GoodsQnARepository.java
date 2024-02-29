package ggs.ggs.goods;

import ggs.ggs.domain.GoodsQnA;
import ggs.ggs.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsQnARepository extends JpaRepository<GoodsQnA,Integer> {

    Page<GoodsQnA> findAllByMemberOrderByIdxDesc(Pageable pageable, Member member);


}
