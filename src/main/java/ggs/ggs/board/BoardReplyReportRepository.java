package ggs.ggs.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ggs.ggs.domain.Member;
import ggs.ggs.domain.Reply;
import ggs.ggs.domain.ReplyReport;

import java.util.List;


@Transactional
@Repository
public interface BoardReplyReportRepository extends JpaRepository<ReplyReport, Integer> {
    boolean existsByReplyIdxAndMemberIdx(Integer replyIdx, Integer memberIdx);

    @Query("SELECT rr FROM ReplyReport rr " +
            "JOIN FETCH rr.reply r " +
            "WHERE rr.member = :member")
    List<ReplyReport> findAllByMemberWithReply(Member member);

    @Query("SELECT rr FROM ReplyReport rr LEFT JOIN FETCH rr.reply WHERE rr.member = :member")
    Page<ReplyReport> findReplyReportsWithReplyByMember(@Param("member") Member member, Pageable pageable);

    int countByReply(Reply reply);

    void deleteByReplyIdxAndMemberIdx(Integer replyIdx, Integer memberIdx);
}