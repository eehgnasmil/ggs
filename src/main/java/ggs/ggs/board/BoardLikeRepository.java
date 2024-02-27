package ggs.ggs.board;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.BoardLike;
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
public interface BoardLikeRepository extends JpaRepository<BoardLike, Integer> {
    boolean existsByBoardIdxAndMemberIdx(Integer boardIdx, Integer memberIdx);

    @Query("SELECT bl FROM BoardLike bl " +
            "JOIN FETCH bl.board b " +
            "WHERE bl.member = :member")
    List<BoardLike> findAllByMemberWithBoard(Member member);

    @Query("SELECT bl FROM BoardLike bl LEFT JOIN FETCH bl.board WHERE bl.member = :member")
    Page<BoardLike> findBoardLikesWithBoardByMember(@Param("member") Member member, Pageable pageable);

    int countByBoard(Board board);

    void deleteByBoardIdxAndMemberIdx(Integer boardIdx, Integer memberIdx);
}
