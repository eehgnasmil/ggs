package ggs.ggs.board;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ggs.ggs.domain.Member;
import ggs.ggs.domain.Reply;
import ggs.ggs.domain.ReplyReport;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.ReportService;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardReplyReportServiceImpl implements ReportService {
    private final BoardReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardReplyReportRepository reportRepository;

    @Override
    public Boolean report(Integer idx, String sid) {
        Reply reply = this.replyRepository.findById(Long.valueOf(idx))
                .orElseThrow(() -> new NoSuchElementException("No Reply with idx " + idx));
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Member member = optionalMember.get();

        ReplyReport replyReport = ReplyReport.builder()
                .reply(reply)
                .member(member)
                .build();

        Boolean exist = reportRepository.existsByReplyIdxAndMemberIdx(idx, member.getIdx());
        if (!exist) {
            reportRepository.save(replyReport);
            reply.setReportCount(reply.getReportCount() + 1); // 신고 추가: reportCount 증가
        }
        replyRepository.save(reply); // 변경된 reportCount 값을 데이터베이스에 저장

        exist = reportRepository.existsByReplyIdxAndMemberIdx(idx, member.getIdx());

        return exist;
    }

    @Override
    public int reportNum(Integer idx) {
        Reply reply = replyRepository.findById(Long.valueOf(idx)).get();
        int cnt = reportRepository.countByReply(reply);
        return cnt;
    }

    @Override
    public Page<Object> reportList(String sid, int page) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Pageable pageable = PageRequest.of(page, 5);
        Member member = optionalMember.get();

        Page<ReplyReport> replyReports = reportRepository.findReplyReportsWithReplyByMember(member, pageable);

        return replyReports.map(replyReport -> {
            ReplyDto replyDto = ReplyDto.from(replyReport.getReply());
            return replyDto;
        });
    }
}
