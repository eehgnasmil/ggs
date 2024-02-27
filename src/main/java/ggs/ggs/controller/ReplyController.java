package ggs.ggs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import ggs.ggs.domain.Member;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    private final MemberRepository memberRepository;

    // 저장, 수정 사용
    @PostMapping("/savereply")
    @ResponseBody
    public ResponseEntity<ReplyDto> saveReply(@RequestBody ReplyDto replyDto, Authentication authentication) {
        String id = authentication.getName();
        Optional<Member> uid = memberRepository.findByid(id);
        Integer memberId = uid.get().getIdx();
        replyDto.setMember(memberId);
        ReplyDto savedReplyDto = replyService.save(replyDto, memberId);
        return ResponseEntity.ok(savedReplyDto);
    }

    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<String> deleteReply(@PathVariable("idx") Long idx) {
        replyService.delete(idx);
        return ResponseEntity.ok("댓글 삭제가 완료되었습니다.");
    }

}
