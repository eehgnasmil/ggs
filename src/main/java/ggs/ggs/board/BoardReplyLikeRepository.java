package ggs.ggs.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.domain.Pageable;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.Reply;
import ggs.ggs.domain.ReplyLike;

@Transactional
@Repository
public interface BoardReplyLikeRepository extends JpaRepository<ReplyLike, Integer> {
    boolean existsByReplyIdxAndMemberIdx(Integer replyIdx, Integer memberIdx);

    @Query("SELECT rl FROM ReplyLike rl " +
            "JOIN FETCH rl.reply r " +
            "WHERE rl.member = :member")
    List<ReplyLike> findAllByMemberWithReply(Member member);

    @Query("SELECT rl FROM ReplyLike rl LEFT JOIN FETCH rl.reply WHERE rl.member = :member")
    Page<ReplyLike> findReplyLikesWithReplyByMember(@Param("member") Member member, Pageable pageable);

    int countByReply(Reply reply);

    void deleteByReplyIdxAndMemberIdx(Integer replyIdx, Integer memberIdx);
}