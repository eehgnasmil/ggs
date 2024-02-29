package ggs.ggs.controller;

import ggs.ggs.service.ReportService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final @Qualifier("memberReportServiceImpl") ReportService memberReportService;
    private final @Qualifier("boardReportServiceImpl") ReportService boardReportService;
    private final @Qualifier("boardReplyReportServiceImpl") ReportService replyReportService;

    public ReportController(
            @Qualifier("memberReportServiceImpl") ReportService memberReportService,
            @Qualifier("boardReportServiceImpl") ReportService boardReportService,
            @Qualifier("boardReplyReportServiceImpl") ReportService replyReportService) {
        this.memberReportService = memberReportService;
        this.boardReportService = boardReportService;
        this.replyReportService = replyReportService;
    }
    
    @PostMapping("/boardReport")
    public Boolean boardReport(@RequestParam("board") Integer board, Authentication authentication) {
        String sid = authentication.getName();
        Boolean boardReport = boardReportService.report(board, sid);
        return boardReport;
    }

    @PostMapping("/replyReport")
    public Boolean replyReport(@RequestParam("reply") Integer reply, Authentication authentication) {
        String sid = authentication.getName();
        Boolean replyReport = replyReportService.report(reply, sid);
        return replyReport;
    }
}
