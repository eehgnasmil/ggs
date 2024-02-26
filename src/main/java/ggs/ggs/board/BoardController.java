package ggs.ggs.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Hashtag;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.BoardDto;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.HashtagService;
import ggs.ggs.service.ReplyService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final ReplyService replyService;
    @Qualifier("boardTagServiceImpl")
    private final HashtagService hashtagService;

    @GetMapping("/{idx}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long idx) {
        BoardDto boardDTO = boardService.getBoardById(idx);
        return ResponseEntity.ok().body(boardDTO);
    }

    @GetMapping("/board_write")
    public String boardwrite(Model model, Authentication authentication) {
        String id = authentication.getName(); // 로그인한 사용자의 id 가져오기
        Member member = memberRepository.findByid(id)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with ID: " + id));
        String nickname = member.getNick();
        model.addAttribute("nickname", nickname);
        model.addAttribute("dto", new BoardDto());
        return "board/board_write";
    }

    @PostMapping("/board_write")
    @ResponseBody
    public ResponseEntity<BoardDto> boardWrite(@RequestBody BoardDto dto, Authentication authentication) {
        String id = authentication.getName();

        BoardDto createdBoardDto = boardService.createBoard(dto, id);

        System.out.println(dto.getHashtags() + "asdgasdgasd");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoardDto);
    }

    // @GetMapping("/board_list")
    // public String boardList(@RequestParam(value = "category", defaultValue =
    // "all") String category,
    // @RequestParam(value = "bsort", defaultValue = "new") String bsort,
    // Model model) {

    // Sort sort;
    // if ("new".equals(bsort)) {
    // sort = Sort.by("modifiedDate").descending();
    // } else if ("view".equals(bsort)) {
    // sort = Sort.by("viewcount").descending();
    // } else if ("like".equals(bsort)) {
    // sort = Sort.by("likesCount").descending();
    // } else {
    // sort = Sort.by("idx").descending();
    // }

    // Pageable pageable = PageRequest.of(0, 15, sort);
    // Page<BoardDto> boardList = boardService.getAllBoards(category, bsort,
    // pageable);

    // List<List<Hashtag>> hashtagsList = new ArrayList<>();
    // for (BoardDto board : boardList) {
    // List<Hashtag> hashtags = boardService.getHashtagsForBoard(board.getIdx());
    // hashtagsList.add(hashtags);
    // }
    // model.addAttribute("boardList", boardList.getContent());
    // model.addAttribute("category", category);
    // model.addAttribute("hashtagsList", hashtagsList);
    // return "board/board_list";
    // }

    @GetMapping("/board_list")
    public String boardList(@RequestParam(value = "category", defaultValue = "all") String category,
            @RequestParam(value = "bsort", defaultValue = "new") String bsort,
            Model model) {

        Sort sort;
        if ("new".equals(bsort)) {
            sort = Sort.by("modifiedDate").descending();
        } else if ("view".equals(bsort)) {
            sort = Sort.by("viewcount").descending();
        } else if ("like".equals(bsort)) {
            sort = Sort.by("likesCount").descending();
        } else {
            sort = Sort.by("idx").descending();
        }

        Pageable pageable = PageRequest.of(0, 15, sort);
        Page<BoardDto> boardList = boardService.getAllBoards(category, bsort,
                pageable);

        model.addAttribute("boardList", boardList.getContent());
        model.addAttribute("category", category);
        return "board/board_list";
    }

    @GetMapping("/board_list_ajax")
    @ResponseBody
    public Page<BoardDto> boardListAjax(
            @RequestParam(value = "category", defaultValue = "all") String category,
            @RequestParam(value = "bsort", defaultValue = "new") String bsort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "15") int size) {

        Sort sort;
        if ("new".equals(bsort)) {
            sort = Sort.by("modifiedDate").descending();
        } else if ("view".equals(bsort)) {
            sort = Sort.by("viewcount").descending();
        } else if ("like".equals(bsort)) {
            sort = Sort.by("likesCount").descending();
        } else {
            sort = Sort.by("idx").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return boardService.getAllBoards(category, bsort, pageable);
    }

    @GetMapping("/board_detail/{idx}")
    public String boardDetail(@PathVariable(name = "idx") Long idx, Model model,
            HttpServletRequest request,
            HttpServletResponse response, Authentication authentication) {
        BoardDto boardDTO = boardService.getBoardById(idx);
        List<ReplyDto> replyList = replyService.findByBoardId(idx);
        Cookie[] cookies = request.getCookies();
        String boardIdx = "[" + boardDTO.getIdx() + "]";
        boolean isCookieExist = false;

        List<Hashtag> hashtags = boardService.getHashtagsForBoard(idx);

        for (int i = 0; cookies != null && i < cookies.length; i++) {
            if (cookies[i].getName().equals("postView")) {
                if (!cookies[i].getValue().contains(boardIdx)) {
                    boardService.incrementViewCount(idx);
                    String newCookieValue = cookies[i].getValue() + boardIdx;
                    cookies[i].setValue(newCookieValue);
                    response.addCookie(cookies[i]); // 쿠키 값을 변경한 후에는 반드시 응답에 추가해야 합니다.
                }
                isCookieExist = true;
                break;
            }
        }

        if (!isCookieExist) {
            boardService.incrementViewCount(idx);
            Cookie cookie = new Cookie("postView", boardIdx);
            response.addCookie(cookie);
        }

        if (authentication != null) {
            String rid = authentication.getName(); // 로그인한 사용자의 id 가져오기
            Optional<Member> id = memberRepository.findByid(rid);
            Integer ridx = id.get().getIdx();
            model.addAttribute("ridx", ridx);
        }
        model.addAttribute("hashtags", hashtags);
        model.addAttribute("board", boardDTO);
        model.addAttribute("replyList", replyList);

        return "board/board_detail";
    }

    @GetMapping("/board_update/{idx}")
    public String boardUpdate(@PathVariable("idx") Long idx, Model model) {
        BoardDto boardDTO = boardService.getBoardById(idx);
        model.addAttribute("board", boardDTO);
        return "board/board_update";
    }

    @PostMapping("/board_update/{idx}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateBoard(@PathVariable("idx") Long idx,
            @RequestBody BoardDto updatedBoardDto) {
        boardService.updateBoard(idx, updatedBoardDto);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/board_delete/{idx}")
    public String boardDelete(@PathVariable("idx") Long idx, @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        String rid = userDetails.getUsername();
        Optional<Member> id = memberRepository.findByid(rid);
        Integer userId = id.get().getIdx();

        boardService.deleteBoard(idx, userId);
        redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        return "redirect:/board/board_list";
    }
}
