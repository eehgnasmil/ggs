package ggs.ggs.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Goods;
import ggs.ggs.domain.Reply;
import ggs.ggs.dto.ReplyDto;

import java.util.*;

@Repository
public interface BoardReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByBoardOrderByParentIdxAscIdxAsc(Board board);


}
