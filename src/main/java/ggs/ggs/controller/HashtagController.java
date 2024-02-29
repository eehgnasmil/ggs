package ggs.ggs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ggs.ggs.board.BoardTagRepository;
import ggs.ggs.domain.Hashtag;
import ggs.ggs.service.HashtagService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/hash")
@RequiredArgsConstructor
public class HashtagController {

    private final BoardTagRepository boardTagRepository;
    private final HashtagService boardTagService;

    @GetMapping("/hashtags")
    public ResponseEntity<List<String>> getHashtags() {
        List<Hashtag> hashtags = boardTagRepository.findAll();
        List<String> hashtagNames = hashtags.stream()
                .map(Hashtag::getHashtag)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hashtagNames);
    }

    // @GetMapping("topHashtags")
    // public String getHashtagPage(Model model) {
    // List<Object[]> topHashtags = boardTagService.getTopHashtags(10);
    // model.addAttribute("topHashtags", topHashtags);
    // return "hashtagPage";
    // }

    // @GetMapping("/hashtagList")
    // public String renderHashtagList(Model model) {
    //     List<Object[]> topHashtags = boardTagService.getTopHashtags(10);
    //     model.addAttribute("topHashtags", topHashtags);
    //     return "hashtag_list";
    // }

}
