package ggs.ggs.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.MiddleTag;
import java.util.*;

@Repository
public interface BoardMiddleTagRepository extends JpaRepository<MiddleTag, Long>{
    
    List<MiddleTag> findByBoard(Board board);
    
}
