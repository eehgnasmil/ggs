package ggs.ggs.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.BoardReport;
import ggs.ggs.domain.Member;

import java.util.List;

@Transactional
@Repository
public interface BoardReportRepository extends JpaRepository<BoardReport, Integer> {
    boolean existsByBoardIdxAndMemberIdx(Integer boardIdx, Integer memberIdx);

    @Query("SELECT br FROM BoardReport br " +
            "JOIN FETCH br.board b " +
            "WHERE br.member = :member")
    List<BoardReport> findAllByMemberWithBoard(Member member);

    @Query("SELECT br FROM BoardReport br LEFT JOIN FETCH br.board WHERE br.member = :member")
    Page<BoardReport> findBoardReportsWithBoardByMember(@Param("member") Member member, Pageable pageable);

    int countByBoard(Board board);

    void deleteByBoardIdxAndMemberIdx(Integer boardIdx, Integer memberIdx);
}