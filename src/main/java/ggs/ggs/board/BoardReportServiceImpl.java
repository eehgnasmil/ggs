package ggs.ggs.board;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.BoardReport;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.BoardDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.ReportService;
import lombok.RequiredArgsConstructor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardReportServiceImpl implements ReportService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardReportRepository reportRepository;

    @Override
    public Boolean report(Integer idx, String sid) {
        Board board = this.boardRepository.findById(Long.valueOf(idx))
                .orElseThrow(() -> new NoSuchElementException("No Board with idx " + idx));
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Member member = optionalMember.get();

        BoardReport boardReport = BoardReport.builder()
                .board(board)
                .member(member)
                .build();

        Boolean exist = reportRepository.existsByBoardIdxAndMemberIdx(idx, member.getIdx());
        System.out.println(exist);
        if (!exist) {
            reportRepository.save(boardReport);
            board.setReportCount(board.getReportCount() + 1); // 신고 추가: reportCount 증가
        }
        boardRepository.save(board); // 변경된 reportCount 값을 데이터베이스에 저장

        exist = reportRepository.existsByBoardIdxAndMemberIdx(idx, member.getIdx());

        return exist;
    }

    @Override
    public int reportNum(Integer idx) {
        Board board = boardRepository.findById(Long.valueOf(idx)).get();
        int cnt = reportRepository.countByBoard(board);
        return cnt;
    }

    @Override
    public Page<Object> reportList(String sid, int page) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Pageable pageable = PageRequest.of(page, 5);
        Member member = optionalMember.get();

        Page<BoardReport> boardReports = reportRepository.findBoardReportsWithBoardByMember(member, pageable);

        return boardReports.map(boardReport -> {
            BoardDto boardDto = new BoardDto(boardReport.getBoard());

            String html = boardReport.getBoard().getDetail();
            Document doc = Jsoup.parse(html);
            Elements imgElements = doc.select("img");

            for (Element imgElement : imgElements) {
                String imageUrl = imgElement.attr("src");
                boardDto.getImageUrls().add(imageUrl);
            }

            return boardDto;
        });
    }
}
