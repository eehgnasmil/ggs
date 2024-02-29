package ggs.ggs.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Hashtag;
import ggs.ggs.domain.MiddleTag;
import java.util.*;

@Repository
public interface BoardMiddleTagRepository extends JpaRepository<MiddleTag, Long> {

    List<MiddleTag> findByBoard(Board board);

    List<MiddleTag> findByHashtag(Hashtag hashtag, Pageable pageable);

    Page<MiddleTag> findByHashtagOrderByBoardModifiedDateDesc(Hashtag hashtag, Pageable pageable);

    @Query("SELECT m.hashtag.hashtag, COUNT(m) FROM MiddleTag m GROUP BY m.hashtag.hashtag ORDER BY COUNT(m) DESC")
    List<Object[]> findTopHashtags(Pageable pageable);

}
