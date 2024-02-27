package ggs.ggs.board;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Hashtag;
import ggs.ggs.domain.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByCategory(String category, Pageable pageable);

    List<Board> findByCategory(String category);

    List<Board> findByMember(Member member);

    Page<Board> findByMember(Member member, Pageable pageable);

    List<Board> findTop10ByOrderByModifiedDateDesc();

    List<Board> findTop10ByOrderByViewcountDesc();

    List<Board> findTop10ByOrderByLikesCountDesc();

    @Query("SELECT mt.hashtag FROM MiddleTag mt WHERE mt.board.idx = :boardIdx")
    List<Hashtag> findHashtagsByBoardIdx(@Param("boardIdx") Long idx);

}
