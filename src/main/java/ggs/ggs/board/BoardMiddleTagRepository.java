package ggs.ggs.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
    // List<MiddleTag> findByHashtagOrderByBoardModifiedDateDesc(Hashtag hashtag, Pageable pageable);
    
    // List<MiddleTag> findByHashtag(Hashtag hashtag);
    // Page<MiddleTag> findByHashtag(Hashtag hashtag, Pageable pageable);

    // @Query(value = "SELECT hashtag_idx, COUNT(*) as count FROM middle_tag GROUP BY hashtag_idx ORDER BY count DESC LIMIT 10", nativeQuery = true)
    // List<Object[]> findTop10Hashtags();

}
