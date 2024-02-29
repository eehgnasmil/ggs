package ggs.ggs.controller;

import ggs.ggs.service.LikeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final @Qualifier("goodsLikeServiceImpl") LikeService goodsLikeService;
    private final @Qualifier("memberLikeServiceImpl") LikeService memberLikeService;
    private final @Qualifier("boardLikeServiceImpl") LikeService boardLikeService;
    private final @Qualifier("boardReplyLikeServiceImpl") LikeService replyLikeService;

    public LikeController(@Qualifier("goodsLikeServiceImpl") LikeService goodsLikeService,
            @Qualifier("memberLikeServiceImpl") LikeService memberLikeService,
            @Qualifier("boardLikeServiceImpl") LikeService boardLikeService,
            @Qualifier("boardReplyLikeServiceImpl") LikeService replyLikeService) {
        this.goodsLikeService = goodsLikeService;
        this.memberLikeService = memberLikeService;
        this.boardLikeService = boardLikeService;
        this.replyLikeService = replyLikeService;
    }

    @ResponseBody
    @PostMapping("/goodsLike")
    public Boolean goodsLike(@RequestParam("goods") Integer goods, Authentication authentication) {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();

        Boolean goodsLike = goodsLikeService.like(goods, sid);
        return goodsLike;
    }

    @ResponseBody
    @PostMapping("/boardLike")
    public Boolean boardLike(@RequestParam("board") Integer board, Authentication authentication) {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();

        Boolean boardLike = boardLikeService.like(board, sid);
        return boardLike;
    }

    @ResponseBody
    @PostMapping("/replyLike")
    public Boolean replyLike(@RequestParam("reply") Integer reply, Authentication authentication) {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();

        Boolean replyLike = replyLikeService.like(reply, sid);
        return replyLike;
    }


}
