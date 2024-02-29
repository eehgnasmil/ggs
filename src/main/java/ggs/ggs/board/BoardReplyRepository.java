package ggs.ggs.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.Reply;

import java.util.*;

@Repository
public interface BoardReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByBoardOrderByParentIdxAscIdxAsc(Board board);

    List<Reply> findByMember(Member member);

}
